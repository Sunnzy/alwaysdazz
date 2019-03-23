package Demo

class MyPartialFunction {

  //偏函数
  //定义PartialFunction 并且没有match关键字
  //概念:一组被{}包围的case语句
  //普通方法也可以使用case,但是需用match匹配

                  // 输入string 返回int
  def fun:PartialFunction[String, Int] = {

    case "one" => 1
    case "two" => 2
    case _ => 3 //你为了什么而去奋斗——龙族

  }

                    //id是以string类型的
  def myPartialFunction(id: String): String = id match {
    case "001" => "张三"
    case "002" => "李四"
  }

}

  object partialFunctionText extends App{

    val newPartial=new MyPartialFunction
    val result =newPartial.fun("who am i")
    println(result)

  }


