package DDL;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

/*
日志预处理，清洗过滤
错误类①ExceptionCode
     ②ServiceRuntimeEXception
配置类①ValidUrlPrefixParser
拿取数据预清理：WebLogBeanParser
main方法：WebLogPreProcess
 */
public class WebLogPreProcess {

    static class WebLogPreProcessMapper extends Mapper<LongWritable, Text,WebLogBean, NullWritable>
    {
    Collection<String> valids=new HashSet<String>();

        @Override
        protected void setup(Context context){
            valids=ValidUrlPrefixParser.parse();//setup()，此方法被MapReduce框架仅且执行一次，在执行Map任务前，进行相关变量或者资源的集中初始化工作。若是将资源初始化工作放在方法map()中，导致Mapper任务在解析每一行输入时都会进行资源初始化工作，导致重复，程序运行效率不高！
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String line = value.toString();
            WebLogBean bean=WebLogBeanParser.parse(line);//数据预清理

            if(!Optional.ofNullable(bean).isPresent())
            {
                return;
            }
            // 过滤js/css/图片等静态资源文件
            // WebLogBeanParser.filtStaticResource(bean, valids);
            if(!bean.isValid())
            {
                return;
            }
            context.write(bean,NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {


        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);
        //只有map不用reduce
        job.setJarByClass(WebLogPreProcess.class);
        job.setMapperClass(WebLogPreProcessMapper.class);
        job.setOutputKeyClass(WebLogBean.class);
        job.setOutputValueClass(NullWritable.class);
        //输出路径
        //测试
        FileInputFormat.setInputPaths(job, new Path("/home/alwaysdazz/桌面/Hadoop之MapReduce/Hadoop之Hive/点击流日志分析/web-click-flow-master/GenerateApacheLog/1.log"));
        FileOutputFormat.setOutputPath(job, new Path("/home/alwaysdazz/0.txt"));
        //退出
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

