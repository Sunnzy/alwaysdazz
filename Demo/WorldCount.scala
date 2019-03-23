package Demo

import org.apache.hadoop.hive.ql.plan.SparkBucketMapJoinContext

object WorldCount extends App {


  val list=List("Hello Array tom","Hello world thanks","and thanks","Array")//单词

  val split1=list.map(_.split(" "))//切割
  //List([Ljava.lang.String;@5ef6ae06, [Ljava.lang.String;@55dfebeb, [Ljava.lang.String;@6e35bc3d, [Ljava.lang.String;@1cdc4c27)

  val flat=split1.flatten//压缩 将
  //List(Hello, Array, tom, Hello, world, thanks, and, thanks, Array)

  val info=flat.map((_,1))//打标签 每个字母后面跟这一个1 所以这里需要元祖
  //List((Hello,1), (Array,1), (tom,1), (Hello,1), (world,1), (thanks,1), (and,1), (thanks,1), (Array,1))

  val grop=info.groupBy(_._1)//分组 拿取第一个字段作为key分组
  //Map(world -> List((world,1)), thanks -> List((thanks,1), (thanks,1)), Array -> List((Array,1), (Array,1)), tom -> List((tom,1)), Hello -> List((Hello,1), (Hello,1)), and -> List((and,1)))

  val counte=grop.mapValues(_.size)//直接计算map中value的值
  //Map(world -> 1, thanks -> 2, Array -> 2, tom -> 1, Hello -> 2, and -> 1)

  val listt=counte.toList//将map转为list 目的是为了排序输出
  //List((world,1), (thanks,2), (Array,2), (tom,1), (Hello,2), (and,1))

  val sot=listt.sortBy(_._2)//按照第二个位置排序
  //List((world,1), (tom,1), (and,1), (thanks,2), (Array,2), (Hello,2))

  val res=sot.reverse//反转输出 按照从大往小的顺序
  //List((Hello,2), (Array,2), (thanks,2), (and,1), (tom,1), (world,1))


                       //压平切割          按空格切    贴标签1   分组按第一个   统计value个数    转list排序           反转 输出 ojbk
  val result=list.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).mapValues(_.size).toList.sortBy(_._2).reverse
  println(result)



  //方法二
//   val conf=new SparkConf().setAppName("aaa").setMaster("local[*]")
//   val sc=new SparkContext(conf)
//                                                          //通过key进行聚合value1+1+1
//   textFile("/home/1.txt").flatmap(_.split(" ")).map((_,1)).reduceByKey(_+_).foreach(println())



}
