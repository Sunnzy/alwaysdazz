package MyMapReduce.DBwrite;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
                            //实现序列化和连接数据库
public class text implements Writable, DBWritable {

    private String name;
    private int age;


    public void write(DataOutput out) throws IOException {
            //反序列化
        out.writeUTF(name);
        out.write(age);
    }

    public void readFields(DataInput in) throws IOException {
            //序列化
        this.name=in.readUTF();
        this.age=in.readInt();
    }

    public void write(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1,name);
        preparedStatement.setInt(2,age);

    }

    public void readFields(ResultSet resultSet) throws SQLException {

        this.name=resultSet.getString(1);
        this.age= resultSet.getInt(2);

    }
    public text(){}

    public text(String name, int age) {
        this.name = name;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return  name +","+ age;
    }
}
