package HbaseApi.MapreduceApi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;
//Hbase对MapReduce提供支持，它实现了TableMapper类和TableReducer类，我们只需要继承这两个类即可
public class ReadFromHbase {
//用hbase使用mapreduce计算架构计算rowkey行数
        public enum ROWCOUNT{//枚举计数器
            Rowcount;
        }

    static class MyReadHbaseMapper extends TableMapper<Text, Text> {
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            /*
            byte[] bytes = key.get();//拿到rowkey
            while (value.advance()) {
                Cell current = value.current();//拿到该rowkey的当前cell
                 }
            */

            context.getCounter(ROWCOUNT.Rowcount).increment(1l);
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
//http://hbase.apache.org/book.html#mapreduce 官方文档注释 55.1. HBase MapReduce Read Example
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","192.168.1.137,192.168.1.138,192.168.1.139");//指定我们的zookeeper所在地（副所在地也可以）
        configuration.set("hbase.zookeeper.property.clientPort","2181");//指定zookeepr的端口号 如果2181被占用则另申请一个
        configuration.set("zookeeper.znode.parent","/hbase-unsecure");


            Job instance = Job.getInstance(configuration,"ExampleRead");
            instance.setJarByClass(ReadFromHbase.class);//指定程序的主类

            Scan scan =new Scan();
            scan.setCaching(500);//这里设置scan的缓存
            scan.setCacheBlocks(false);//官方提示 这里要设置false

        TableMapReduceUtil.initTableMapperJob(

                "student",//表名
                scan,//scan实例
                MyReadHbaseMapper.class,//继承的map类
                Text.class,//输出key
                Text.class,//输出key
                instance//实例
        );

        boolean b = instance.waitForCompletion(true);
        if(!b)
        {
            throw new IOException("error with job");
        }

        long value = instance.getCounters().findCounter(ROWCOUNT.Rowcount).getValue();//用枚举获取值
        System.out.println(value);
    }


}