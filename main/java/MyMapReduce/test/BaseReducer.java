package MyMapReduce.test;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

                                                //这里为什么是text发送
public class BaseReducer extends Reducer<Text,JavaBeann,Text,Text> {
    @Override
    protected void reduce(Text key, Iterable<JavaBeann> values, Context context) throws IOException, InterruptedException {
     /*
     这里只需要遍历拿出javabean，

*/          //算数求和
        long upload=0;
        long download=0;
        long sumload=0;//在这里聚合总流量

        for(JavaBeann jv:values)
        {
            upload+=jv.getUpload();
            download+=jv.getDownload();
        }
            sumload=upload+download;//相对应的上行流量和下行流量

        context.write(key,new Text(upload+","+download+","+sumload));

    }
}
