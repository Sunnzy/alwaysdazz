package Demo

object LazyFuction {

  def init() ={

    println("hello.Lazy")
    ""//返回值为空

  }


  def main(args: Array[String]): Unit = {
  //lazy关键字 叫做惰性变量 被lazy修饰的变量只有在被用到的时候才会被加载,并且无论多少次调用，实例化方法只会执行一次。
  //底层使用的java懒加载方式自动帮我们实现了,还加了锁避免多线程同时调用初始化方法可能导致不一致问题
     lazy val in=init()

    println("这是mian方法")
    println(in)//这次调用成功了
    println(in)//这次没有别调用

  }



}
