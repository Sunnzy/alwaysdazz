package MyMapReduce.UMapReduce;

import MyMapReduce.MyReduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyDriver {


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job in = Job.getInstance();

        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(configuration);

        in.setJarByClass(MyDriver.class);
        in.setMapperClass(Mymapper.class);
        in.setReducerClass(MyReduce.class);

        in.setMapOutputKeyClass(Text.class);//map输出
        in.setMapOutputValueClass(LongWritable.class);

        in.setOutputKeyClass(Text.class);//reduce输出
        in.setOutputValueClass(LongWritable.class);

        Path path =new Path(args[1]);//输出路径即第二个参数

        if(fileSystem.exists(path))//如果输出的路径存在
        {
            fileSystem.delete(path,true);//则删除

        }

        FileInputFormat.setInputPaths(in,args[0]);//源目标路径 即第一个参数
        FileOutputFormat.setOutputPath(in,path);//输出路径 第二个参数
        System.exit(in.waitForCompletion(true)?0:1);

    }

}
