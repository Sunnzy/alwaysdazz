package MyMapReduce.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class BaseDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();

        Configuration configuration = instance.getConfiguration();

        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path("/home/alwaysdazz/1.txt");

        if(fileSystem.exists(path)){
            fileSystem.delete(path,true);
        }

        instance.setJarByClass(BaseDriver.class);
        instance.setMapperClass(BaseMapper.class);
        instance.setReducerClass(BaseReducer.class);


        instance.setMapOutputKeyClass(Text.class);
        instance.setMapOutputValueClass(JavaBeann.class);

        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/mobileflow"));


        FileOutputFormat.setOutputPath(instance,path);


        System.exit(instance.waitForCompletion(true)?0:1);



















       /* Job instance = Job.getInstance();
        instance.setJarByClass(BaseDriver.class);

        instance.setMapperClass(BaseMapper.class);
        instance.setMapOutputKeyClass(Text.class);
        instance.setMapOutputValueClass(JavaBean.class);

        instance.setReducerClass(BaseReducer.class);
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(Text.class);

        Path path=new Path("/home/alwaysdazz/1.txt");

        Configuration conf=new Configuration();
        FileSystem fileSystem = FileSystem.get(conf);
        if(fileSystem.exists(path))
        {
            fileSystem.delete(path,true);
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/mobileflow"));
        FileOutputFormat.setOutputPath(instance,path);

        System.exit(instance.waitForCompletion(true)?0:1);*/
    }


}
