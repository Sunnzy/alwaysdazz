package Demo.implicitDemo

object ImplicitText2 {
//使用面板的形式的隐式参数 即 将隐式值放到面板(专一的一个类),然后导入该类的地址 即可使用
  def HelloImplicit(implicit name:String,age:Int): Unit ={

    println(s"名字为$name+$age")

  }

    def main(args: Array[String]): Unit = {

     import ImplicitPanel._
      HelloImplicit
    }
}
