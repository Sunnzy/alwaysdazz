package MyMapReduce.Wordcount.ZuoYe;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class JavaBeann implements WritableComparable<JavaBeann> {

    private String sex;
    private String gande;


    public JavaBeann(){}

    public JavaBeann(String sex, String gande) {
        this.sex = sex;
        this.gande = gande;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGande() {
        return gande;
    }

    public void setGande(String gande) {
        this.gande = gande;
    }



    @Override
    public String toString() {
        return sex+""+gande;
    }

    public void write(DataOutput out) throws IOException {
                out.writeUTF(sex);
                out.writeUTF(gande);

    }

    public void readFields(DataInput in) throws IOException {

            this.sex=in.readUTF();
            this.gande=in.readUTF();
    }

    public int compareTo(JavaBeann o) {

        if(sex.equals(o.sex))
        {
            if(gande.equals(o.gande))
            {
                return 0;
            }
            else
            {
                return gande.compareTo(o.gande);
            }
        }
        else
        {
            return sex.compareTo(o.sex);
        }

    }
}
