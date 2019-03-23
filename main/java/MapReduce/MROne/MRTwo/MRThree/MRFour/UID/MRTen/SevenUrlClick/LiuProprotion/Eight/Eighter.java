package MapReduce.MROne.MRTwo.MRThree.MRFour.UID.MRTen.SevenUrlClick.LiuProprotion.Eight;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Eighter {

private static long count;
//(8)Rank在10以内的点击次数占比,先判断3号位是否大于10,在计算总数,在计算占比
    static class mm extends Mapper<LongWritable,Text, Text, LongWritable>
    {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        count++;
        System.out.println(count+"count");
        String[] split = value.toString().split("\t");

           if(Long.parseLong(split[3])<10)
           {

               context.write(new Text(""),new LongWritable(1));
           }
    }
    }

    static class vv extends Reducer<Text,LongWritable,Text, DoubleWritable>
    {
        long sum=0;
         double fl=0;
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

            for(LongWritable lo:values)
            {
                sum++;
                System.out.println(sum+"sum");
            }
            fl=1.0000000f*sum/count;
          //  System.out.println(fl);
            context.write(new Text("占比为"),new DoubleWritable(fl));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        Job instance = Job.getInstance();

        Configuration configuration = instance.getConfiguration();
        FileSystem fileSystem = FileSystem.newInstance(configuration);
        //job主要是用来定义整个作业所依赖的mapper，reducer，driver
        instance.setJarByClass(Eighter.class);
        instance.setMapperClass(mm.class);
        instance.setReducerClass(vv.class);

        instance.setMapOutputKeyClass(Text.class);//map的输出k
        instance.setMapOutputValueClass(LongWritable.class);//map的输出v

        instance.setOutputKeyClass(Text.class);  //最终的key输出
        instance.setOutputValueClass(LongWritable.class);//最终的v输出

        Path path = new Path("/home/alwaysdazz/1.txt");
        if(fileSystem.exists(path)){
            fileSystem.delete(path,true);
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop之MapReduce/day11/sogou.500w.utf8"));//设置输入路径
        FileOutputFormat.setOutputPath(instance,path);
        System.exit(instance.waitForCompletion(true)?0:1);

//        Counters counters = instance.getCounters();//在这里拿到计数器的实例
//        Counter mapcounter = counters.findCounter(Six.MyCounter.MAPCOUNT);//拿到map计数的实例
//        Counter reducecounter = counters.findCounter(Six.MyCounter.REDUCEKEYCOUNT);//拿到reduce计数的实例
//
//        System.out.println("**"+mapcounter.getValue());//拿到map的总和
//        System.out.println("++"+reducecounter.getValue());//拿到reduce的总和
//
//        System.out.println("//"+(1.0f)*reducecounter.getValue()/mapcounter.getValue());





    }



}
