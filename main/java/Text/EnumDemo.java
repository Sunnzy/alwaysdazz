package Text;
public class EnumDemo {
    enum Color {red,green,blue,yello;}
    enum size {big,midde,small;}
    public static void main(String[] args)
    {
        for(Color c:Color.values() )
        {
            System.out.println(c.ordinal());
        }

    }
}
