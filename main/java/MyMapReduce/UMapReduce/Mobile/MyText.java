package MyMapReduce.UMapReduce.Mobile;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

                //实现序列化
public class MyText implements Writable {

    private String phone;
    private long upload;
    private long download;
    private long sumload;


    //反序列化
    public void write(DataOutput out) throws IOException {
    out.writeUTF(phone);
    out.writeLong(upload);
    out.writeLong(download);
    out.writeLong(sumload);

    }
    //序列化
    public void readFields(DataInput in) throws IOException {

        this.phone = in.readUTF();
        this.upload=in.readLong();
        this.download=in.readLong();
        this.sumload=in.readLong();
    }

    //构造


    public String getPhone() {
        return phone;
    }

    public long getUpload() {
        return upload;
    }

    public long getDownload() {
        return download;
    }

    public long getSumload() {
        return sumload;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUpload(long upload) {
        this.upload = upload;
    }

    public void setDownload(long download) {
        this.download = download;
    }

    public void setSumload(long sumload) {
        this.sumload = sumload;
    }


//无参构造
    public MyText()
    {

    }


    //有参构造
    public MyText(String phone, long upload, long download) {
        this.phone = phone;
        this.upload = upload;
        this.download = download;
        this.sumload = upload+download;
    }

        //tostring
        @Override
        public String toString()
        {
            return phone+","+upload+","+download+","+sumload;
        }


}
