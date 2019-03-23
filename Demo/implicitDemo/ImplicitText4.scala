package Demo.implicitDemo

object ImplicitText4 {

  //隐式方法

  def m(x:Int):Int={

    x+1
  }


  def main(args: Array[String]): Unit = {

    import ImplicitPanel._
    val result=m("1")//我的隐式面板里 有字符串转int方法 直接调用
    println(result)
  }


}
