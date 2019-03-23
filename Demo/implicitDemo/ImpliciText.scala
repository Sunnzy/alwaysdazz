package Demo.implicitDemo

object ImpliciText {


  implicit val name: String = "李四"//此为隐式参数 即编译器会在本类中寻找(无论放在任何位置都可以找到)切记 每种类型只能隐藏一次 多了无效
  //隐式转换就是,当scala编译器进行类型匹配时,如果找不到合适的候选 那么隐式转换就会给你提供
  //另一种途径告诉编译器将当前的类型转换为预期的类型

  //作用 通过隐式转换程序员可以编写scala程序时,故意漏掉一些代码让编译器在编译期间自动导出这些信息和特征可以极大的减少代码量
  //忽略那些冗长,过于细节的代码

  //1.将方法或变量标记为implicit
  //2.将方法的参数列表标记为implicit
  //3.将类标记为implicit


  //例一隐式参数===================

  def sayHi(implicit name:String): Unit ={

    println(s"名字为$name")
  }

  def main(args: Array[String]): Unit = {

    sayHi

  }

}



