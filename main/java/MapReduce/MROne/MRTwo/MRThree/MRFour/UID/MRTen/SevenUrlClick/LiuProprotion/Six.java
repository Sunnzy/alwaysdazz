package MapReduce.MROne.MRTwo.MRThree.MRFour.UID.MRTen.SevenUrlClick.LiuProprotion;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Six {
//查询次数大于2次的用户占比,以用户为Key map算出总的用户总KEY，然后将大于2次的用户key发送给reduce


    //MR计数器使用枚举来定义的

       public enum MyCounter{

        MAPCOUNT,
        REDUCEKEYCOUNT

        }


    static class mymm extends Mapper<LongWritable, Text,Text, LongWritable>
    {


        LongWritable v=new LongWritable();
        Text k=new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //这边按行拿取
            String[] split = value.toString().split("\\|");
            k.set(split[1]);//这是拿去的key
            v.set(1);//这是对应key的111
            context.write(k,v);

        }
    }

    static class myru extends Reducer<Text,LongWritable,Text,LongWritable>
    {
        //这边按key拿取
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

            context.getCounter(MyCounter.MAPCOUNT).increment(1);
           long count=0;//这是总的用户次数
            for(LongWritable lo:values)
            {
                count++;//这里拿到聚合之后的用户id
            }
            if(count>2)//这是大于2次的用户次数
            {
                context.getCounter(MyCounter.REDUCEKEYCOUNT).increment(1);
            }
                context.write(new Text("helloworld"),new LongWritable());
        }


    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        Job instance = Job.getInstance();

        Configuration configuration = instance.getConfiguration();
        FileSystem fileSystem = FileSystem.newInstance(configuration);
        //job主要是用来定义整个作业所依赖的mapper，reducer，driver
        instance.setJarByClass(Six.class);
        instance.setMapperClass(mymm.class);
        instance.setReducerClass(myru.class);

        instance.setMapOutputKeyClass(Text.class);//map的输出k
        instance.setMapOutputValueClass(LongWritable.class);//map的输出v

        instance.setOutputKeyClass(Text.class);  //最终的key输出
        instance.setOutputValueClass(LongWritable.class);//最终的v输出

        Path path = new Path("/home/alwaysdazz/1.txt");
        if(fileSystem.exists(path)){
            fileSystem.delete(path,true);
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop之MapReduce/day11/sg.txt"));//设置输入路径
        FileOutputFormat.setOutputPath(instance,path);

        instance.waitForCompletion(true); //等待输出

        Counters counters = instance.getCounters();//在这里拿到计数器的实例
        Counter mapcounter = counters.findCounter(MyCounter.MAPCOUNT);//拿到map计数的实例
        Counter reducecounter = counters.findCounter(MyCounter.REDUCEKEYCOUNT);//拿到reduce计数的实例

        System.out.println("**"+mapcounter.getValue());//拿到map的总和
        System.out.println("++"+reducecounter.getValue());//拿到reduce的总和

        System.out.println("//"+(1.0f)*reducecounter.getValue()/mapcounter.getValue());





    }





}
