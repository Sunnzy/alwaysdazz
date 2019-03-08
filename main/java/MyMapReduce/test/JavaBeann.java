package MyMapReduce.test;

import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
//自定义数据类型,这是需要传输的类
public class JavaBeann implements Writable {
//基于基站传输，只需要定义value的值 因为后期要聚合
   // private String base;

    private long upload;

    private long download;

    public JavaBeann() {

    }

    public JavaBeann(long upload, long download) {
        this.upload = upload;
        this.download = download;
    }

    public long getUpload() {
        return upload;
    }

    public void setUpload(long upload) {
        this.upload = upload;
    }

    public long getDownload() {
        return download;
    }

    public void setDownload(long download) {
        this.download = download;
    }

    //写入磁盘需要反序列化
    public void write(DataOutput out) throws IOException {

        out.writeLong(upload);
        out.writeLong(download);

    }

    public void readFields(DataInput in) throws IOException {

        this.upload=in.readLong();
        this.download=in.readLong();

    }

    @Override
    public String toString() {
        return upload+","+download;
    }
}
