package MapReduce.MROne.MRTwo;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ZuoyeLei implements WritableComparable<ZuoyeLei> {
    private String name;


    public int compareTo(ZuoyeLei o) {
        if(name.equals(o.name))
        {
            return this.name.compareTo(o.name);
        }
        else
        {
            return this.name.compareTo(o.name);
        }
    }

    public void write(DataOutput out) throws IOException {

        out.writeUTF(name);

    }

    public void readFields(DataInput in) throws IOException {

        this.name=in.readUTF();
    }

    public ZuoyeLei(){}
    public ZuoyeLei(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
