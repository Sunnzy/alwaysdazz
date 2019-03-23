package Demo
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer, Set}
//传名和传值参数
object Demo01 extends App {

  def time()={

    println("获取时间,单位为毫秒")
    System.nanoTime()//时间毫秒

  }


//传值调用,直接获取参数名中的值放入主函数中    传值调用（call-by-value）
  def delayed(t:Long) ={

    println("在delayed方法内")

    println("参数为"+t)

  }

//  获取时间,单位为毫秒
//  在delayed方法内
//  参数为10874963211520
delayed(time())


//传名调用当遇到参数名的时候 计算参数名中的逻辑    传名调用（call-by-name）
  def delayedd(t : =>Long) = {

    println("在delayed方法内")

    println("参数为" + t)
  }
//
//  在delayed方法内
//  获取时间,单位为毫秒
//  参数为11088250581065

  delayedd(time())

//高阶函数===================
  val salaries=Seq(20000,30000,50000)//定义数组
  val doublesalary= (x: Int)=> x*2//函数
  val newSalaries =salaries.map(doublesalary)//map方法
  println(doublesalary)//函数名function
  println(newSalaries)//40000,60000,100000
  println(salaries.map(_*2).mkString)//等同于上面的三个式子 mkstring 可以把集合转为字符串

//匿名化 指 没有函数指向定义的变量==========================
  val arr =Array(3,6,9)
  val result=arr.map(a => a*3)
  println(result.mkString(""))


  val arrr=Array(3,6,9)
  val resull=arrr.map(3 *_)
  println(resull.mkString(""))


  val arrs=Array(3,6,9)
  val arrd=arrs.map(_ *2)
  println(arrd.mkString(""))


  //高阶函数1-1========================
  def m6(x:Int,y:Int,f1:(Int,Int) =>Int): Any =
  {
    f1(x,y)
  }

  val sn=(x:Int,y:Int)=>x*y//定义的函数 在里面写相应的逻辑
  m6(3,6,sn)//传入俩个参数3,6给方法 传入函数sn给方法的f1



  //高阶函数1-2===========================
  def test(n1:Int,n2:Double,f : Int =>Double,f1 : (Double,Int) => Int)={

    f(f1(n2,n1))//②先运算f1函数里的逻辑 在运算f逻辑

  }

  def sanx(s : Int):Double ={

    s+s+1

  }

  def sum(d : Double,d1:Int ) :Int={
    (d*d1).toInt
  }


  val res =test(6,3.0,sanx,sum)//①sanx指向test的f 3.0默认指向n2,6默认指向n1,也可以n1=6，n2=3.0
  println(res)//37




  //高阶函数2 返回函数======================

  def test(x: Int)={//括号可以去掉

    (y:Int)=>x-y//匿名函数

  }

  val f1=test(3)//f1=(y:Int)=>3-y 即只要f1,只要在f1的生命周期内3是不会变的永远是他
  println(f1(9)) //f1=(9:Int)=>3-9  这个9传给的是y 即3-9

  //也可以写成这样 柯里化 一步到位 现将3传给了x (y:Int)=>3-y 但是没有变量接收 然后在传进一个9  (9:Int)=>3-9
  println(test(3)(9))


//==============================


  def m2(f:(Int,Int)=>Int)= f(2,6)//def 这是方法,方法名为m2,参数为俩个Int返回类型=>代表为要引用传名函数 f(2,6)为调用自身的并引入参数

  val f2=(x:Int,y:Int)=>x+y//函数
  println(m2(f2))//将函数放入方法中进行加法运算

  println(m2((x:Int,y:Int)=>x+y))//直接上匿名函数吧
  println(m2(_+_))//终极输出 这才是我喜欢的方式




  //高阶函数值传递方法=====================

  def m7(s:Int,x:Int,f1:(Int,Int)=>String): Any =  //方法1
  {
    f1(s,x)
  }


  def m8=((x:Int,y:Int)=>x.+("qqq")+y.+("ggg")) //方法2

  println(m7(2,3,m8))  //2,3为m7方法的参数,m8为方法传入m7内 实际上是将m8的方法转换为函数传入m7


//闭包================================

  val factor =3//闭包就是在函数之外 在定义一个参数(即返回值结果依赖于外部环境参数 就形成了闭包)
  val nim=( i : Int)=> i*factor//闭包函数 i是匿名函数的形参,自由变量factor含义是引用函数外面定义的变量,定义这个函数的过程是将这个自由变量捕获而构成一个封闭的函数。

  println(nim(3))//3*3=9


  //string 在定义之后就不可改变了 但是可以使用String bulider类=============
  val buf = new StringBuilder;//''只能是一个字符 char 并且是+=  ""是双引号可以添加多个字体 并且是++=
  buf += 's'
  buf += 'a'
  buf ++= "sss"
  buf += 'a'
  buf ++="aaaa"
  buf ++="abcdefg"
  val buff=new String("ssss")

  println(buff.concat(buf.mkString),buff+buf)//字符串连接
  println(buf.charAt(0))//返回指定位置的字符
  println(buff.compareTo("sss"))//按字典顺序比较俩个字符串
  println(buff.endsWith("s"))//判断字符串是否以指定的字符结尾

  //=========array

  var z:Array[String] = new Array[String](3)//定义容量为3的string的array数组
  z(0)="33";z(1)="4";z(2)="55"//给下角标为0 1 2 赋值
 z=Array("ss","dd","56a")//也可以直接用array赋值
  var v=Array("s","d")//直接array赋值

  for(i<-0 to v.length)//i到v长度的循环
    {
      println(i)//1 2
    }


  val sdf=new ArrayBuffer[Int]()//可变长度数组
  sdf.+=(2)
  sdf.+=(3)
  sdf.+=(4,5,6)
  sdf.insert(0,-1,0)//在0下角标添加俩个位置-1 0
  sdf.append(10)//追加
  println(sdf.toBuffer)

  val ssdd=Array(("tingting",24),("huanhuan",25),("hahaha",36.0))//定义array的元祖
  println(ssdd.toMap)//array转Map(tingting -> 24, huanhuan -> 25, hahaha -> 36.0)

  val ss=Array(3,4,5,99)
  val dd=Array(7,8,9)
  val asd=ss.zip(dd)//拉链 下角标相同的对应
  println(asd.toBuffer)//ArrayBuffer((3,7), (4,8), (5,9))

  val mywords= ss++dd//俩个数组的拼接
  for(i<- mywords){

    println(i)

  }

  //=========list集合
  val listbuff=ListBuffer.apply(1,2,3,4,5)//可变长集合 但是不管可变长还是不可变长 在元集合都不会改变 只会生成一个新的集合
  val lis=listbuff.+=(6)
  println(lis)


val site:List[String]=List("st","ea","s")//定义list
  for(i <- site)
    {
      println(i)
    }

val sitt="st"::("ea"::("m":: Nil))//等同于上面

  println(sitt.isEmpty)
  println(sitt.head)
  println(sitt.tail)
  println(sitt.:::(site))//连接俩个list集合
  println(sitt.::("ssda"))//向sitt添加ssda
  println(("ssda"::sitt))//等同于上面 相当于是后面这个数组的方法 没有.了
  println(0::site)//向sitt添加ssda
  println(sitt.+:("h"))//向头部插入一个和：：区别不大
  println(sitt.:+("o"))//向尾部插入一个


  val si=List.fill(3)("Runnoob")//重复3次Runnoob
  val sii=List.fill(3)(9)//重复3次9
  println(si)

  //只有list有::这种方式   1::Nil 新建list的一种方式
  var summm=1::Nil
  var summ=2::(1::(4::(3::Nil)))//定义list数组,Nil是空集合 新建list列表的一种方式
  summ=List.tabulate(5)((s:Int) =>s*2)//通过给定的函数创造列表 0 2 4 6 8,方法的第一个参数为元素的数量，可以是二维的，第二个参数为指定的函数，我们通过指定的函数计算结果并返回值插入到列表中，起始值为 0
  summ= List.tabulate(5)(_*2)//等同于上面
  println(summ)

//===========set集合
  val p=Set(1,2)
  println(p.getClass.getName)//默认不可变集合scala.collection.immutable.HashSet$HashTrieSet
  println(p.exists(_%2==0))//true

  val p1=Set(2,3)
  println(p1.getClass.getName)//import scala.collection.mutable.Set可以增加数组内的内容
  p1.+=(6)//增加一个元素6
  println(p1)

  println(p.&(p1))//显示俩个set数组的交集数 2

  val it =Iterator("hauwei","baidu")
  while (it.hasNext)
    {
      println(it.next())
    }
//===========map集合

  var sssss:Map[String,Int]=Map("s"->1,"q"->9)
  sssss += ("ss" -> 1)//添加元素
  println(sssss("s"))//按key拿值
  println(sssss.values)
  println(sssss.keys)
  println(sssss.isEmpty)
  sssss.keys.foreach {i=>println(i);println(sssss(i))}
  println(sssss.getOrElse("sss",1))//当map容器内没有sss的key时 则会输出1
//==========元祖

  val ts=("s",5,5.6,'s')//定义元祖
  val tts=new Tuple3(1,3.14,"sd")//Tuple3为容纳3个元素的元祖
  val tuple=("s",2,6.0)//定义tuple 最大值是22个
  val sume=tts._1+tts._2+tts._3//将下角标为1 2 3 加在一起
    println(sume)//4.140000000000001sd
  val sst =new Tuple2[String,String]("s","d")//定义一个容纳为2个元素的元祖 方括号内的类型可写可不写
  println(sst.swap)//元素交换 位置互换,只能在俩个元素的元祖的时候使用
  tts.productIterator.foreach{ i =>println("Value = " + i )}
//  Value = 1
//  Value = 3.14
//  Value = sd


  class point(xc:Int,yc:Int)//定义类 并且俩个参数接收x y
  {
    var xx=xc
    var yy=yc

    def move (dx:Int,dy:Int):Any = {//定义方法 写个逻辑

      xx=xx+dx
      yy=yy+dy
      println(xx+"\t"+yy)
    }
  }
    new point(3,1).move(4,5)
//==========================
//case class persionn(name:String,age:Int)//第一个 这个类可以在模式匹配里匹到
//{
//
//}

class persionn(name:String,age:Int){}//或者是第二个 实现apply和UNapply

  object persionn{

    var name=""
    var age=0

    def apply(name: String, age: Int): persionn =
    {

      this.name=name
      this.age=age
      new persionn(name, age)
    }



    def unapply(arg: persionn): Option[(String, Int)] = {

      Some(this.name,this.age)
    }
  }


  val app=(x:Any)=>{//脑动大开  可以匹配自己定义的类 case class

    x match {//按照顺序匹配 如果把_放第一位那所有的都是这个

      case 1=>println("额能")
      case y:String=>println("字符串")
      case y:Int=>println("int")
      case persionn(name,age)=>println("persion")
      case _=>println("匹配不到的都在这里")

    }

  }
  app(persionn("name",16))




  }
