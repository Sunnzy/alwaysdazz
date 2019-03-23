package MapReduce.MROne.MRTwo.MRThree;

import org.apache.hadoop.io.LongWritable;


public class ClickRate extends LongWritable {

    @Override
    public int compareTo(LongWritable o) {
        return -(super.compareTo(o));//降序输出
    }


}
