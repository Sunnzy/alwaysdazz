package Demo

object HelloWorld {//类
import java.util.{HashMap=>mk}//重命名,该类下所有成员均可以使用该名称
import java.util.{HashMap=>_,_}//引入了所有util的所有包但是hash成员被隐藏
  def main(args: Array[String]): Unit = {//main方法

    //  var name,ami:String ="小明"
    //  var genner:String="男"
    //  val age:Int=18
    //  var sal:Float=188.8f
    //  var hight :Double=12.5
    //  var pa=("张三",28.0f,"男")//元祖
    //  pa:(String,Float,String)
    //    println(name + genner + age + ami+pa)
//    ssh(1,2)
//    sshh(1, 3)
//
//    val nm=new mk()//hashmap
//    (new Outer).say()//当用calss修饰的时候new一个出来 当用Object时直接点
//
//    forfo()
//    two()

    test(5)
    test2(5)
val x=1
val y=if (x > 0) 1 else 0
  }

//def two(){ for(i<-Range.inclusive(1,10) )
//{
//  if(i%2==0)
//  {
//    println(i)
//  }
//}
//
//}



//  def forfo(): Unit ={
//
//    for(i<-Range.inclusive(1,10))//包头包尾  range包头不保尾
//      {
//        println(i)
//      }
//  }
//
//
//
//  def ssh(a:Int,b:Int):Any={//方法
//
//
//    println(
//      """ 我是陆超"a+b找"
//        |
//        |
//      """.stripMargin)
//    }


//  class Outer//类
//  {
//     def say():Unit={
//      println("helloworld")
//
//     def talk():Unit={
//       println("www")
//            say()
//       }
//
//    }
//
//
//  }

//
//  def sshh(a:Int,b:Int):Any=
//  {
//    if(a==1)
//      {
//       var a=123
//      println(a)
//        "13".toInt
//      }
//    else
//   {
//     a+b
//   }
//  }

//递归
  def test(n:Int): Unit =
  {
    if(n>2)
      {
        test(n-1)//调用自身再一次的循环 直到不能满足if条件 依次按照栈顶到main方法输出
      }
    println("n"+n)
  }

  def test2(n:Int): Unit =
  {
    if(n>2)
      {
        test2(n-1)
      }
    else
      {
        println(n)
      }

  }

}
