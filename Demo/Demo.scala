package Demo

import java.util.Calendar

import scala.collection.mutable.ArrayBuffer

object Demo extends App {





//    def sayok(): Unit ={//final方法
//
//      println("mian sayok")
//    }
//
//    //定义数组
//    var arry:Array[Int]=new Array[Int](10)//new一个对象 int型 10个容量
//    var str:Array[String]=new Array[String](3)
//    val strr=Array("dunbi","shadiao","hyy")//直接定义
//    println(strr(0))
//    str=Array("helllo","ojbk",null)
//      arry=Array(1,2,3,44);println(arry(3))
//    for(i<-str(1))
//      {
//        println(i)
//      }
//
//
//    //变长数组 ArrayBuffer
//    val strbuff=ArrayBuffer[String]()
//    strbuff.+=("a","b","c")
//    strbuff.+=("cc","dd")
//   for(i<-strbuff)
//     {
//       println(i)
//     }
//
//    var ++ =ArrayBuffer[String]()
//    ++.+=("english","chinese","math")
//    for(i<- ++)
//      {
//        println(i)
//      }
//
//
//  for(i<-1 to 3;j<- 1 to 3 if i!=j)//双重for循环
//    {
//      println(i*10+j)
//
//    }
//
//
//  def ml(S:Int,y:Int):Int=S+y
//  ml(3,5)
//  println()

//  var name:String =_//代表空值
//  //val age :String=_
//  println(name)
//
//
//def lc:Int ={
//
  var x=3
val result ={

  if(x > 3)
    {
      3
    }
  else if(x<8)
    {
      "鸟哥"
    }
  else {
    "gg"
  }

}

println(result)

//  val v=for(i<- 1 to 10) yield i*10
//  for(i<-v)
//    {
//      println(i)
//    }

  val a=10
  val b=20
  println(a.+(b))
  println(a+b)


  println(Long.MaxValue)


}
