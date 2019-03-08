package HbaseApi;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;

import java.io.IOException;
import java.util.List;

/**
 * 需求是，往yg表里放数据时，将员工的pid加入到zg表对应的rowkey上，比如：
 * yg表：
 * rowkey：0001     cf:zg->zg001
 * 当我们插入这样一条数据时，需要将0001这条数据插入到zg表中：
 * zg表:
 * rowkey:zg001     cf:0001:N
 */
public class RegionObServerDemo extends BaseRegionObserver {

        private final byte[]CF="cf".getBytes();//定义列簇
        private final byte[] Qualifier="qualifier".getBytes();//列值

        private byte[]zgrow;
        private byte[]ygrow;


    @Override                         //RegionCoprocessorEnvironment环境类:可以通过此拿到连接或者表
    public void prePut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {

        if(put.has(CF,Qualifier))//如果有列簇和列限定符
        {
            List<Cell> cells = put.get(CF, Qualifier);//则获取这个cell,获取了cell就可以获取很多的值
            if(cells.size()==1)//保证只有一条符合该列簇和列限定符的数据,如果有俩个列限定符则代表有俩条数据是错误的
            {               // cf:1 q:2 v:3 size=1
                            // cf:1 q:2 v:4 size=2
                Cell cell = cells.get(0);
                System.out.println(cell);
                byte[] bytes = CellUtil.cloneValue(cell);//将value的值(即zg001)克隆给bytes
                zgrow=bytes;//在这里拿到了zg的rowkey
            }

            //e.getEnvironment(获取环境
            HTableInterface zg = e.getEnvironment().getTable(TableName.valueOf("zg"));//拿到zg表的控制权
            Put put1 =new Put(zgrow);//将rowkey值放入进去
            put1.addColumn(CF,ygrow,"N".getBytes());//这里是往zg表插入列簇，列和值
            zg.put(put1);//将值放进去
        }
    }
}
