package Demo

import scala.swing.Color

object MyextendsTrait extends App {

  //占位符
  val name="张三"
  val id =9857
  println(s"$name+$id")


  //apply方法
  //通常来说我们会在伴生对象里面定义apply方法
  //遇到类名(参数1...参数n)的时候apply方法会被调用
  //在object类型的类里面 我就可以使用apply方法
  //只要你通过类名后面跟上参数就会调用apply方法
  //apply方法主要是用于初始化

  def apply(color:String): Unit ={

    println(s"电脑的颜色:$color")

  }


  def apply(color:String,brand:String): Unit ={

    println(s"color:$color+brand:$brand")

  }


  val computer=MyextendsTrait.apply("深紫色")//直接类名.apply调用  也可以省略apply
  println(computer)
  val computer1=MyextendsTrait("海蓝色","Dell")
  println(computer1)

}


//===============抽象类 abstract

abstract class absText{

  def eat//无实例的抽象方法,如要继承此类需要重写此方法
  def run(): Unit ={
    //抽象方法可以写实例,有实例的抽象方法,在继承类的时候不要求重写可以直接使用,也可以重写
    println("i can fly！")

  }
}

class Monkey extends absText{
  override def eat: Unit = {//重写无实例的eat方法
    println("this is monkey")

  }

def MonkeyM1: Unit ={//声明一个方法 将所有的方法写到里面 后面直接调用这个方法就ok
  eat
  run()
}

}

//=================特征类(即java的接口类)trait
//定义特征和java中的接口不一样,特征可以写实例,有实例的特征可以直接使用,也可以重写,在scala中可以使用extends去继承特征类,但是如果继承接口下一次有一个类使用的话就不能使用了
//一般来说 都是实现特征(接口)继承抽象 继承用with关键字
//一般来说 extends也可以
trait bird{

  def fly()//抽象的方法
  def name(): Unit ={

    println("this name is little bird")
  }
}

class maque extends absText with bird{
  override def fly(): Unit = {

    println("u can fly")
    name()
  }
  def m2(): Unit ={
      fly()
      name()

  }

  override def eat: Unit = ???
}
//======================main方法
object Textsix extends App{

  val mo=new Monkey
  mo.eat
  mo.MonkeyM1

  val fy=new maque
  fy.fly()
  fy.m2()


}












