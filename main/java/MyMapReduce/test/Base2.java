package MyMapReduce.test;

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
/*
题目根据表一的结果按上行流量总和的大小排序
 */
public class Base2 {
/*
首先 根据第一个结果得出的结论
我们首先将实现LongWritable的compareTo方法 此实现可以将排序按倒叙输出（将表一的结果按倒叙输，）
mappper泛型 偏移量和内容 输出为以上行流量为key，基站下行流量和总流量为value输出
mappper第一步要做的 先切分 由table切分得出基站 就形成了分组0 和1 然后在1里面切分上行 下行 总流量 按，切割
这样我们得到了 按table建切分的 基站和按，切分的上行 下行 总流量 然后将上行流量以key方式发出去，其他以value发出去

接下来是reduce
reduce拿到的值是 以上行为key 基站 下行 总和为value的值
在这里我的目标是 将上行流量最大的前五个 输出
则我需要一个计数器 count<5因为 我是从0开始的 循环5次
将 values的内容遍历出来 拿到了基站 上 总
分别输出

*技术难点 应该是实现Longwritable
* 在比较大小的时候我们应该用longwritable的compareto的方法来比较 大小，因为正常是按正序输出 而我们需要的从大到小的前五个
* 则需要倒叙输出，且根据的是第一张表的输出结果所做到的
 */

                                             //这里调用的是 继承的Mylong 实现排序的方法
    static class Mymapper extends Mapper<LongWritable,Text,MyLong,Text>
{
    MyLong ml=new MyLong();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
                                 //偏移量，  流量

        String[] base = value.toString().split("\t");//第一次切基站 0和1

        String[] split = base[1].split(",");//将1里的字段按，来切
        //以上行流量为key发送,第一个为上行流量
        ml.set(Long.parseLong(split[0]));//这里要转 将字符串格式转为long类型的
        context.write(ml,new Text(base[0]+" "+split[1]+" "+split[2]));//这里发送了 key 和下行流量还有总和流量
                    //上行                基站          下行          总
    }
}


    static class Myreducer extends Reducer<MyLong,Text,MyLong,Text>
    {
        int count =0;

        @Override
        protected void reduce(MyLong key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            /*
             这里要算出前五名 因为compareto已经将大小排序好

              */
            if (count < 5) {    //如果小于5
                for (Text t : values) {//基站 下行流量 总和

                    String[] sp = t.toString().split(" ");// 基站 下行 总
                    String base=sp[0];
                    String doown=sp[1];
                    String summ=sp[2];
                    count++;//循环加一
                    context.write(key,new Text(doown+"  "+summ+"  "+base) );
                }
            }
        }
    }



    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();
        instance.setJarByClass(Base2.class);

        Configuration conf=new Configuration();
        FileSystem fileSystem = FileSystem.get(conf);
        Path patn=new Path("/home/alwaysdazz/8.txt");
        if(fileSystem.exists(patn))
        {
            fileSystem.delete(patn,true);
        }

        instance.setMapperClass(Mymapper.class);
        instance.setMapOutputKeyClass(MyLong.class);
        instance.setMapOutputValueClass(Text.class);

        instance.setReducerClass(Myreducer.class);
        instance.setOutputKeyClass(MyLong.class);
        instance.setMapOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/1.txt/part-r-00000"));
        FileOutputFormat.setOutputPath(instance,patn);

        System.exit(instance.waitForCompletion(true)?0:1);

    }


}
