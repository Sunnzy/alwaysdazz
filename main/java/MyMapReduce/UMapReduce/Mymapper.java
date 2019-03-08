package MyMapReduce.UMapReduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.regex.Pattern;

public class Mymapper extends Mapper<LongWritable,Text,Text,LongWritable>{

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        Text text=new Text();//新建一个类String格式的 拿到他的实例

        LongWritable lo=new LongWritable();//拿到类long的实例，这个实际指的是偏移量
        String[] s = value.toString().split(" ");

        for(String ss:s)
        {

            text.set(ss);//这个是每个词的组成

            lo.set(1);//这个计算值

            context.write(text,lo);

        }

    }
}
