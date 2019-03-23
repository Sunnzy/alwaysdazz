package MapReduce.MROne.MRTwo.MRThree.MRFour.UID.MRTen;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Ten implements Writable {

    private long id;
    private long chick;


    public Ten(){}

    public Ten(long id, long chick) {
        this.id = id;
        this.chick = chick;
    }

    public void write(DataOutput out) throws IOException {

        out.writeLong(id);
        out.writeLong(chick);

    }

    public void readFields(DataInput in) throws IOException {

        this.id=in.readLong();
        this.chick=in.readLong();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChick() {
        return chick;
    }

    public void setChick(long chick) {
        this.chick = chick;
    }

    @Override
    public String toString() {
        return id+" "+chick;
    }
}
