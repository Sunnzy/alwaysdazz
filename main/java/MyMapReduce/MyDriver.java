package MyMapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MyDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {

       //调用job方法
        Job ins = Job.getInstance();

//        Configuration configuration = ins.getConfiguration();
//
//        URI uri = new URI("hdfs://192.168.1.137:9000");
//
//        FileSystem fileSystem = FileSystem.get(uri,configuration);


        ins.setMapperClass(MyMapper.class);//要指定的mapper

        ins.setReducerClass(MyReduce.class);

        ins.setJarByClass(MyDriver.class);

        ins.setMapOutputKeyClass(Text.class);//map输出

        ins.setMapOutputValueClass(LongWritable.class);

        ins.setOutputKeyClass(LongWritable.class);//最终输出

        ins.setOutputValueClass(Text.class);


//        if(fileSystem.exists(new Path("/4.txt")))
//        {
//            fileSystem.delete(new Path("/4.txt"),true);
//        }

        FileInputFormat.setInputPaths(ins,new Path("/home/alwaysdazz/1.txt"));//输入路径

        FileOutputFormat.setOutputPath(ins,new Path("/home/alwaysdazz/4.txt"));//输出路径

        System.exit(ins.waitForCompletion(true)?0:1);

    }

}
