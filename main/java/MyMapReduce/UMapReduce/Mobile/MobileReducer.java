package MyMapReduce.UMapReduce.Mobile;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MobileReducer extends Reducer<Text,MyText,Text,MyText> {

    /*
    首先 将键放在一侧不动 遍历值 然后一一对应
    将值遍历出来 手机号 上行 下行 总和
     */
    @Override
    protected void reduce(Text key, Iterable<MyText> values, Context context) throws IOException, InterruptedException {

        Map<String,List<Long>> m=new HashMap<String,List<Long>>();

        for(MyText my:values)

        {
            String phone=my.getPhone();
            if( m.containsKey(phone))//如果有与之匹配的手机号
            {
               List<Long> longs= m.get(phone);

               Long oldup=longs.get(0);
                Long oldDown = longs.get(1);
                Long oldSum = longs.get(2);

                longs.set(0,oldup+my.getUpload());//这里为什么有重新赋值了
                longs.set(1,oldDown+my.getDownload());
                longs.set(2,oldSum+my.getSumload());
                m.put(phone,longs);
            }
            else
            {
                ArrayList<Long>longs=new ArrayList<Long>();

                longs.add(my.getUpload());

                longs.add(my.getDownload());

                longs.add(my.getSumload());

                m.put(phone,longs);

            }

            //for循环遍历输出

            for(String k:m.keySet())

            {

                List<Long> longs = m.get(k);
                context.write(key,new MyText(k,longs.get(0),longs.get(1)));

            }
        }
    }
}
