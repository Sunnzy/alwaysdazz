package MyMapReduce.UMapReduce.Mobile;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


//  继承mapper
public class MobileMapper extends Mapper<LongWritable, Text,Text,MyText>{

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //将数据进行切分
Text text=new Text();//键
MyText my=new MyText();//值

        String[] split = value.toString().split("\t");

           text.set(split[0]);//拿基站
           my.setPhone(split[1]);
            my.setUpload(Long.parseLong(split[7].trim()));//将手机号强转为long型 并去除该数据的前后空格
            my.setDownload(Long.parseLong(split[8].trim()));
            my.setSumload(Long.parseLong(split[7].trim())+ Long.parseLong(split[8].trim()));//总流量
            context.write(text,my);
            /*
            键（基站）
            值（手机号，上下流量，总流量）
             */

    }
}
