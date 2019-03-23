package DDL;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.*;

public class PageViewProcess {
    /**
     * Title: PageViewProcess
     * Description:
     * 	点击流模型(pv)处理
     * 	按照session聚集访问页面信息
     * 	本例中因为没有sessionid，所以只能通过ip地址进行区别
     * 	同时输出数据时补充上随机sessionid用于visit模型区分
     * @author yangzheng
     * @date 2018年3月12日
     */

    static class PageViewMapper extends Mapper<LongWritable,Text,Text,WebLogBean>
    {
        //优化内存 减少垃圾
        private Text k=new Text();
        private WebLogBean v=new WebLogBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //拿取一行 读取数据
            String line = value.toString();
            //按照\001来切
            String[] arr = line.split(WebLogBeanParser.SPLIT_DELIMITER);
            //这里根据ip聚集，将同一个用户的访问页面聚合在一起交由reduce处理
            k.set(arr[1]);

            v.setValid(Boolean.valueOf(arr[0]));
            v.setRemote_addr(arr[1]);
            v.setRemote_user(arr[2]);
            v.setTime_local(arr[3]);
            v.setRequest(arr[4]);
            v.setStatus(arr[5]);
            v.setBody_bytes_sent(arr[6]);
            v.setHttp_referer(arr[7]);
            v.setHttp_user_agent(arr[8]);

            context.write(k,v);
        }
    }


        static class PageViewReducer extends Reducer<Text,WebLogBean,PageViemBean, NullWritable>
        {
            //默认首页和尾页停留时长为60s
            private static final String DEFAULT_STEPLONG="60";

            @Override
            protected void reduce(Text key, Iterable<WebLogBean> values, Context context) throws IOException, InterruptedException {

                Collection<WebLogBean> beans=new ArrayList<>();//为bean准备一个array容器

                for(WebLogBean value:values)
                {
                    WebLogBean bean=new WebLogBean();//准备一个对象

                    try {
                        BeanUtils.copyProperties(bean,value);//将value的值复制给bean
                    } catch (Exception e) {
                        throw new ServiceRuntimeException(ExceptionCode.COMMON.BEAN_ATTR_COPY_EXCEPTION, "无法复制webbean对象");
                    }

                    beans.add(bean);//添加bean到beans数组中
                }

                // 根据访问时间对同一个用户的访问记录排序

                ((ArrayList<WebLogBean>) beans).sort((WebLogBean o1, WebLogBean o2) -> {//与老师的出入不一样
                    Date time1 = WebLogBeanParser.toData(o1.getTime_local());
                    Date time2 = WebLogBeanParser.toData(o2.getTime_local());

                    if (!Optional.ofNullable(time1).isPresent()
                            || !Optional.ofNullable(time2).isPresent()) {
                        return 0;
                    }

                    return time1.compareTo(time2);	// -1 1
                });

                // 为每一个用户生成一个临时的sessionid
                String session=createSession();
                PageViemBean pvBean=new PageViemBean();//new一个对象
                WebLogBean bean;
                WebLogBean preBean;
                int step=1;//第一步
                for(int i=0;i<beans.size();i++)
                {

                    bean=((ArrayList<WebLogBean>) beans).get(i);//这里强转了与老师代码不一样
                    if(beans.size() == 1)//如果该用户在该会话走了一步则为正确数据 输入到数据库中
                    {
                        pvBean.set(session, bean.getRemote_addr(), bean.getHttp_user_agent(), bean.getTime_local(),
                                bean.getRequest(), 1, DEFAULT_STEPLONG, bean.getHttp_referer(), bean.getBody_bytes_sent(), bean.getStatus());
                        context.write(pvBean, NullWritable.get());
                        break;
                    }

                    if (i == 0) {//如果步数为零则为脏数据 该用户没有访问任何会话
                        continue;
                    }

                    // 计算上一页面的停留时间，由当前页访问时间-上一页访问时间
                    preBean = ((ArrayList<WebLogBean>) beans).get(i-1);//这里强转了与老师代码不一样
                    long timeDiff=WebLogBeanParser.timediff(bean.getTime_local(),preBean.getTime_local());

                    // 这里需要注意的是，session认为30分钟失效，所以超过30分钟之后需要重新生成会话
                    if (timeDiff > 30 * 60 * 1000) {
                        pvBean.set(session, preBean.getRemote_addr(), preBean.getHttp_user_agent(), preBean.getTime_local(),
                                preBean.getRequest(), step, DEFAULT_STEPLONG, preBean.getHttp_referer(), preBean.getBody_bytes_sent(), preBean.getStatus());
                        context.write(pvBean, NullWritable.get());
                        step = 1;
                        session = createSession();
                    } else {
                        pvBean.set(session, preBean.getRemote_addr(), preBean.getHttp_user_agent(), preBean.getTime_local(),
                                preBean.getRequest(), step, (timeDiff / 1000)+"", preBean.getHttp_referer(), preBean.getBody_bytes_sent(), preBean.getStatus());
                        context.write(pvBean, NullWritable.get());
                        step++;
                    }

                    // 如果当前遍历到最后一页，还需要直接把最后一条记录直接输出
                    if (i == beans.size() - 1) {
                        pvBean.set(session, bean.getRemote_addr(), bean.getHttp_user_agent(), bean.getTime_local(),
                                preBean.getRequest(), step, DEFAULT_STEPLONG, bean.getHttp_referer(), bean.getBody_bytes_sent(), bean.getStatus());
                        context.write(pvBean, NullWritable.get());
                    }

                }
            }

            //创建session
            private String createSession()
            {
                return UUID.randomUUID().toString();//生成的随机id
            }
        }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(PageViewProcess.class);

        job.setMapperClass(PageViewMapper.class);
        job.setReducerClass(PageViewReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WebLogBean.class);

        job.setOutputKeyClass(PageViemBean.class);
        job.setOutputValueClass(NullWritable.class);

//        Path output = new Path(args[1]);
//        FileSystem fs = FileSystem.get(conf);
//        if (fs.exists(output)) {
//            fs.delete(output, true);
//        }
//
//        FileInputFormat.setInputPaths(job, new Path(args[0]));
//        FileOutputFormat.setOutputPath(job, output);

        //测试
        FileInputFormat.setInputPaths(job, new Path("/home/alwaysdazz/part-m-00000"));
        FileOutputFormat.setOutputPath(job, new Path("/home/alwaysdazz/1.txt"));

        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }

}
