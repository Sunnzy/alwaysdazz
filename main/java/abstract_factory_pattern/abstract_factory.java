package abstract_factory_pattern;
//②通过color shape对象的创建来获取工厂信息
public abstract class abstract_factory {
    public abstract color getcolor(String color);
    public abstract shape getshape(String shape);
}
