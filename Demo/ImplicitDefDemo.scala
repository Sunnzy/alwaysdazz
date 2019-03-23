package Demo

object ImplicitDefDemo extends App{
//隐式方法                  //tup类型 输入string和Int类型,返回值是string 返回结果为tup的string和int的数值
implicit def showname1(x:(String,Int)):String =x._1+x._2+"name1" //返回
implicit def showname2(x:String,y:Int):String =x+y+"name2"

  //只要你的参数列表是符合你的隐式方法的参数列表那么在你使用其他方法的时候你可以只传参数列表的值 而不用传方法名
  def showname(name:String)=println(name)//定义的一个方法
  showname("hello",1)//①//这个方法只是隐藏了showname2的方法
  showname(("Hello",2))//②
  showname(showname2("HELLO",3))//③ 这个就相当于①的写法 在方法内调用另一个方法

}

//使用类不存在的方法======================

class Person{
  def studay (kemu:String)= println(s"$kemu,真好")

}


  object Student {

    implicit def ssr(demo:Aminal) = new Person //隐式
}


class Aminal

 //导入该student下的所有方法包括隐式方法
object Aminal extends App{

   import Student._//导入该student下的所有方法 包括隐士方法

   val dem=new Aminal
   dem.studay("隐藏不存在的方法")

 }



