package Text;


import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
                            // 实现writablecomparable的类   泛型指定自己
public class MobileWritble implements WritableComparable<MobileWritble> {

        //定义的key
        private String jz;//成员变量
        private String phone;   //成员变量

    public MobileWritble() {//①实现无参构造
    }

    public MobileWritble(String jz, String phone) {//②实现有参构造
        this.jz = jz;
        this.phone = phone;
    }

    public String getJz() {
        return jz;
    }//setget方法，因为序列化要用到writble来完成序列化和反序列化 所以要设置setget

    public void setJz(String jz) {
        this.jz = jz;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return jz+","+phone;
    }  // tostring

    public void write(DataOutput out) throws IOException { // 必须重写write方法 完成反序列化，因为在网络传输中必须要求序列化

       out.writeUTF(jz);
        out.writeUTF(phone); //序列化与反序列化的顺序必须一致

    }

    public void readFields(DataInput in) throws IOException { //必须重写readdields 完成序列化，在网络传输过程中必须要序列化

            this.jz=in.readUTF();

            this.phone=in.readUTF();
    }


    public int compareTo(MobileWritble mo) //字符串的比较方法 用来排序的 比较俩个相同的数据类型
    {

        if(this.jz.equals(mo.jz))//mo指与其他该类的实例，与this.jz比较，基站对比如果一致则比较手机号
        {
            if(this.phone.equals(mo.phone))//如果手机号相同则返回0 表示数据一致
            {
                return 0;
            }
            else
            {
               return this.phone.compareTo(mo.phone);//此手机号与下一个手机号不同
            }
        }
        else
        {
           return this.jz.compareTo(mo.jz);//基站不同 返回
        }
    }

}
