package abstract_factory_pattern;

public class factoryporducer {
//④创建工厂的实例对象
    public static abstract_factory getfactory(String choice){

        if(choice.equalsIgnoreCase("shape"))
        {
            return new shapefactory();
        }
        else if(choice.equalsIgnoreCase("color"))
        {
            return new colorfactory();
        }

        return null;

    }

}
