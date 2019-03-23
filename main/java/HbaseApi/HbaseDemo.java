package HbaseApi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.Batch;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.hadoop.hbase.TableName.valueOf;

//由于hbase不同于mysql添加driver 所以要在lib库里找配置
public class HbaseDemo {

    //连接hbase增删改查
//在公司的环境中将配置类放在一起，用静态代码块修饰，随着类加载而加载，private修饰最佳，可以直接一起修改参数很方便，将变动的参数提取出来放到一个类中，
    public static void main(String[] args) throws IOException, InterruptedException {

        Configuration conf = HBaseConfiguration.create();//拿到hbase的配置实例
        conf.set("hbase.zookeeper.quorum", "192.168.1.137,192.168.1.138,192.168.1.139");//指定zookeepr连接的所在地
        conf.set("hbase.zookeeper.property.clientPort", "2181");//指定zk的端口号，默认就是2181,如果你将zk的默认端口号改了，那么就需要重新指定为你改的那个端口好

        conf.set("zookeeper.znode.parent", "/hbase-unsecure");//指定Hbase在zk中的父目录，非常重要，如果不是/hbase则显示的指定出来

        //加载配置,获取到hbase的连接
        Connection connection = ConnectionFactory.createConnection(conf);


//=============================================================

        Table table1 = connection.getTable(valueOf("student"));//拿到表的实例

/*      //====================基于期望值来完成操作，主要对应场景为安全性的删除。
        Delete delete=new Delete("125".getBytes());//rowkey
        delete.addColumn("info".getBytes(),"zxc".getBytes());//列簇，列
            //如果table1的rowkey，列簇，列，列值都正确 则删除上面rowkey为125的值 否则不能删除
        boolean b = table1.checkAndDelete("125".getBytes(), "info".getBytes(),"zxc".getBytes(),"zqa".getBytes(),delete);//rowkey,列簇，列名，列值 如果列值与苦衷的值相同则删除此条字段，并返回true否则false
        System.out.println(b);

        Put put=new Put("19".getBytes());//rowkey
        put.addColumn("info".getBytes(),"zzz".getBytes(),"ojbk".getBytes());//列簇 列 列值
        boolean b1 = table1.checkAndPut("127".getBytes(), "info".getBytes(),"age".getBytes(),"333".getBytes(),put);//这条数据必须正确才能实现上面的put操作
        System.out.println(b1);
*/


/*
      //==========batch批量操作可以同时增删改查
        List<Row>rows=new ArrayList<>();

        Get get=new Get("125".getBytes());//我要拿到125的rowkey
        get.addColumn("info".getBytes(),"zxc".getBytes());//

        Delete delete=new Delete("126".getBytes());
        delete.addColumn("info".getBytes(),"ssss".getBytes());//列簇，列限定符

        Delete delete1=new Delete("001".getBytes());//直接删除此条rowkey

        Put put=new Put("127".getBytes());
        put.addColumn("info".getBytes(),"age".getBytes(),"333".getBytes());//列簇，列限定符，列值
        rows.add(delete1);
        rows.add(get);
        rows.add(delete);
        rows.add(put);

        final Object[] result = new Object[rows.size()];//需要返回一个 结果集合
//
//        table1.batch(rows,result);//fuck 这里需要list类型的rows，还有object类型个的一个集合


//          <R> void batchCallback(
//final List<? extends Row> actions, final Object[] results, final Batch.Callback<R> callback

        //=============callback 回调函数:一般应用于游戏验证码;;
        Batch.Callback callback = new Batch.Callback() {
            @Override
            public void update(byte[] region, byte[] row, Object result) {

                System.out.println(region.toString(region));
                System.out.println(row.toString(row));
                System.out.println(result.toString(result));
            }
        };
        table1.batchCallback(rows,result,callback);

*/
/*

        //==========================put操作同delete scan操作一样

        Put put=new Put("001".getBytes());//rowkey
        put.addColumn("info".getBytes(),"name1".getBytes(),"wanghaha".getBytes());//列簇，列限定符，值
        put.addColumn("info".getBytes(),"name2".getBytes(),"wangqaqa".getBytes());//列簇，列限定符，值
        table1.put(put);

        Scan scan = new Scan();//拿到实例
        ResultScanner scanner = table1.getScanner(scan);
        for(Result result:scanner)//遍历一下放入的值 确保放进去了
        {
            System.out.println(result);
        }
*/

        //==================添加内容,需要 指定rowkey,列簇,类限定符,列值,append比put操作更安全 代码操作一样
/*
        Table table = connection.getTable(TableName.valueOf("first".getBytes()));//首先获取表名
        Append append = new Append("01".getBytes());//这TTTTTMMMMM是rowkey！！！
        append.add("info5".getBytes(),"name".getBytes(),"hahahah".getBytes());
        Result append1 = table.append(append);//追加等同于put相对来说更安全 有锁限制着
        while(append1.advance())
        {
            System.out.println(append1.current());
        }
  */

/*      //批量get，put，delete都需要封装到arraylist数组,只能单独的增 或者 删 或者 查  不能同时增删改查
        List<Put> put=new ArrayList<Put>();//批量put，delete，get都需要用list数组
        //需要拿到列簇，列，值
        Put put1=new Put("125".getBytes());//指定rowkey
        put1.addColumn("info".getBytes(),"zxc".getBytes(),"zqa".getBytes());//列簇，列限定符，列值
        Put put2=new Put("126".getBytes());
        put2.addColumn("info".getBytes(),"ssss".getBytes(),"4s".getBytes());
        put.add(put1);//封装到数组中
        put.add(put2);//同上
        table1.put(put);//需要参数put

*/


//==============================admin操作=======================

        Admin admin = connection.getAdmin();//通过连接获取执行者（管理者）实例,可以对表进行增删改查 不可以对里面的内容增删改查


        /*
        //===========================基于startkey，endkey来完成自动分区
        TableName tablename = TableName.valueOf("table2".getBytes());//先创建一个表的名字
        HTableDescriptor hTableDescriptor = new HTableDescriptor(tablename);//这里需要tablename的实例
        HColumnDescriptor info = new HColumnDescriptor("info");//这里需要列簇名
        hTableDescriptor.addFamily(info);//表名加载列簇

        admin.createTable(hTableDescriptor, "Hello".getBytes(), "world".getBytes(), 5);//基于startkey和endkey来完成自动分区,这里的startkey是hello，endkey是world


        //基于手动操作完成分区

        TableName table2 = TableName.valueOf("table3".getBytes());//先指定我们要创建的表名
        HTableDescriptor hTableDescriptor1 = new HTableDescriptor(table2);//这里我们拿到了表的名称但是还需要表的列簇指定
        HColumnDescriptor info1 = new HColumnDescriptor("info");//这里创建了列簇名称 我下一步就需要将俩个联合在一起
        hTableDescriptor1.addFamily(info1);

        String[] strs = new String[]{"1", "2", "3"};

        byte[][] bytes = new byte[][]{"A".getBytes(),"B".getBytes(),  "C".getBytes()};//和String数组一样只不过是这里需要用byte数组类分区,相当于无穷-A区，A-B区，B-C区,C-无穷区

        admin.createTable(hTableDescriptor1,bytes);//进入admin方法我得知需要 HTableDescriptor实例，HTableDescriptor实例需要表的实例和列簇的实例
*/
        //=====显示表的命名空间
      /*  NamespaceDescriptor[] ns = admin.listNamespaceDescriptors();//显示表的命名空间
        for(NamespaceDescriptor na:ns)
        {
            System.out.println(na);
        }
        //======遍历表的名字
        TableName[] tableNames = admin.listTableNames();//遍历表的名字
        for(TableName ta:tableNames)
        {
            System.out.println(ta);
        }
        //======遍历表的属性
        HTableDescriptor[] hTableDescriptors = admin.listTables();//遍历第一表的属性
        for(HTableDescriptor h:hTableDescriptors)
        {
            System.out.println(h);
        }
*/
       //这里的tablename是静态的 需要一个实例来调用
//==================创建first表名，列簇info5
    /*    TableName tableName = TableName.valueOf("first");//创建的表名

        HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);//这里的参数需要tablename的类型

        HColumnDescriptor hColumnDescriptor = new HColumnDescriptor("info5");//指定列簇
        HTableDescriptor hTableDescriptor1 = hTableDescriptor.addFamily(hColumnDescriptor);//表的属性
        admin.createTable(hTableDescriptor1);//添加表操作，需要表的详情实例
        */

    }

}
