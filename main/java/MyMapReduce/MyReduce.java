package MyMapReduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class MyReduce extends Reducer <Text,LongWritable,LongWritable,Text>{

    protected static int sum;

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

        sum=0;

//        Iterator<LongWritable> iterator = values.iterator();
//        while(iterator.hasNext())
//        {
//
//            iterator+=iterator.next().get();
//
//        }

        for(LongWritable it: values)
        {
             sum+=it.get();

            context.write(new LongWritable(sum),key);

        }
    }
}
