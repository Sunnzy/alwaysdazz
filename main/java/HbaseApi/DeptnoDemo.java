package HbaseApi;

import net.sf.json.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeptnoDemo {

    public static void main(String[] args) throws IOException {

            Configuration conf = HBaseConfiguration.create();//拿到hbase的配置实例
            conf.set("hbase.zookeeper.quorum", "192.168.1.137,192.168.1.138,192.168.1.139");//指定zookeepr连接的所在地
            conf.set("hbase.zookeeper.property.clientPort", "2181");//指定zk的端口号，默认就是2181,如果你将zk的默认端口号改了，那么就需要重新指定为你改的那个端口好

            conf.set("zookeeper.znode.parent", "/hbase-unsecure");//指定Hbase在zk中的父目录，非常重要，如果不是/hbase则显示的指定出来

            //加载配置,获取到hbase的连接
            Connection connection = ConnectionFactory.createConnection(conf);

        //依照原来的接口参数，会有前端传过来的参数，参数是一个json，我根据工程得知，该工程使用的Json包在我的pom文件中。
        //比如：传过来的参数如果是:{"pid":"00000000003044"}，那么需要通过该类 获取到pid的值,因为不知道该包如何使用，所以我会在test目录中创建一个该
        //类的实例完成该类的熟悉,JSONDemo

        //分析本来工程中的一个接口，发现他们的参数就是 前段传过来的arg，一个表名一个列簇名。所以 我们现将这些参数实例话


        String  arg = "{\"pid\":\"00000000003044\"}";
        String tbName1 = "smp:t_zcpidtrace_pid_index";  //职场与pid的索引表
        String tbName2 = "smp:xmis_items"; //当天实时积分

        String cf = "ci"; //列簇名  注意 这次变量都是来自于参数，只是我在写逻辑时 自己定义了一遍


        /**
         * 以下为我给造的模拟数据
         * smp:t_zcpidtrace_pid_index：
         * ROW                           COLUMN+CELL
         *  00000000003044               column=ci:PID~44030000000218, timestamp=1551235791347, value=N
         *  00000000003044               column=ci:PID~44030000000222, timestamp=1551235813900, value=N
         * 1 row(s) in 0.0300 seconds
         *
         * smp:xmis_items
         * ROW                           COLUMN+CELL
         *  44030000000218               column=ci:CiIForceOcc24M_Exp_Count, timestamp=1551235931264, value=1
         *  44030000000218               column=ci:CiISurrenderAndCancel_Exp_Count, timestamp=1551236034821, value=0
         *  44030000000218               column=ci:NSalesCiIAll_Exp_Count, timestamp=1551236021665, value=1
         *  44030000000222               column=ci:CiIForceOcc24M_Exp_Count, timestamp=1551235968506, value=0
         *  44030000000222               column=ci:CiISurrenderAndCancel_Exp_Count, timestamp=1551235998924, value=3
         *  44030000000222               column=ci:NSalesCiIAll_Exp_Count, timestamp=1551236013422, value=10
         * 2 row(s) in 0.0410 seconds
         */

        Table table = connection.getTable(TableName.valueOf(tbName1));
        JSONObject jsonObject = JSONObject.fromObject(arg);//用json字符串拿到arg
        String pid = jsonObject.getString("pid");//拿到arg里面的pid的值

        //用一个数组来封装JSON的结果
        List<Get> gets=new ArrayList<>();//用一个arraylist的数组来装

        Get get=new Get(pid.getBytes());//根据参数封装get  注意如果这里是多个pid，需要循环封装并且将这些get都给添加到gets中

        gets.add(get);//封装到数组中了

        Result[] results = table.get(gets);
    List<String> l=new ArrayList<>();
        for(Result re:results)
        {
            CellScanner cellScanner = re.cellScanner();//获取cell
            while (cellScanner.advance())//如果还有下一个cell
            {
                Cell current = cellScanner.current();//拿到当前的值
                byte[] familyArray = current.getFamilyArray();//拿到了一个列簇
                byte[] qualifierArray = current.getQualifierArray();//拿到列值
                String s = Bytes.toString(familyArray);//注意 是ByteSSSSSSSSSSS!!
                String s1 = Bytes.toString(qualifierArray);
                if(s1.equalsIgnoreCase("N"))
                {
                    String[] split = s.split("\\~");//按照～切
                    l.add(split[1]);//拿上第1个
                }
                System.out.println(Bytes.toString(familyArray)+"->"+Bytes.toString(qualifierArray));
            }
            //遍历结果如下：PID~44030000000218->N
            //           PID~44030000000222->N
            //因为我现在运行过之后，知道我可以这样获取到我的列限定符 以及列限定符的值，所以我可以新建一个list来将该主管下的所有员工pid放到list中

        }
        //为了证明我的listPids中是所有的pid，所以我们这里可以输出一下
//        for (String s :listPids){
//            System.out.println(s);
//        }
        ////结果:44030000000218
        //    44030000000222
        //的确是所有员工的pid


        //然后再将arraylist中的内容一一遍历出来 在put到table中

        List<Get> ss=new ArrayList<>();

        for(String s:l)
        {
            Get get1=new Get(s.getBytes());
            ss.add(get1);//这里我们拿到了add
        }

        Table table1 = connection.getTable(TableName.valueOf(tbName2));//拿到表二的内容

        Result[] results1 = table1.get(ss);//通过批量get拿到所有的这些员工的结果

        Map<String, Map<String, Object>> stringMapMap = new HashMap<String, Map<String, Object>>();//必须new出具体的map 如hashmap//用于存放最终的而结果
        //类似于 key = "pid",value = Map<String,Object>用于存放它的积分详情
        //遍历这个结果

        for(Result res:results1)
        {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();//用来存储积分信息
            String s="";//定义rowkey
            long sum=0;//计数器
            while(res.advance())//是循环的一个RowKey所有Cell
            {
                Cell current = res.current();//拿到每个cell
                byte[] rowArray = current.getRowArray();//拿到rowkey
                byte[] familyArray = current.getFamilyArray();//拿到列簇
                byte[] qualifierArray = current.getQualifierArray();//拿到列

                s = Bytes.toString(rowArray);
                String s1 = Bytes.toString(familyArray);
                String s2 = Bytes.toString(qualifierArray);
                System.out.println(s+"-->"+s1+"-->"+s2);//看一看结果对不对    ,结果的确是对的，但是因为我们最终需要返回一个json
                //  //根据工程里的实例，我们知道我们可以将一个Map或者list转换成一个Json，具体可看JSONDemo

                sum += Long.parseLong(s2.trim());  //求和

                stringObjectHashMap.put(s1,s2);  //将积分信息放入jfMap中
            }

            stringObjectHashMap.put("zjf",sum);
            stringMapMap.put(s,stringObjectHashMap);

        }
        //44030000000218-->CiIForceOcc24M_Exp_Count-->1
        //44030000000218-->CiISurrenderAndCancel_Exp_Count-->0
        //44030000000218-->NSalesCiIAll_Exp_Count-->1
        //44030000000222-->CiIForceOcc24M_Exp_Count-->0
        //44030000000222-->CiISurrenderAndCancel_Exp_Count-->3
        //44030000000222-->NSalesCiIAll_Exp_Count-->10

        String s = JSONObject.fromObject(stringMapMap).toString();//在这里输出一下结果
        System.out.println(s);//ojbk

    }

}
