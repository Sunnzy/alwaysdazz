package Demo

import scala.util.Random

object myMatch {

  def main(args: Array[String]): Unit = {

    //匹配值
    val arr = Array("aaa", 222, 0.20)
    //定义一下arr参数
    val values = arr(Random.nextInt(arr.length)) //返回随机长度的arr数组给values
    values match {

      case 1 => println("aaa")
      case 2 => println(222)
      case _ => println(0.20)

    }

    //匹配类型
    //模式匹配的规则 如果第一个匹配到 其他忽略
    val arr1 = Array("Hello", -2, myMatch)
    val arrs=arr1(Random.nextInt(arr1.length))
    arrs match {

      case x:Int => println("HelloWorld")
      case y:String => println("是个负数")
      case _:Any=> myMatch

    }

    //位置匹配
    val tup = (6, 5.5, "happy")
    tup match {

      case (_, 5, "") => println("吼吼吼")
      case (_, z, w) => println("this is very different")
      case (x, y, z) => println(s"$x,$y,$z")
      case (_, x, _) => println("这个也能匹配的上")


    }
    //位置匹配
    val arr2 = Array(0, 1, 5, 7)
    arr2 match {
      case Array(1, x, y) => println("aaa")
      case Array(1, 1, 5) => println("bbb")
      case Array(0, _*) => println("ccc")
      case _ => println("ddd")

    }

    //匹配list是几个元素的集合
    //nil代表一个空的list

    val list=List(0,2)
    list match {

      case 0::Nil=>println("only 0")//制作了一个只有0的list集合
      case x::y::Nil=>println(s"$x,$y")//制作了俩个未知数的list集合,所以这个可以匹配的上
      case 0::a=>println(s"$a")//scala因为匹配的是list(只有list有::) 底层帮你把0,和任意数放在list()里面,这个也可以匹配的上
    }

    }
}


//样例类 是一个特殊的类 专门用来进行模式匹配
case class Dog(name:String,color:String)
case class Cat(eye:String,leg:Int)
case object sun

  object classText extends App {

    val dog=Dog("阿黄","黄色")
    val cat=Cat("rubyEye",4)
    println(dog)
    println(cat)

    val arr3=Array(dog,cat,sun)
    arr3(Random.nextInt(arr3.length)) match {

      case Dog(x,y)=>println(s"名字$x,颜色$y")
      case Cat(x,y)=>println(s"眼睛$x,腿$y")
      case sun=>println("单例")

    }
  }

