package HbaseApi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.datanucleus.store.types.backed.ArrayList;

import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;

import static org.apache.hadoop.hbase.filter.CompareFilter.CompareOp.EQUAL;
import static org.apache.hadoop.hbase.filter.CompareFilter.CompareOp.LESS;

public class FilterDemo {
//https://cloud.tencent.com/developer/article/1336651

    private static void scan(Filter filter,Table table) throws IOException {

        Scan scan=new Scan();//new 一个对象
        scan.setFilter(filter);//设置过滤器
        ResultScanner scanner = table.getScanner(scan);//返回table的执行结果

        int rowcount=0;
        for(Result result:scanner)//遍历结果
        {
            CellScanner cellScanner = result.cellScanner();//拿到cell
            while (cellScanner.advance())
            {
                Cell current = cellScanner.current();//拿到当前的cell
                byte[] rowArray = current.getRowArray();//拿到rowkey
                byte[] familyArray = current.getFamilyArray();//拿到列簇
                byte[] qualifierArray = current.getQualifierArray();//拿到当前的列
                byte[] valueArray = current.getValueArray();//拿到值
                String s2 = Bytes.toString(CellUtil.cloneRow(current));
                String s = Bytes.toString(CellUtil.cloneFamily(current));//拿到列簇的字符串
                String s1 = Bytes.toString(CellUtil.cloneQualifier(current));//拿到列的字符串
                String s3 = Bytes.toString(CellUtil.cloneValue(current));//拿到value的值
                System.out.println(s2+"-->"+s+"-->"+s1+"-->"+s3);
               }
            rowcount++;
        }

        System.out.println(rowcount);
        scanner.close();
    }

    public static void main(String[] args) throws IOException {


        Configuration conf = HBaseConfiguration.create();//拿到hbase的配置实例
        conf.set("hbase.zookeeper.quorum", "192.168.1.131,192.168.1.132,192.168.1.133");//指定zookeepr连接的所在地
        conf.set("hbase.zookeeper.property.clientPort", "2181");//指定zk的端口号，默认就是2181,如果你将zk的默认端口号改了，那么就需要重新指定为你改的那个端口好

        conf.set("zookeeper.znode.parent", "/hbase-unsecure");//指定Hbase在zk中的父目录，非常重要，如果不是/hbase则显示的指定出来

        //加载配置,获取到hbase的连接
        Connection connection = ConnectionFactory.createConnection(conf);
        Table bg06 = connection.getTable(TableName.valueOf("t1"));
//====keyOnlyFilter
/*      //这个过滤器唯一的功能就是只返回每行的行键，值全部为空，这对于只关注于行键的应用场景来说非常合适，这样忽略掉其值就可以减少传递到客户端的数据量，能起到一定的优化作用
        KeyOnlyFilter keyOnlyFilter = new KeyOnlyFilter();//该过滤器没有任何参数，作用很单一，将所有的cell值变成空。经常用于Row个数统计
        scan(keyOnlyFilter,bg06);
*/
//====FirstKeyOnlyFilter
/*      //如果你只想返回的结果集中只包含第一列的数据，那么这个过滤器能够满足你的要求。它在找到每行的第一列之后会停止扫描，从而使扫描的性能也得到了一定的提升
        FirstKeyOnlyFilter firstKeyOnlyFilter = new FirstKeyOnlyFilter();//该过滤器没有任何参数，作用很单一，只获取到每个rowkey的第一个cell即当俩个rowkey相同时则只显示第一个cell区间。经常用于Row个数统计
        scan(firstKeyOnlyFilter,bg06);
*/
//====TimestampsFilter
/*
        TimestampsFilter timestampsFilter = new TimestampsFilter(Arrays.asList(1551442681243l));//时间过滤器,后面必须有l 表示查找此时间戳的数据
        scan(timestampsFilter,bg06);
*/
//===PrefixFilter//前缀过滤器
//        PrefixFilter prefixFilter = new PrefixFilter("r".getBytes());
//        scan(prefixFilter,bg06);

        //====3、rowFilter  这是rowkey的比较 它就需要参数，一个是比较操作符，一个是比较器。
      /**
         * 比较操作符就是我们常说的>,<,<=,>=,!=,== 等等，对应的类是
         * CompareFilter.CompareOp  里面有N多个枚举。
         *    LESS,LESS_OR_EQUAL,EQUAL,NOT_EQUAL,GREATER_OR_EQUAL,GREATER,NO_OP
         *
         *  还有比较器：BinaryComparator、BinaryPrefixComparator、LongComparator、RegexStringComparator
         *  SubstringComparator.
         *
         **/
//      //==BinaryComparator二进制比较过滤器 0 01 03 2
//        BinaryComparator binaryComparator=new BinaryComparator("1".getBytes());
//        RowFilter rowFilter = new RowFilter(EQUAL,binaryComparator);//按二进制比较，采用 Bytes.compareTo(byte[])
//        scan(rowFilter,bg06);
////        003-->info3-->name-->ooooo
////        1


        //== 单值过滤器（SingleColumnValueFilter）筛选值小于v3的字段
//
//        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter("a1".getBytes(),"c2".getBytes(), LESS,"v3".getBytes());
//        scan(singleColumnValueFilter,bg06);
//
//        r2-->a1-->c2-->v2
//        r2-->cf2-->-->v2
//        r2-->cf2-->c1-->v1
//        1

        //==BinaryPrefixComparator二进制前缀顾虑器
//        BinaryPrefixComparator binaryPrefixComparator = new BinaryPrefixComparator("h".getBytes());//二进制前缀比较器 例如 rui和ruiii 的前缀都是rui
//        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.LESS, binaryPrefixComparator);
//        scan(rowFilter,bg06);
////        003-->info3-->name-->ooooo
////        127-->info-->age-->333
////        127-->info-->name-->333
////        2

//        //===longcomparator长度过滤器（long类型）
//        LongComparator longComparator = new LongComparator(5);//长度为5
//        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.LESS, longComparator);//小于5的rowkey
//        scan(rowFilter,bg06);
////        //Exception in thread "main" java.lang.RuntimeException: org.apache.hadoop.hbase.DoNotRetryIOException: Failed after retry of OutOfOrderScannerNextException: was there a rpc timeout?

        //==RegexStringComparator正则过滤器
//        RegexStringComparator name = new RegexStringComparator("^h");//提供一个正则的比较器，仅支持 EQUAL 和 NOT_EQUAL 运算符
//        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, name);//查找以h为开头的rowkey
//        scan(rowFilter,bg06);
//        //hello-->info-->hhh-->this is methis is methis is me
//        //1

//        ===QualifierFilter列过滤器
//        BinaryPrefixComparator binaryPrefixComparator = new BinaryPrefixComparator("name".getBytes());//列过滤器
//        QualifierFilter qualifierFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL,binaryPrefixComparator);//查找列名为name的rowkey
//        scan(qualifierFilter,bg06);
////        003-->info3-->name-->ooooo
////        127-->info-->name-->333
////        2



        //===SubstringComparator 包含过滤器
//        SubstringComparator substringComparator = new SubstringComparator("127");//包含127的过滤器
//        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, substringComparator);//查看包含127的rowkey
//        scan(rowFilter,bg06);
////        127-->info-->age-->333
////        127-->info-->name-->333
////        1



//        //====NullComparator判断给定value的是否为空
//        NullComparator nullComparator = new NullComparator();
//        ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.valueOf("a"),nullComparator);
//        scan(valueFilter,bg06);

            //===分页过滤器
      //  PageFilter pageFilter = new PageFilter(3);//设置每页显示2页
    //    scan(rowFilter,bg06);
        //====PrefixFilter 前缀过滤器
//        PrefixFilter ss = new PrefixFilter(Bytes.toBytes("ss"));
//        MultipleColumnPrefixFilter multipleColumnPrefixFilter = new MultipleColumnPrefixFilter(Bytes.toBytes(ss));


    }

}
