package Text;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/*
1.接收来自mapper的值 类型要一致
2.遍历接收来的值 将值按业务逻辑切割 并遍历用数组对应拿去 并将相同的值累加
3.context发送

 */

public class MobileReduce extends Reducer<MobileWritble,Text,MobileWritble,Text> {


    @Override           //  基站和手机号        与基站手机号匹配的数组
    protected void reduce(MobileWritble key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        long up=0;//上行流量
        long down=0;//下行流量

        for(Text t:values)//遍历数组 将上行流量与下行流量一块迭代
        {

            String[] split = t.toString().split(",");//将上行流量与下行流量分割
            up+=Long.parseLong(split[0]);//拿到上行流量
            down+=Long.parseLong(split[1]);//拿到下行流量

            context.write(key,new Text(up+","+down+","+(up+down)));
                    //key（基站手机号）  value 上行     下行        总和
        }

    }
}
