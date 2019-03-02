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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class VisitProcess {

    static class VisitMapper extends Mapper<LongWritable, Text, Text, PageViemBean> {

        private Text k = new Text();
        private PageViemBean v = new PageViemBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String line = value.toString();
            // 665dfb07-28a2-4f99-95c7-43bdec3a6da6101.226.167.201/hadoop-mahout-roadmap/2013-09-18 09:30:36160"http://blog.fens.me/hadoop-mahout-roadmap/"10335200"Mozilla/4.0(compatible;MSIE8.0;WindowsNT6.1;Trident/4.0;SLCC2;.NETCLR2.0.50727;.NETCLR3.5.30729;.NETCLR3.0.30729;MediaCenterPC6.0;MDDR;.NET4.0C;.NET4.0E;.NETCLR1.1.4322;TabletPC2.0);360Spider"
            String[] arr = line.split(WebLogBeanParser.SPLIT_DELIMITER);
            String session = arr[0];//以会话窗口为key
            k.set(session);
            Integer step = Integer.parseInt(arr[4]);//选取步数
            v.set(session, arr[1], arr[9], arr[3], arr[2], step, arr[5], arr[6], arr[7], arr[8]);
            context.write(k, v);

        }
    }

    static class VisitReducer extends Reducer<Text, PageViemBean, VisitBean, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<PageViemBean> values, Context context) throws IOException, InterruptedException {
            List<PageViemBean> beans = new ArrayList<>();//创建一个PageViemBean类型的容器
            for (PageViemBean value : values) {
                PageViemBean bean = new PageViemBean();//创建一个实例对象
                try {
                    BeanUtils.copyProperties(bean, value);//将value拷贝到数组beans内
                } catch (Exception e) {
                    throw new ServiceRuntimeException(ExceptionCode.COMMON.BEAN_ATTR_COPY_EXCEPTION, "复制pageviewbean失败");
                }
                beans.add(bean);//向数组内添加内容
            }


            beans.sort((PageViemBean o1, PageViemBean o2) ->
            {
                Date time1 = WebLogBeanParser.toData(o1.getTimestr());
                Date time2 = WebLogBeanParser.toData(o2.getTimestr());
                if (!Optional.ofNullable(time1).isPresent() || !Optional.ofNullable(time2).isPresent()) {
                    return 0;
                }
                return time1.compareTo(time2);
            });

            // 取这次visit的首尾pageview记录，将数据放入VisitBean中

            VisitBean visitBean = new VisitBean();
            // 进入页面
            PageViemBean in = beans.get(0);
            visitBean.setInPage(in.getRequest());
            visitBean.setInTime(in.getTimestr());
            // 离开页面
            PageViemBean out = beans.get(beans.size() - 1);
            visitBean.setOutPage(out.getRequest());
            visitBean.setOutTime(out.getTimestr());
            // 访问的总页面数
            visitBean.setPageVisits(beans.size());
            visitBean.setSession(in.getSession());
            visitBean.setReferal(in.getReferal());
            visitBean.setRemote_addr(in.getRemote_addr());

            context.write(visitBean, NullWritable.get());

        }
    }
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(VisitProcess.class);

        job.setMapperClass(VisitMapper.class);
        job.setReducerClass(VisitReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(PageViemBean.class);

        job.setOutputKeyClass(VisitBean.class);
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
        FileInputFormat.setInputPaths(job, new Path("/home/alwaysdazz/1.txt/part-r-00000"));
        FileOutputFormat.setOutputPath(job, new Path("/home/alwaysdazz/2.txt"));

        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}