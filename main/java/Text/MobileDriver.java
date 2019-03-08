package Text;

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

        Job instance = Job.getInstance();//调用job的实例

        Configuration conf=new Configuration();
        FileSystem fm = FileSystem.get(conf);

        instance.setJarByClass(MobileDriver.class);

        instance.setMapperClass(MobileMapper.class);

        instance.setReducerClass(MobileReduce.class);
        //map输出key
        instance.setMapOutputKeyClass(MobileWritble.class);
        //map输出value
        instance.setMapOutputValueClass(Text.class);
        //reduce输出kv
        instance.setOutputKeyClass(MobileWritble.class);
        instance.setOutputValueClass(Text.class);

        Path path=new Path("/6.txt");

        if(fm.exists(path))//如果输出的路径存在
        {
            fm.delete(path,true);//则删除
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/mobileflow"));
        FileOutputFormat.setOutputPath(instance,path);

        System.exit(instance.waitForCompletion(true)?0:1);
    }

}
