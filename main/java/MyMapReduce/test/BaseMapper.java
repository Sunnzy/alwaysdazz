package MyMapReduce.test;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class BaseMapper extends Mapper<LongWritable,Text, Text,JavaBeann> {

    Text text=new Text();
    JavaBeann jv=new JavaBeann();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {


        String[] split = value.toString().split("\t");

        text.set(split[0].trim());//这个是基站

        jv.setUpload(Long.parseLong(split[7].trim()));//这个是上行流量

        jv.setDownload(Long.parseLong(split[8].trim()));
        context.write(text,jv);

    }
}
