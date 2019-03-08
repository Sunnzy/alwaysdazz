package MapReduce.MROne;


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

public class MRone {

//(1)查询总条数非空排除
    static class firstmapper extends Mapper<LongWritable, Text,Text,LongWritable>
{
    String k=new String();
    LongWritable lo=new LongWritable();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] split = value.toString().split("\\|");
       if(split.length>4){
       k=split[1];
           // k.set(split[3].trim());
        lo.set(1);
        context.write(new Text(k),lo);
      }
       else {
           return;
       }

    }
}

    static class firstreduce extends Reducer<Text,LongWritable,Text,LongWritable>
    {
        long count=0;
//        LongWritable lo=new LongWritable();
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            for(LongWritable lo:values)
            {
                count+=lo.get();

            }

            context.write(key,new LongWritable(count));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();//调用job的实例

        Configuration conf=new Configuration();
        FileSystem fm = FileSystem.get(conf);

        instance.setJarByClass(MRone.class);

        instance.setMapperClass(firstmapper.class);

        instance.setReducerClass(firstreduce.class);
        //map输出key
        instance.setMapOutputKeyClass(Text.class);
        //map输出value
        instance.setMapOutputValueClass(LongWritable.class);
        //reduce输出kv
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(LongWritable.class);

        Path path=new Path("/home/alwaysdazz/6.txt");

        if(fm.exists(path))//如果输出的路径存在
        {
            fm.delete(path,true);//则删除
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop之MapReduce/day11/sg.txt"));
        FileOutputFormat.setOutputPath(instance,path);

        System.exit(instance.waitForCompletion(true)?0:1);


    }


}
