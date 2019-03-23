package MapReduce.MROne.MRTwo.MRThree.MRFour;


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
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;

import java.io.IOException;

public class mrfour {

    static class fourmap extends Mapper<LongWritable, Text,Text,LongWritable>
    {


        Text v=new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("\\|");

            if(split.length>2&&!split[3].equals("1"))//大于2
            {

                    v.set(split[1]);//用户

                String name="总用户";
                context.write(new Text(name),new LongWritable(1));
            }

        }

    }


    static class fourreduce extends Reducer<Text,LongWritable,Text,LongWritable>
    {
        long count=0;
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

            for(LongWritable t:values)
            {

                    count+=t.get();
                    context.write(key,new LongWritable(count));


            }

        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();//调用job的实例

        Configuration conf=new Configuration();
        FileSystem fm = FileSystem.get(conf);

        instance.setJarByClass(mrfour.class);

        instance.setMapperClass(fourmap.class);

        instance.setReducerClass(fourreduce.class);
        //reduce输出kv
        instance.setOutputKeyClass(Text.class);
        instance.setMapOutputValueClass(LongWritable.class);

        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(LongWritable.class);

        Path path=new Path("/home/alwaysdazz/3.txt");

        if(fm.exists(path))//如果输出的路径存在
        {
            fm.delete(path,true);//则删除
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop/day11/sg.txt"));
        FileOutputFormat.setOutputPath(instance,path);

        System.exit(instance.waitForCompletion(true)?0:1);


    }

}
