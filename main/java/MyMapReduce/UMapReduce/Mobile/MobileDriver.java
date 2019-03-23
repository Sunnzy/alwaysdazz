package MyMapReduce.UMapReduce.Mobile;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MobileDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {



        Job in = Job.getInstance();

        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(configuration);

        in.setJarByClass(MobileDriver.class);

        in.setMapperClass(MobileMapper.class);
        in.setMapOutputKeyClass(Text.class);
        in.setMapOutputValueClass(MyText.class);
        //reducer输出
        in.setReducerClass(MobileReducer.class);
        in.setOutputKeyClass(Text.class);
        in.setMapOutputValueClass(MyText.class);

        Path path =new Path(args[1]);//输出路径即第二个参数

        if(fileSystem.exists(path))//如果输出的路径存在
        {
            fileSystem.delete(path,true);//则删除

        }

        FileInputFormat.setInputPaths(in,args[0]);//源目标路径 即第一个参数
        FileOutputFormat.setOutputPath(in,path);//输出路径 第二个参数
        System.exit(in.waitForCompletion(true)?0:1);




/*
       //获得job的实例
        Job instance = Job.getInstance();
        Configuration conf =new Configuration();
        FileSystem fs=FileSystem.get(conf);

        instance.setJarByClass(MobileDriver.class);
            //mapper输出
        instance.setMapperClass(MobileMapper.class);
        instance.setMapOutputKeyClass(Text.class);
        instance.setMapOutputValueClass(MyText.class);
            //reducer输出
        instance.setReducerClass(MobileReducer.class);
        instance.setOutputKeyClass(Text.class);
        instance.setMapOutputValueClass(MyText.class);

        Path path =new Path("/home/alwaysdaz/7.txt");

       if(fs.exists(path))
       {
           fs.delete(path,true);
       }



        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/mobileflow"));
        FileOutputFormat.setOutputPath(instance,path);

        System.exit(instance.waitForCompletion(true)?0:1);

*/

    }
}
