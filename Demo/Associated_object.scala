package Demo
/*

自我理解 如果一个类没有伴生对象则需要new,如果一个类不需要new则一定有一个伴生对象
因为伴生对象底层默认实现了apply方法,此方法在伴生对象中自动new了一个类的对象,如果类有参数的话,可以直接使用伴生对象传参即可,伴生对象底层自动会帮我们把参数传递给类
tips:伴生对象没有参数,如果可以传参 那也是传给了类

 */
//这是类
class Associated_object {

  val id:Int=9527
  var name:String=_
  private val age:Int=20
  private[this] val school="清华"
  //如果object里面的属性被修饰符private[this]修饰了,那么伴生对象也无法访问此属性,private[this]是scala最高界别的私有属性,只有object内部可以访问
  //除了private[this]关键字 其他的私有属性都可以互相访问

  def sayHello(other:Associated_object) ={

    println(other.age)
  }

//  def sayHelll(other:Associated_object)={
//
//    println(other.school)
//  }

}


//伴生对象 可以直接. 不需要new 如果看见一个类没有new 说明他有伴生对象
//单例 地址只有一个 都是他自己 ,伴生对象可以理解为单例对象,所有人公用一个地址数据
object Associated_object extends App{


  val p=new Associated_object
  val p1=new Associated_object
  p.name="华安"
  p1.name="安华"
  p.sayHello(p1)//20

  println(p.name+""+p.age)//华安20



}
//静态工具类 测试伴生对象的地址 及类的地址
object testt extends App{

  val a1=new Associated_object
  val a2=new Associated_object
  val a3=Associated_object
  val a4=Associated_object

  println(a1)
  println(a2)
  println(a3)
  println(a4)
//    Demo.Associated_object@4fe3c938
//    Demo.Associated_object@5383967b
//    Demo.Associated_object$@2ac273d3
//    Demo.Associated_object$@2ac273d3
//结果得知 new的俩个类的地址都不相同,而伴生对象的地址确实完全相同,所以伴生对象为静态类 可直接调用



}