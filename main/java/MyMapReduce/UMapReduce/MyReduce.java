package MyMapReduce.UMapReduce;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MyReduce extends Reducer<Text,LongWritable,LongWritable,Text> {

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

         long sum=0;

                for(LongWritable ln:values)
                {

                    sum+=ln.get();

                }

                context.write(new LongWritable(sum),key);


    }
}
