package Demo
/*不明白

//构造器,即在类名后面加上参数 空为无参 有参数为有参,当主构造器用private修饰时 只能通过从构造器或者通过伴生对象apply工厂方法
class Student private(var id:Int,var name:String) {

  var id_ = id
  var name_ = name

  println("id是"+id_)
  println("名字是"+name_)
  def sayHellp(): Unit = {

    println("hello world")
  }


  //从构造器 都是以def this开始,
  def this(id: Int, name: String,age:Int) {
    this(id,name) //必须第一行写this关键字
    this.age=age
    println("hello,构造器")

  }

}

object Text2 {

  def main(args: Array[String]): Unit = {

    var p = new Student(33,"华安")

    p.sayHellp() //由于方法没有在堆内所以必须用实例来引用指向方法区才可以使用


  }


}
*/