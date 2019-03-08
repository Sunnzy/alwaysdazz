package MyMapReduce.Day10;

import MyMapReduce.MyReduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyReduceJoin {

    /**
     * reduceJoin
     * 需求类似于：                                                       key为 deptno
     *select e.empno,e.ename,d.dname,d.deptno from emp e join dept d on e.deptno=d.deptno;
     * 思考，join操作，发生在reduce端，也就是说，我们的key将会关联条件的那个字段。
     * 然后，在map端给不同的数据打标记，再在redeuce端进行数据拆分，拼接操作。
     */
    //首先 创建内部类

    static class mymapper extends Mapper<LongWritable,Text,Text,Text>
    {
        String v="";
        String k="";

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String[] split = value.toString().split(",");

            if(split.length>3)//这里是判断员工
            {
                 this.v=split[0]+","+split[1]+","+0;//这里我抓取了 编号 和员工姓名还有 打上了烙印0
                 this.k=split[7];//不能拿去key吗
            }
            else //这里抓取了部门编号
            {
               this.v=split[0]+","+split[1]+","+1;
               this.k=split[0];
            }

           context.write(new Text(k),new Text(v));
           // System.out.print(" *** "+k+v);
        }
    }

    static class myReduce extends Reducer<Text,Text,Text,Text>
    {
        String v = "";
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
/*
        来个集合 让我们来放些东西

 */

            List<String>list=new ArrayList<String>();//这个放员工吧
            List<String>list1=new ArrayList<String>();// 这个放部门咯


            //key:30 , value: (编号，姓名，0)（部门编号，部门名称，1）
        //    String[] split = values.toString().split(",");

            for(Text t:values)//将数组内的内容遍历出来
            {   System.out.println("******"+t);
                String[] split = t.toString().split(",");
                System.out.println("////////"+split.toString());
                if (split[2].equals("0"))//如果数组内的第三个号位为0则为员工
                {
                    list.add(t.toString());

                } else {
                    list1.add(t.toString());

                }
            }

            for(String yuan:list)
            {
                System.out.println("++++++"+yuan);
                for(String bu: list1)
                {System.out.println("-------"+bu);
                   v=(yuan+bu).replace(",0","-").replace(",1","");
                   context.write(new Text(v),new Text(""));

                }
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Job instance = Job.getInstance();//获取实例
        Configuration configuration = instance.getConfiguration();//加载配置
        Path path=new Path("/home/alwaysdazz/3.txt");
        FileSystem fileSystem = FileSystem.get(configuration);
        if(fileSystem.exists(path))
        {
            fileSystem.delete(path,true);
        }

        instance.setJarByClass(MyReduce.class);
        instance.setMapperClass(mymapper.class);
        instance.setMapOutputKeyClass(Text.class);
        instance.setMapOutputValueClass(Text.class);

        instance.setReducerClass(myReduce.class);
        instance.setOutputKeyClass(Text.class);
        instance.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop/day10/join操作"));
        FileOutputFormat.setOutputPath(instance,path);

        System.exit(instance.waitForCompletion(true)?0:1);
    }
}
