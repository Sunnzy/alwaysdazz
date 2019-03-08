package abstract_factory_pattern;
//继承abstract_factory的成员属性，基于给定的信息生成具体的类
public class shapefactory extends abstract_factory{
    public color getcolor(String color) {
        return null;
    }

    public shape getshape(String shape) {

        if(shape==null)
        {
            return null;
        }

        if(shape.equalsIgnoreCase("square"))
        {
            return new square();
        }

        if(shape.equalsIgnoreCase("rectangle"))
        {
            return new rectangle();
        }
        if(shape.equalsIgnoreCase("circle"))
        {
            return new circle();
        }
        return null;
    }
}
