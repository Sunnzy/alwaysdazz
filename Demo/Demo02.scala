package Demo

import scala.actors.Actor

object Demo02 extends App{


  val list=List(1,2,3,4,5,6,7,8,9)

//  println(list.max)//最大值 9
//  println(list.min)//最小值 1
//  println(list.sum)//求和45
//  val result=list.map(println)//打印输出 自动转换,此方法会创建一个空的数组 尽量不要使用此方法
//  for(i<-list)//foreach 可以遍历很多
//    {
//      println(i)
//    }
//  list.foreach((i:Int)=>println(i))//使用foreach方法
//  list.foreach(println)//跟简单


  val arr=Array("Hello",3,2.0)
 // arr.foreach(println)

 // for(i<-arr if i==2) println(i)//for循环加判断


//  val newArr=for(i<-list) yield i*10 //同等于map操作生成一个新的数组都是乘以10的
//    val newmap=list.map(_*10)
//    println(newmap)//List(10, 20, 30, 40, 50, 60, 70, 80, 90)
//  println(newArr)//List(10, 20, 30, 40, 50, 60, 70, 80, 90)

  //聚合函数
  val list1=List(1,2.3,8)//数组最大值
 // println(List(2,90,11,5,6).sorted.reverse.length)//排序+倒序+长度
 // println(list.reduce(_+_))//相加 45 第一个下划线是0  0+1=1 1+2=3 3+4=5
// println(list.par.fold(1)(_+_))//fold和reduce一样但是有初始值,par是多核并行计算 多加几个1就是多几个核因为每个核都拿初始值来计算
  //根据核随机分组每个核都去拿最初始的数值 即： 123+1 456+1 789+1 1 此处运用了3核来计算的



  //聚合局部求和在汇总
  val list2=List(List(1,2,3),List(2,3,4),List(4,5,6))//list内部在嵌套
//  val ss= list2.aggregate(0)(_+_.reduce(_+_),_+_)//局部计算 先计算局部的值 在汇总局部的值 0为初始值 第一个是局部逻辑 第二个是汇总逻辑
//  println(ss)

  //统计count
  //println(list.count(_>2))//统计大于2的数  7
  //println(list.sortWith(_<_))//类似冒泡 前后俩个相比较小的在左侧大的在右侧 List(1, 2, 3, 4, 5, 6, 7, 8, 9)

//交集并集差集
//  println(list.intersect(list1))//交集 List(1, 8)
//  println(list.union(list1))//并集 List(1, 2, 3, 4, 5, 6, 7, 8, 9, 1.0, 2.3, 8.0)
//  println(list.diff(list1))//差集 相对于list1没有的集合 List(2, 3, 4, 5, 6, 7, 9)



  //过滤filter
 // println(list.filter(_.>(5)))//大于5的数据  List(6, 7, 8, 9)

























}
