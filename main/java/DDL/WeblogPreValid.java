package DDL;

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
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
//备份错误日志类，单独使用
public class WeblogPreValid {

    static class WeblogPreValidMapper extends Mapper<LongWritable, Text, Text, WebLogBean> {

        Collection<String> valids = new HashSet<>();
        Text k = new Text();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            valids = ValidUrlPrefixParser.parse();//setup在每次方法开始前只运行一次 加载配置
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String line = value.toString();

            WebLogBean bean = WebLogBeanParser.parse(line);

            if (!Optional.ofNullable(bean).isPresent()) {
                return;
            }
            // 过滤js/css/图片等静态资源文件
            // WebLogBeanParser.filtStaticResource(bean, valids);
            if (bean.isValid()) {
                k.set(bean.getRemote_addr());
                context.write(k, bean);//以用户id为key属性为value发送给ruduce
            }
        }
    }

    static class WeblogPreValidReducer extends Reducer<Text, WebLogBean, NullWritable, WebLogBean> {
        @Override
        protected void reduce(Text key, Iterable<WebLogBean> values, Context context) throws IOException, InterruptedException {
            for (WebLogBean bean : values) {
                context.write(NullWritable.get(), bean);
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(WeblogPreValid.class);

        job.setMapperClass(WeblogPreValidMapper.class);
        job.setReducerClass(WeblogPreValidReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WebLogBean.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(WebLogBean.class);

        Path output = new Path("/home/alwaysdazz/3.txt");
        //输出目录不能存在，否则报错

        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(output)) {
            fs.delete(output, true);
        }

        FileInputFormat.setInputPaths(job, new Path("/home/alwaysdazz/桌面/Hadoop之MapReduce/Hadoop之Hive/点击流日志分析/web-click-flow-master/GenerateApacheLog/1.log"));

        FileOutputFormat.setOutputPath(job,output);
        System.exit(job.waitForCompletion(true)?0:1);
    }

}
