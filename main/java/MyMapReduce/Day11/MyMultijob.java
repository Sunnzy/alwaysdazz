package MyMapReduce.Day11;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
//多JOB关联
public class MyMultijob {

    static class Jobmap extends Mapper<LongWritable, Text,Text,JavaBeann>
    {   Text t=new Text();
        JavaBeann jv =new JavaBeann();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            t.set(split[0]);
            jv.setUpload(Long.parseLong(split[8]));
            jv.setDownload(Long.parseLong(split[9]));
            System.out.println(t);
            context.write(t,jv);
        }
    }

    static class Jobreduce extends Reducer<Text,JavaBeann,Text,Text>
    {
        //key base  value up down
        long sum=0;
        long up=0;
        long down =0;
        @Override
        protected void reduce(Text key, Iterable<JavaBeann> values, Context context) throws IOException, InterruptedException {
            //聚合
            for(JavaBeann t:values)
            {
                up=t.getUpload();
                down=t.getDownload();
                System.out.println(up+"////"+down);

            }
            sum=up+down;
            context.write(key,new Text(up+"\t"+down+"\t"+sum));
            System.out.println(sum);
        }
    }

                //因为这个表依赖于第一个表 所以 就相当于再次从第一个文件读
    static class SecondJobmap extends Mapper<LongWritable,Text,LongWritable,Text>
    {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("\t");

            long sum=Long.parseLong(split[3].toString().trim());//流量
            String t=split[0]+"\t"+split[1]+"\t"+split[2];
            context.write(new LongWritable(sum),new Text(t));
        }
    }


    static class SecondReduce extends Reducer<LongWritable,Text,Text,Text>
    {
        String str="";
        @Override
        protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

             for(Text t:values)
             {
                 String[] split = t.toString().split("\t");
                 if(split.length>1)
                 {
                     str=split[1]+split[2]+split[3];
                     context.write(new Text(split[0]),new Text(""+str));
                 }
             }
        }
    }


    public static void main(String[] args) throws IOException {



        Path path=new Path("/home/alwaysdazz/4.txt");
        Configuration conf = new Configuration();
        Job instance = Job.getInstance(conf);//加载conf
        FileSystem fileSystem = FileSystem.get(conf);
        if(fileSystem.exists(path))
        {
            fileSystem.delete(path,true);
        }

        instance.setJarByClass(MyMultijob.class);
        instance.setMapperClass(Jobmap.class);
        instance.setMapOutputKeyClass(Text.class);
        instance.setMapOutputValueClass(JavaBeann.class);

        instance.setReducerClass(Jobreduce.class);
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/mobileflow"));
        FileOutputFormat.setOutputPath(instance,path);
//========
        ControlledJob controlledJob = new ControlledJob(conf);//声明第一个job
        controlledJob.setJob(instance);//记载job
 //========

        Job instance2=Job.getInstance(conf);//加载相同的配置

        instance2.setJarByClass(MyMultijob.class);
        instance2.setMapperClass(SecondJobmap.class);
        instance2.setMapOutputKeyClass(LongWritable.class);
        instance2.setMapOutputValueClass(Text.class);

        instance2.setReducerClass(Jobreduce.class);
        instance2.setOutputKeyClass(Text.class);
        instance2.setOutputValueClass(SecondReduce.class);

        Path path2=new Path("/home/alwaysdazz/5.txt");
        if(fileSystem.exists(path2))
        {
            fileSystem.delete(path2,true);
        }
        //接第一个输出结果 变为输入路径
        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/4.txt"));
        FileOutputFormat.setOutputPath(instance,path2);
//============
        ControlledJob controlledJob1=new ControlledJob(conf);//加载一样的配置
        controlledJob1.setJob(instance2);//加载job
//==============

        controlledJob1.addDependingJob(controlledJob);//job2依赖于job1
        JobControl aaa = new JobControl("aaa");//组名随便输入
        aaa.addJob(controlledJob);
        aaa.addJob(controlledJob1);//将job1和job2添加给在总控制下的jobcont

        Thread t = new Thread(aaa);//必须将你的总控制器开启一个线程
        t.start();

        while (true) {
            if (aaa.allFinished()) {
                System.out.println(aaa.getSuccessfulJobList());
                aaa.stop();
                break;
            }
        }
    }
}
