package MyMapReduce.Day11;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class ChainMapReduceMap {
/*
根据基站总流量 正序输出

 */
    static class firstMapper extends Mapper<LongWritable,Text,Text,Text>
    {   Text t=new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(" ");
            long price= Long.parseLong(split[1]);
           if(price<100) {
               context.write(new Text(split[0]),new Text(""+price));
               System.out.println(t);
           }
           else
           {
               return;
           }
        }
    }

    static class firstReduce extends Reducer<Text,Text,Text,Text>
    {

        long count=0;
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //聚合
            for(Text t:values)
            {
                count+=Long.parseLong(t.toString());
                System.out.println("*****"+count);
            }

            context.write(key,new Text(""+count));
        }
    }

    static class secondly extends Mapper<Text,Text,Text,Text>
    {
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {

            if(key.toString().length()<3)
            {
                context.write(key,value);
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();
        instance.setJarByClass(ChainMapReduceMap.class);
        Path path=new Path("/home/alwaysdazz/3.txt");
        Configuration conf = instance.getConfiguration();
        FileSystem fileSystem = FileSystem.get(conf);
        if(fileSystem.exists(path))//第一条路径
        {
            fileSystem.delete(path,true);
        }
        //在这里定义map的输出
        instance.setMapOutputKeyClass(Text.class);
        instance.setMapOutputValueClass(LongWritable.class);
        //这里写链表连接的顺序
        ChainMapper.addMapper(instance,firstMapper.class,LongWritable.class,Text.class,Text.class,Text.class,conf);
        ChainReducer.setReducer(instance,firstReduce.class,Text.class,Text.class,Text.class,Text.class,conf);
        ChainMapper.addMapper(instance,secondly.class,Text.class,Text.class,Text.class,Text.class,conf);
        //控制最终的输出
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop/day11/a.txt"));
        FileOutputFormat.setOutputPath(instance,path);
        System.exit(instance.waitForCompletion(true)?0:1);
    }

}
