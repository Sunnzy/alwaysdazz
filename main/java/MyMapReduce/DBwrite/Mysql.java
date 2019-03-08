package MyMapReduce.DBwrite;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Mysql {

    static class myMapper extends Mapper<LongWritable,text, Text,LongWritable>
    {
        @Override
        protected void map(LongWritable key, text value, Context context) throws IOException, InterruptedException {
            String name=value.getName();
            int age=value.getAge();

            context.write(new Text(name),new LongWritable(age));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();
        Configuration configuration = instance.getConfiguration();
        DBConfiguration.configureDB(configuration,"org.mysql.jdbc.Driver","jdbc:mysql://127.0.0.1:3306/test","root","Alwaysdazz");
        FileSystem fileSystem = FileSystem.get(configuration);


        //连接数据库
        //注册驱动

        instance.setJarByClass(Mysql.class);
            //当做value做终极输出
        instance.setMapperClass(myMapper.class);
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(LongWritable.class);

        Path path=new Path("/home/alwaysdazz/1.txt");
        //判断路径
        if(fileSystem.exists(path))
        {
            fileSystem.delete(path,true);
        }
        //指定数据库的输入
        DBInputFormat.setInput(instance,text.class,"Hadoop",null,"age>10",null);
        //指定输出的路径
        FileOutputFormat.setOutputPath(instance,path);
        //等待执行
        System.exit(instance.waitForCompletion(true)?0:1);
    }

}
