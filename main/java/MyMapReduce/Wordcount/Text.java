package MyMapReduce.Wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class Text {
 //   检测相同基站下的手机号的上行下行和总和流量
//基于kEY比较基站和手机号
//然后进行上行下行和总流量


        static class mypapper extends Mapper<LongWritable,Text,Bsae,LL>
        {

            Bsae ba=new Bsae();
            LL l=new LL();
            @Override
            protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

                String[] split = value.toString().split("\t");
                ba.setBase(split[0]);
                ba.setPhone(split[1]);
                l.setUpload(Long.parseLong(split[7].trim()));
                l.setDownload(Long.parseLong(split[8].trim()));
                context.write(ba,l);

            }
        }


        static class myreducer extends Reducer<Bsae,LL,Bsae,LongWritable>
        {

            @Override           //相同基站下的手机号
            protected void reduce(Bsae key, Iterable<LL> values, Context context) throws IOException, InterruptedException {
                long upload=0;
                long download=0;
                long sumload=0;
                for(LL l:values)
                {
                    upload=l.getUpload();
                    download=l.getDownload();
                }
                sumload=upload+download;

                context.write(key,new LongWritable(Long.parseLong(upload+","+download+","+sumload)));//类型强转 不管最后是什么都要用text或者longwritable类型

            }
        }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();
        instance.setJarByClass(Text.class);

        FileSystem fileSystem = FileSystem.get(new Configuration());
        Path path=new Path("/home/alwaysdazz/10.txt");
        if(fileSystem.exists(path))
        {
            fileSystem.delete(path,true);
        }

        instance.setMapperClass(mypapper.class);
        instance.setMapOutputKeyClass(Bsae.class);
        instance.setMapOutputValueClass(LL.class);

        instance.setReducerClass(myreducer.class);
        instance.setOutputKeyClass(Bsae.class);
        instance.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/mobileflow"));
        FileOutputFormat.setOutputPath(instance,path);
        System.exit(instance.waitForCompletion(true)?0:1);


    }

























}
