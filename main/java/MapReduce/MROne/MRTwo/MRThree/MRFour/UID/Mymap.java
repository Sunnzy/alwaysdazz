package MapReduce.MROne.MRTwo.MRThree.MRFour.UID;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Mymap {
// 独立UID总数
    static class myMapperd extends Mapper<LongWritable,Text,dd,LongWritable>
    {
        dd d=new dd();
        LongWritable lo=new LongWritable();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split("\\|");
            d.setId(split[0]);
            lo.set(1);
            context.write(d,lo);
        }
    }

    static class myreduce extends Reducer<dd,LongWritable,Text,Text>
    {
        @Override
        protected void reduce(dd key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
long count=0;
        for(LongWritable lo:values)
        {
            count=lo.get();
            context.write(new Text(String.valueOf(key)),new Text(String.valueOf(count)));
        }
        }
    }

}
