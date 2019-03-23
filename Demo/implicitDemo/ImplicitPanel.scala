package Demo.implicitDemo

import Demo.Person

//①因为俩个参数 所以要发送俩个参数
//②因为是隐式类,所以需要写明类和类的类型,定义隐式的类值

object ImplicitPanel {

  implicit val name:String="发斯蒂芬"//②
  implicit val age:Int=99//②

  implicit val P:Person=new Person("张三丰",99)//③

  implicit def StringToInt(x:String)=x.toInt//④

  implicit def fun(p:Human)=new ImplicitText5//new 一个类

}

class Person (val mame:String,val age:Int)//③