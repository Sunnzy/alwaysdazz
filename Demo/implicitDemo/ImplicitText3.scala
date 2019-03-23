package Demo.implicitDemo



object ImplicitText3 {

  import ImplicitPanel._//导入该类所在的位置
  //隐式类

  def sayHi2(implicit P:Person): Unit ={


    println(P.mame+" "+P.age)

  }


  def main(args: Array[String]): Unit = {

    sayHi2
  }

}
