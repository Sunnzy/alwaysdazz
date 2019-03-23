package MyMapReduce.test.Card;

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

public class Textt {

    static class mypapper extends Mapper<LongWritable, Text,Card,LongWritable>
    {   //切TMD
        Card ca=new Card();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
            ca.setCity(split[4]);
            ca.setCard(split[5]);
            context.write(ca,new LongWritable(1));

        }
    }


   static class myreducer extends Reducer<Card,LongWritable,Card,LongWritable>
   {
       @Override
       protected void reduce(Card key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
           int count =0;
        for(LongWritable lo:values)
        {
            count+=lo.get();

        }
            context.write(key,new LongWritable(count));

        }
   }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();
        instance.setJarByClass(Textt.class);

        FileSystem fileSystem = FileSystem.get(new Configuration());
        Path path=new Path("/home/alwaysdazz/9.txt");
        if(fileSystem.exists(path))
        {
            fileSystem.delete(path,true);
        }

        instance.setMapperClass(mypapper.class);
        instance.setMapOutputKeyClass(Card.class);
        instance.setMapOutputValueClass(LongWritable.class);

        instance.setReducerClass(myreducer.class);
        instance.setOutputKeyClass(Card.class);
        instance.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/2.csv"));
        FileOutputFormat.setOutputPath(instance,path);
        System.exit(instance.waitForCompletion(true)?0:1);


    }


}
