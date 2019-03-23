package MyMapReduce.Wordcount;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LL implements Writable {

private long upload;
private long download;

    public LL( ){}
    public LL(long upload, long download) {
        this.upload = upload;
        this.download = download;
    }

    public void write(DataOutput out) throws IOException {

        out.writeLong(upload);
        out.writeLong(download);

    }

    public void readFields(DataInput in) throws IOException {

            this.upload=in.readLong();
            this.download=in.readLong();

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

    @Override
    public String toString() {
        return  upload +""+ download;
    }


}
