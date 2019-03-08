package Text;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
/*
1.接收和发送的泛型
2.将key 和 value按业务逻辑切片
3.创建相对应类型的实例 并拿取key和value的值
4.context发送给reduce 类型要对应
 */
                                        //传入的泛型         输出的泛型
public class MobileMapper extends Mapper<LongWritable,Text,MobileWritble,Text> {
    @Override           //处理输入的泛型
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        MobileWritble mo=new MobileWritble();//要输出的key

        Text text=new Text();//要输出的value

        String[] split = value.toString().split("\t");//只需要按table来分割字符串 将每行分割好的单词依次给split

            mo.setJz(split[0]);// split拿到第0个单词

            mo.setJz(split[1]);//拿到第1个单词

            text.set(split[8]+","+split[9]);//拿到第8和第9个单词
            context.write(mo,text);//发送给reduce处理????发送的是 key（基站 手机号） value（上行流量 下行流浪）
    }
}
