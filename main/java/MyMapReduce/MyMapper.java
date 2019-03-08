package MyMapReduce;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MyMapper extends Mapper <LongWritable,Text,Text,LongWritable>{
                        //第一行          //单词
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

         Text text=new Text();

        LongWritable lo=new LongWritable();

        String[] ss = value.toString().split(" ");

        for(String s:ss)
        {
            text.set(s);

            lo.set(1);

            context.write(text,lo);

        }

    }
}
