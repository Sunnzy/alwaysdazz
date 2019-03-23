package MapReduce.MROne.MRTwo.MRThree.MRFour.UID.MRTen.SevenUrlClick;

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


//(9)直接输入URL查询的比例
public class SevenClick {
public static long l;
    static class mymapper extends Mapper<LongWritable,Text,Text,LongWritable>
    {
        Text t=new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            l++;
            System.out.println(l+"l");
            String[] split = value.toString().split("\\|");
                t.set("占比为");
                String pan="[a-zA-z]+://[^\\s]*";
                if(split[2].equals(split[5])||pan.matches(split[2]))
                {
                    context.write(t,new LongWritable(1));//以账户id为key 发送1and2

                }
        }
    }

    static class myrdu extends Reducer<Text,LongWritable,Text,Text>
    {

        long count =0;//计算总数
        float n=1.0f;
        long a=0;
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            System.out.println(count);

            for(LongWritable ss:values)//这里是总数
            {
               a+= ss.get();
               System.out.println(a+"++++");
            }
            n=1.0f*a/l;
            context.write(new Text("占比为"),new Text(""+n));
    }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();//调用job的实例

        Configuration conf=new Configuration();
        FileSystem fm = FileSystem.get(conf);

        instance.setJarByClass(SevenClick.class);

        instance.setMapperClass(mymapper.class);

        instance.setReducerClass(myrdu.class);
        //map输出key
        instance.setMapOutputKeyClass(Text.class);
        //map输出value
        instance.setMapOutputValueClass(LongWritable.class);
        //reduce输出kv
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(Text.class);

        Path path=new Path("/home/alwaysdazz/9.txt");

        if(fm.exists(path))//如果输出的路径存在
        {
            fm.delete(path,true);//则删除
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop之MapReduce/day11/sg.txt"));
        FileOutputFormat.setOutputPath(instance,path);

        System.exit(instance.waitForCompletion(true)?0:1);


    }



}
