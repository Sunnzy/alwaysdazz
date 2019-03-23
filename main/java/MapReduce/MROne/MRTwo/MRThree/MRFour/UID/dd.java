package MapReduce.MROne.MRTwo.MRThree.MRFour.UID;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class dd implements WritableComparable<dd> {

    private String id;

    public int compareTo(dd o) {
        if(this.id.equals(o.id))
        {
            return this.id.compareTo(o.id);
        }
        else
        {
            return this.id.compareTo(o.id);
        }
    }

    public void write(DataOutput out) throws IOException {

        out.writeUTF(id);

    }

    public void readFields(DataInput in) throws IOException {

        this.id = in.readUTF();
    }

    public dd(){}
    public dd(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return id;
    }
}
