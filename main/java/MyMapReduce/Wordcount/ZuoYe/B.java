package MyMapReduce.Wordcount.ZuoYe;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class B implements WritableComparable<B> {

    private String ganner;
    private String grade;


    public B(String ganner, String grade) {
        this.ganner = ganner;
        this.grade = grade;
    }

    public B() {
    }

    public int compareTo(B o) {
        if(this.ganner.equals(o.ganner))
        {
            if(this.grade.equals(o.grade))
            {
                return 0;
            }else
            {
                return this.grade.compareTo(grade);
            }
        }else {
            return this.ganner.compareTo(ganner);
        }
    }

    public void write(DataOutput out) throws IOException {
                out.writeUTF(ganner);
                out.writeUTF(grade);
    }

    public void readFields(DataInput in) throws IOException {

                this.ganner=in.readUTF();
                this.grade=in.readUTF();
    }

    public String getGanner() {
        return ganner;
    }

    public void setGanner(String ganner) {
        this.ganner = ganner;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return  ganner +","+ grade;
    }

}
