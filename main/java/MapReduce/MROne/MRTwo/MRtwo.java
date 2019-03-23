package MapReduce.MROne.MRTwo;

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
//无重复的总条数
public class MRtwo {

    static class twomapper extends Mapper<LongWritable,Text,Text,LongWritable>
    {
       // ZuoyeLei zy=new ZuoyeLei();
        Text t=new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("\\|");
            if(split.length>2&&!split[2].equals("")){
           // zy.setName(split[2]);
            t.set(split[2]);
            context.write(t,new LongWritable(1) );

            }
            else
            {
                return;
            }
        }
    }




    static class tworeduce extends Reducer<Text,LongWritable,Text,LongWritable>
    {
        protected static int count;
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            count=0;
            for(LongWritable lo:values)
            {
                count+=lo.get();

            }
            context.write(new Text(String.valueOf(key)),new LongWritable(1));

        }
    }


    static class threemap extends Mapper<LongWritable,Text,LongWritable,LongWritable>
    {

        LongWritable lol=new LongWritable();
        LongWritable lo=new LongWritable();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("\\\t");
            lol.set(Long.parseLong(split[1]));
            System.out.println(lol+"***");
            lo.set(Long.parseLong(split[0]));
            context.write(lo,lol);
                        //1,内容

        }
    }


    static class threereduce extends Reducer<LongWritable,LongWritable,LongWritable,LongWritable>
    {
        long count=0;
        @Override
        protected void reduce(LongWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

            for(LongWritable lo:values)
            {

                count += lo.get();

            }

               context.write(key,new LongWritable(count));
            System.out.println(count);

        }
    }







    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        Job instance = Job.getInstance(conf);//调用job的实例

        FileSystem fm = FileSystem.get(conf);

        instance.setJarByClass(MRtwo.class);

        instance.setMapperClass(twomapper.class);

        instance.setReducerClass(tworeduce.class);
        //map输出key
        instance.setMapOutputKeyClass(Text.class);
        //map输出value
        instance.setMapOutputValueClass(LongWritable.class);
        //reduce输出kv
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(LongWritable.class);

        Path path=new Path("/home/alwaysdazz/7.txt");

        if(fm.exists(path))//如果输出的路径存在
        {
            fm.delete(path,true);//则删除
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop/day11/sg.txt"));
        FileOutputFormat.setOutputPath(instance,path);

        ControlledJob controlledJob = new ControlledJob(conf);//声明第一个job
        controlledJob.setJob(instance);
//============

        Job instance1 = Job.getInstance(conf);//调用job的实例

        instance1.setJarByClass(MRtwo.class);

        instance1.setMapperClass(threemap.class);

        instance1.setReducerClass(threereduce.class);
        //map输出key
        instance1.setMapOutputKeyClass(LongWritable.class);
        //map输出value
        instance1.setMapOutputValueClass(LongWritable.class);
        //reduce输出kv
        instance1.setOutputKeyClass(LongWritable.class);
        instance1.setOutputValueClass(LongWritable.class);

        Path path1=new Path("/home/alwaysdazz/8.txt");

        if(fm.exists(path1))//如果输出的路径存在
        {
            fm.delete(path1,true);//则删除
        }

        FileInputFormat.setInputPaths(instance1,new Path("/home/alwaysdazz/7.txt"));
        FileOutputFormat.setOutputPath(instance1,path1);

        ControlledJob controlledJob1 = new ControlledJob(conf);//生命第二个线程
        controlledJob1.setJob(instance1);

        controlledJob1.addDependingJob(controlledJob);//job2的实例依赖于job1的实例

        JobControl abc = new JobControl("abc");//分组
        abc.addJob(controlledJob);//在组内添加第一个job
        abc.addJob(controlledJob1);//在组内添加第二个job

        Thread t=new Thread(abc);//将组信息添加到线程中
        t.start();//开启线程
        while(true) {
            if (abc.allFinished()) {
                System.out.println(abc.getSuccessfulJobList());
                abc.stop();
                break;
            }

         //   System.exit(instance.waitForCompletion(true) ? 0 : 1);

        }
    }


}
