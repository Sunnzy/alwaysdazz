package Demo.implicitDemo

class ImplicitText5 {


  def SheRi(): Unit ={

    println("射日")

  }
}

class Human{

  def eat(): Unit ={

    println("吃饭")

  }
}

object implText{

  def main(args: Array[String]): Unit = {

    import ImplicitPanel._
    val sit=new Human
    sit.SheRi()


  }
}