package HbaseApi.MapreduceApi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;
//用hbase使用mapreduce将数据大批量上传到表中
public class Write2HbaseDemo {
//将本地的字符串文件转换为Hfie文件上传至表中

    static class MyMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put>
    {

        ImmutableBytesWritable im=new ImmutableBytesWritable();//此类可以经string字符串转换为hfile文件格式,然后通过put操作将Hfile文件放入表中
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {


            String[] split = value.toString().split("\t");//按\t切分
            byte[] bytes = split[0].getBytes();//拿到rowkey
            im.set(bytes);//HFile

            Put put =new Put(bytes);
            put.addColumn("info".getBytes(),"name".getBytes(),"123".getBytes());

            context.write(im,put);//将值put上去
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Configuration entries = HBaseConfiguration.create();
        entries.set("hbase.zookeeper.quorum","192.168.1.137,192.168.1.138,192.168.1.139");
        entries.set("hbase.zookeeper.property.clientPort","2181");
        entries.set("zookeeper.znode.parent","/hbase-unsecure");


        Job exampleRead = Job.getInstance(entries, "ExampleRead");
        exampleRead.setJarByClass(Write2HbaseDemo.class);//输出主类 即main方法所在类
        exampleRead.setMapperClass(MyMapper.class);//map输出类
        exampleRead.setOutputKeyClass(ImmutableBytesWritable.class);//输出key
        exampleRead.setOutputValueClass(Put.class);//输出value

        FileInputFormat.setInputPaths(exampleRead,"/home/alwaysdazz/1.txt");//设置的输入路径
        TableMapReduceUtil.initTableReducerJob("student",null,exampleRead);

        boolean b = exampleRead.waitForCompletion(true);
        if(!b)
        {
            throw new IOException("error with job");
        }
    }
}
