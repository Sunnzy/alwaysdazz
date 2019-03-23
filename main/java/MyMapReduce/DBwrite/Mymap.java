package MyMapReduce.DBwrite;

import MyMapReduce.Wordcount.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Mymap extends Mapper<LongWritable,Text, LongWritable, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        context.write(key,value);

    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {

        Job instance = Job.getInstance();

        Configuration configuration = instance.getConfiguration();
//        DBConfiguration.configureDB(configuration,"org.mariadb.jdbc.Driver","jdbc:mariadb://192.168.1.31:3306/test","root","root");

        FileSystem fileSystem = FileSystem.get(configuration);
        Path path = new Path("/home/thanatos/aaa");

        if(fileSystem.exists(path)){
            fileSystem.delete(path,true);
        }

        instance.setJarByClass(Mymap.class);
        instance.setMapperClass(Mymap.class);

        instance.setInputFormatClass(MyIntputFormat.class);


//        instance.setMapOutputKeyClass(Text.class);
//        instance.setMapOutputValueClass(Text.class);

        instance.setOutputKeyClass(LongWritable.class);
        instance.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/thanatos/test/aaa.txt"));
        //定义Reduce任务的数量
        //      instance.setNumReduceTasks(3);

//        DBInputFormat.setInput(instance,MyReadMysql.class,"bg06","age>10",null,"name","age");

//        DBInputFormat.setInput(instance,MyReadMysql.class,"select name,age from bg06 where age > 10","select count(1) from bg06");

        FileOutputFormat.setOutputPath(instance,path);


        System.exit(instance.waitForCompletion(true)?0:1);


    }

}
