package MyMapReduce.test;

import org.apache.hadoop.io.LongWritable;

public class MyLong extends LongWritable {

    @Override//这默认是正序输出
    public int compareTo(LongWritable o) {
        return -(super.compareTo(o));//这里按的是倒叙排序 直接添加一个-号,后期直接使用这个实例
    }               //切记 切记 要括号起来
}
