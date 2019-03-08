package abstract_factory_pattern;
//③继承abstract_factory的成员属性，基于给定的信息生成具体的类
public class colorfactory extends abstract_factory {

    public color getcolor(String color) {
        if(color==null)
        { return null;}

        if(color.equalsIgnoreCase("blue"))
        {
            return new blue();
        }

        if(color.equalsIgnoreCase("Red"))
        {
            return new red();
        }

        if(color.equalsIgnoreCase("yellow"))
        {
            return new yellow();
        }
        return null;

    }
    public shape getshape(String shape) {
        return null;
    }


}
