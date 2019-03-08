package MapReduce.MROne.MRTwo.MRThree.MRFour.UID.MRTen.SevenUrlClick;


import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Seven implements Writable {

    private long name;
    private long uri;

    public Seven(){}
    public Seven(long name, long uri) {
        this.name = name;
        this.uri = uri;
    }


    public long getName() {
        return name;
    }

    public void setName(long name) {
        this.name = name;
    }

    public long getUri() {
        return uri;
    }

    public void setUri(long uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return  name+","+uri;
    }




    public void write(DataOutput out) throws IOException {
        out.writeLong(name);
        out.writeLong(uri);

    }

    public void readFields(DataInput in) throws IOException {

        this.name=in.readLong();
        this.uri=in.readLong();
    }
}
