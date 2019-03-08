package abstract_factory_pattern;

public class factory_demo {
//⑤main方法，用创建工厂的对象的里面的newshape方法,在调用shape下面的new方法调用形状
    public static void main(String[] args) {
        //先调用工厂找到形状的方法
        abstract_factory shape = factoryporducer.getfactory("shape");
        //在调用形状的方法 确认具体形状
        abstract_factory_pattern.shape square = shape.getshape("square");
        //输出square方法
        square.drow();

        abstract_factory color = factoryporducer.getfactory("color");
        abstract_factory_pattern.color red = color.getcolor("red");
        red.fill();


    }
}
