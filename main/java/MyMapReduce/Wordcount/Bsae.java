package MyMapReduce.Wordcount;

public class Bsae implements Comparable<Bsae> {

    private String base;
    private String phone;


    public Bsae(){}

    public Bsae(String base, String phone) {
        this.base = base;
        this.phone = phone;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int compareTo(Bsae o) {
        if(base.equals(o.base))
        {
            if(phone.equals(o.phone))
            {
                return 0;
            }
            else
            {
                return phone.compareTo(o.phone);
            }

        }
        else
        {
            return base.compareTo(o.base);
        }

    }

    @Override
    public String toString() {
        return base+phone;
    }
}
