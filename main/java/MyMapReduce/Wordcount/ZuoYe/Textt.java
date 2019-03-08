package MyMapReduce.Wordcount.ZuoYe;


import MyMapReduce.test.Base2;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;

import java.io.IOException;

public class Textt {
/*
    1、根据性别、会员级别统计出乘客数量占比
    男6-0，女6-1，男5-2，女5-3,其他-4
    格式如下：性别、级别、数量、占比
	（使用分区)

*/
        static class mymapp extends Mapper<LongWritable, Text, JavaBeann, LongWritable>
        {
            JavaBeann jv=new JavaBeann();

            @Override
            protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

                String[] sp = value.toString().split(",");
                jv.setSex(sp[3]);
                jv.setGande(sp[4]);

                context.write(jv,new LongWritable(1));
            }
        }


        static class myreduce extends Reducer<JavaBeann,LongWritable,Text,Text>
        {
            @Override
            protected void reduce(JavaBeann key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
                int sum =0;

                float zb=0.0f;
                for(LongWritable vip:values)
                {
                    sum+=vip.get();
                }
                System.out.println(sum);
                zb=1.0f*sum/62988;
                context.write(new Text(String.valueOf(key)),new Text(sum+","+zb));
            }
        }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();
        instance.setJarByClass(Base2.class);

        Configuration conf=new Configuration();
        FileSystem fileSystem = FileSystem.get(conf);
        Path patn=new Path("/home/alwaysdazz/8.txt");
        if(fileSystem.exists(patn))
        {
            fileSystem.delete(patn,true);
        }

        instance.setMapperClass(mymapp.class);
        instance.setMapOutputKeyClass(JavaBeann.class);
        instance.setMapOutputValueClass(LongWritable.class);

        instance.setPartitionerClass(MyPartition.class);
        instance.setNumReduceTasks(5);

        instance.setReducerClass(LongSumReducer.class);
        instance.setOutputKeyClass(JavaBeann.class);
        instance.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/2.csv"));
        FileOutputFormat.setOutputPath(instance,patn);

        System.exit(instance.waitForCompletion(true)?0:1);

    }

}
