package MapReduce.MROne.MRTwo.MRThree.MRFour.UID.MRTen;


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
import java.net.URISyntaxException;

public class TenClass {
//(10)查询搜索过”伟大导演“的uid，并且搜索伟大导演的次数大于3
    /*
    1.key=伟大导演， value=uid，以uid为key查询搜索伟大导演的次数大于3，输出 uid
    想reduce输出的是 uid和伟大导演 然后以uid为key聚合 在value删选次数大于三的uid
     */

    static class Mymapper extends Mapper<LongWritable,Text,Text,LongWritable>{
        LongWritable lo=new LongWritable();
        Text k=new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("\t");
           // if(split.length>2) {
                if (split[2].equals("仙剑奇侠传")) {
                    lo.set(1);//伟大导演
                    k.set(split[1]);//用户uid
                    context.write(k, lo);

             //   }
            }
        }
    }

    static class myreuce extends Reducer<Text,LongWritable,Text,Text>
    {

        long count;
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
     //这里筛选大于三的伟大导演
             count=0;
            for(LongWritable t:values)
            {
                count+=t.get();//将相同id的次数累加
                System.out.println(count);
            }
            if(count>3)
            {
                context.write(key,new Text(""));
            }

        }
    }



    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {

        //调用job方法
        Job ins = Job.getInstance();
        Configuration configuration = ins.getConfiguration();
        FileSystem fileSystem = FileSystem.get(configuration);
        ins.setMapperClass(Mymapper.class);//要指定的mapper
        ins.setReducerClass(myreuce.class);
        ins.setJarByClass(TenClass.class);
        ins.setMapOutputKeyClass(Text.class);//map输出
        ins.setMapOutputValueClass(LongWritable.class);
        ins.setOutputKeyClass(Text.class);//最终输出
        ins.setOutputValueClass(Text.class);
        if(fileSystem.exists(new Path("/home/alwaysdazz/4.txt")))
        {
            fileSystem.delete(new Path("/home/alwaysdazz/4.txt"),true);
        }

        FileInputFormat.setInputPaths(ins,new Path("/home/alwaysdazz/桌面/Hadoop之MapReduce/day11/sogou.500w.utf8"));//输入路径

        FileOutputFormat.setOutputPath(ins,new Path("/home/alwaysdazz/4.txt"));//输出路径

        System.exit(ins.waitForCompletion(true)?0:1);

    }

}
