package MapReduce.MROne.MRTwo.MRThree;


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

public class threeMR {

    static class threeMymapper extends Mapper<LongWritable,Text,ClickRate,Text>
    {
       ClickRate cl=new ClickRate();
       Text v=new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("\\|");
            if(split.length>3) {

                cl.set(Long.parseLong(split[3]));
                v.set(split[2]);
                context.write(cl, v);
            }
        }
    }

    static class threeMyreduce extends Reducer<ClickRate,Text,Text,Text>
    {

        int count=0;
        @Override
        protected void reduce(ClickRate key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            if(count<6) {
                for (Text t : values) {

                    String a = t.toString();
                    count++;
                    context.write(new Text(a), new Text(String.valueOf(key)));
                }
            }

        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();//调用job的实例

        Configuration conf=new Configuration();
        FileSystem fm = FileSystem.get(conf);

        instance.setJarByClass(threeMR.class);

        instance.setMapperClass(threeMymapper.class);

        instance.setReducerClass(threeMyreduce.class);
        //map输出key
        instance.setMapOutputKeyClass(ClickRate.class);
        //map输出value
        instance.setMapOutputValueClass(Text.class);
        //reduce输出kv
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(Text.class);

        Path path=new Path("/home/alwaysdazz/2.txt");

        if(fm.exists(path))//如果输出的路径存在
        {
            fm.delete(path,true);//则删除
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop/day11/sg.txt"));
        FileOutputFormat.setOutputPath(instance,path);

        System.exit(instance.waitForCompletion(true)?0:1);


    }




}
