package MyMapReduce.Wordcount.ZuoYe;

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

import java.io.IOException;

public class A  {

    static class c extends Mapper<LongWritable, Text,B,LongWritable>{

        B b=new B();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
        //拿去4号位
        b.setGanner(split[3].trim());
        //强转
        b.setGrade(split[4].trim());
        context.write(b,new LongWritable(1));
        System.out.println(b);
        }

    }
    static class d extends Reducer<B,LongWritable,B,LongWritable>
    {

        @Override
        protected void reduce(B key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
          //  long count=0;
             long sum=0;//初始化
            for(LongWritable lo:values)
            {
                sum+=lo.get();
                System.out.println(sum);
            }

            context.write(key,new LongWritable(sum));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();

        Configuration conf=new Configuration();
        FileSystem fileSystem = FileSystem.get(conf);
        Path path=new Path("/home/alwaysdazz/2.txt");
        if(fileSystem.exists(path))
        {
            fileSystem.delete(path,true);
        }

        instance.setJarByClass(A.class);
        instance.setMapperClass(c.class);
        instance.setMapOutputKeyClass(B.class);
        instance.setMapOutputValueClass(LongWritable.class);

        instance.setOutputValueClass(d.class);
        instance.setOutputKeyClass(B.class);
        instance.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/2.csv"));
        FileOutputFormat.setOutputPath(instance,path);
        System.exit(instance.waitForCompletion(true)?0:1);

    }
}
