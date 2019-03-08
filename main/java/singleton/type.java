package singleton;
//枚举类
     enum type {
//第一个必须写要枚举和其描述信息
         SPRING("chuan");
//描述的属性
         private final String name;
//使用构造方法初始化属性
         type(String name) {
             this.name = name;
         }
//公共的get方法 拿值
         public String getName() {
             return name;
         }
//tosting
         @Override
         public String toString() {
             return "type{" +
                     "nam='" + name + '\'' +
                     '}';
         }
//枚举类默认是私有 直接在在同一个类下调用
         public static void main(String[] args) {

             type t = type.SPRING;
             System.out.println(t);

         }

 }