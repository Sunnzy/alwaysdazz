package MyMapReduce.Wordcount.ZuoYe;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Partitioner;
import java.util.HashMap;
import java.util.Map;

public class MyPartition extends Partitioner<JavaBeann, LongWritable> {
//1、根据性别、会员级别统计出乘客数量占比
//    男6-0，女6-1，男5-2，女5-3,其他-4
//    格式如下：性别、级别、数量、占比
//	（使用分区)`
/*
1.先求出数量
2.在加入分区
 */
    static Map<String,Integer> map;
    static
    {
        map=new HashMap<String, Integer>();
        map.put("男6",0);
        map.put("女6",1);
        map.put("男5",2);
        map.put("女5",3);
    }
//reduce数量为5
    @Override
    public int getPartition(JavaBeann javann, LongWritable longWritable, int i) {
        String trim = javann.toString();//截取下角标为1的
        if(map.containsKey(trim))
        {
            return map.get(trim);
        }
        else
        {
            return 4;
        }
    }
}
