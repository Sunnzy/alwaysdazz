package Demo

import scala.actors.Actor

/*

!  发送异步消息,没有返回值
!? 发送同步消息,等待返回值
!! 发送异步消息,返回值是Future[any]

 */
//学习开启线程 即Actor类的act方法
//1.首先调用start方法启动actor
//2.调用start方法后其act方法会被执行
//3.向Actor发送消息
/*Thread封装好了一些东西 进行一个调用就可以使用，显然你要开启一个线程，相当于在进程之间，开辟一块小的区域 想要实现多线程，只需要继承这个类，或者Runalbe接口 就可以实现它

Java中 有多线程，在scala中必然也有，叫做Actor,这个编程模型是已经封装好的，只需要调用即可，并且会使用api 就可以使用多线程了，Actor是scala中多线程。
我们学习Actor 是因为我们要为学习Akka做准备。
*/
class MyActor extends Actor {
  override def act(): Unit = {
    //这就是actor要实现的act方法  即 java中的run方法

    for (i <- 1 to 10) {

      println(Thread.currentThread().getName + "线程为" + i) //获取当前线程的名字
      println(Thread.currentThread().getId+"线程id"+i)
      Thread.sleep(1000) //此线程睡1s
    }
  }
}
  object ActorText extends App{

    val thread1=new MyActor
    thread1.start()//开启线程 start方法

    val thread2=new MyActor
    thread2.start()//开启线程

  }
//======================发送异步消息没有返回值！

class MyActor2 extends Actor{
  override def act(): Unit = {
    //偏函数联系 即在方法内可以用case语句,这个receice是底层封装过PartialFunction的方法
    receive{
      case "Hello"=>println("hi")
      case "你好"=>println("你好")
      case "how old are u"=> println(20)
    }
  }
}


object Actor2Text extends App{

  val thread1=new MyActor2
  //启动线程
  thread1.start()
  //发送消息
  thread1 !"Hello"//发送异步消息 没有返回值 即不用刻意等待消息接收 直接结束
  println("发送成功")

}

//=====================发送同步消息等待返回值！？
class MyActor3 extends Actor{
  override def act(): Unit = {

    receive{

      case "Hello"=>sender !"你好"//发送异步消息 没有返回值 即 当传来一个消息后 我需要返回一个信息 但是不需要接收
      case "你好啊"=>sender !"ojbk"

    }

  }
}

object Actor3Text extends App{

  val thread1=new MyActor3
  thread1.start()////同步消息 有返回值 即发完消息后要等待返回的信息
  val result=thread1 !?"你好啊" //发送完消息后 有返回值需要接收 因为是!?又返回值 所以需要接收,当然也可以不接受
  println(result)
  println("发送成功")


}

//=====================死循环！！


class MyActor4 extends Actor{

  //实现继承类里面的方法 act
  override def act()= {

  while(true)
  receive{
    {

      case Msg(aggrment: String) => sender !aggrment //如果匹配到Msg类则 回复发来的消息aggrment ！发送异步消息,没有返回值
      case Stops => {
        //如果匹配到Stops类 则输出以下内容
        println("停止死循环")
        Thread.sleep(20000) //睡2s
        exit() //退出循环

      }
    }
    }
  }
}

object Actor4Text extends App{

  val Thread1=new MyActor4//new一个线程
  Thread1.start()//启动线程
  Thread1!Msg("Hello")//发送消息
  Thread1 !Stops//发送stop样例类
}

case class Msg(s:String)

case object Stops


//==================react函数loop函数 创建线程池:即 创建的线程不会因为使用一次而扔掉 而是一直在线程池里等待任务 very nice
//除了使用偏函数(在类里面case)还可以使用其他方式来接受消息react
//这种方式也可以接受请求,线程可以重复利用,而不是去创建新的线程
//线程池的复用需要一个组合loop+react可以足够实现线程池的复用(不要再使用while了),需要使用线程就去线程池里拿,线程用完不销毁,归还到线程池里

class MyActor5 extends Actor{
  override def act(): Unit = {

    loop{
      react{
        case "Hello"=>sender!"收到"

      }
    }
  }
}

object Actor5Text extends App{


  val thread1=new MyActor5
  thread1.start()
  val result=thread1!!"Hello"
  if(!result.isSet){//如果非空

    println(result.apply())

  }

}



























