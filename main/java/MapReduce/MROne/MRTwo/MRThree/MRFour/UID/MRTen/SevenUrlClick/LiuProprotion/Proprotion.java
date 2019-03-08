package MapReduce.MROne.MRTwo.MRThree.MRFour.UID.MRTen.SevenUrlClick.LiuProprotion;

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

public class Proprotion {
//(7)查询次数大于2次的用户占比
    //k 用户查询俩次的数占所有用户查询次数的
    //先统计出总数量 在统计用户数


    static class mapp extends Mapper<LongWritable,Text,Text,LongWritable>
    {
        Text k=new Text();
        LongWritable v=new LongWritable();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("\\|");
            k.set(split[1]);//以id做聚合
            v.set(1);
            context.write(k,v);

        }
    }

    static class redu extends Reducer<Text,LongWritable,Text,Text>
    {

       long cs=0;
       long usr=0;

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
          long  sum=0;
            usr++;//用户总数
            for(LongWritable lo:values)
            {
                sum+=lo.get();//这里是根据用户进行聚合 1,1,1,2,3
            }
            if(sum>2)
            {
                cs++;//大于2的用户
            }
                context.write(key,new Text(usr+"|"+cs));
        }
    }

    static class lac extends Mapper<LongWritable,Text,hhhh,Text>
    {

        Text k=new Text();
        hhhh v=new hhhh();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("|");

            k.set("a");
            v.set(Long.parseLong((split[1])));//总的用户
            v.set(Long.parseLong((split[2])));//大于2词的用户
            context.write(v,k);

        }
    }


    static class cal extends Reducer<hhhh,Text,Text,LongWritable>
    {
        long count=0;
        float fl=1.0f;
        @Override
        protected void reduce(hhhh key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            String[] split = key.toString().split(",");
            if(count<2) {
                for (Text h : values) {

                            long a= Long.parseLong(split[0]);
                            long b= Long.parseLong(split[1]);
                            count++;
                    fl = 1.0f * a / b;
                    context.write(new Text("占比为"),new LongWritable((long) fl));
                }
            }



        }
    }



    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();//调用job的实例

        Configuration conf = new Configuration();
        FileSystem fm = FileSystem.get(conf);

        instance.setJarByClass(Proprotion.class);

        instance.setMapperClass(mapp.class);

        instance.setReducerClass(redu.class);
        //map输出key
        instance.setMapOutputKeyClass(Text.class);
        //map输出value
        instance.setMapOutputValueClass(LongWritable.class);
        //reduce输出kv
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(Text.class);

        Path path = new Path("/home/alwaysdazz/9.txt");

        if (fm.exists(path))//如果输出的路径存在
        {
            fm.delete(path, true);//则删除
        }

        FileInputFormat.setInputPaths(instance, new Path("/home/alwaysdazz/桌面/Hadoop之MapReduce/day11/sg.txt"));
        FileOutputFormat.setOutputPath(instance, path);

        ControlledJob controlledJob = new ControlledJob(conf);
        controlledJob.setJob(instance);//将第一个jjob放入实例中jar

        Job instance1 = Job.getInstance(conf);
        instance1.setJarByClass(Proprotion.class);

        instance1.setMapperClass(lac.class);

        instance1.setReducerClass(cal.class);
        //map输出key
        instance1.setMapOutputKeyClass(hhhh.class);
        //map输出value
        instance1.setMapOutputValueClass(Text.class);
        //reduce输出kv
        instance1.setOutputKeyClass(Text.class);
        instance1.setOutputValueClass(LongWritable.class);

        Path path1 = new Path("/home/alwaysdazz/10.txt");

        if (fm.exists(path1))//如果输出的路径存在
        {
            fm.delete(path1, true);//则删除
        }

        FileInputFormat.setInputPaths(instance1, path);
        FileOutputFormat.setOutputPath(instance1, path1);

        ControlledJob controlledJob1 = new ControlledJob(conf);
        controlledJob1.setJob(instance1);

        controlledJob1.addDependingJob(controlledJob);//job2依赖于job1

        JobControl aaa = new JobControl("aaa");
        aaa.addJob(controlledJob);
        aaa.addJob(controlledJob1);
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
