package MyMapReduce.Day10;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MyMapJoin {

/**
 * map  join 大表关联小表；会将小表加载到每个maptask中。
 * 如何实现将小表分发给每个maptask
 * 可以只有map没有reduce。
 */
    static class Mymapper extends Mapper<LongWritable,Text,Text,Text>
{
    /**
     * setup方法是每个MapTask都会运行一次的方法，该方法主要是初始化一些业务需要的逻辑类。
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    private Map<String,String> map=new HashMap<String, String>();
    private BufferedReader fs;
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //在这里导入小表的数据
        //首先获取路径
        URI[] cacheFiles = context.getCacheFiles();//get到实例,拿到文件的uri
        for(URI uri:cacheFiles)//将路径拿出来
        {
            fs=new BufferedReader(new FileReader(uri.getPath().toString()));//通过uri获取缓存的文件路径 开一个输入流 该流读取部门数据
            String s = fs.readLine();//先读取一行
            while(s!=null)//如果该行不为null,则截取该条数据
            {
                String[] split = s.split(",");//将该行的字段按，隔开
                map.put(split[0],split[1]);//将部门编号和名称放入map中
                System.out.println("++++++"+split[0]+split[1]);
                s=fs.readLine();//这里更新一下
                /*
                ++++++30sales
                ++++++20research
                ++++++10accounting
                 */
            }
        }
    }
    //这里获取员工的数据
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] split = value.toString().split(",");
        String k=split[7];//拿取员工的部门编号
        if(map.containsKey(k))// 对比数组里有没有相同的数值
        {
            String deptno = map.get(k);//获取部门map相对应键的值
           String v=split[0]+","+split[1]+","+k+","+deptno;
            context.write(new Text(v),new Text("")); //这里不需要reduce 只要map输出即可
        }
    }

    @Override//关流操作 需要再次使用BufferedReader fs 所以需要全局化
    protected void cleanup(Context context) throws IOException, InterruptedException {
        fs.close();
    }

    public static void main(String[] args) throws Exception {

        Job instance = Job.getInstance();

        Configuration configuration = instance.getConfiguration();
        FileSystem fileSystem = FileSystem.newInstance(configuration);
        //job主要是用来定义整个作业所依赖的mapper，reducer，driver
        instance.setJarByClass(MyMapJoin.class);
        instance.setMapperClass(Mymapper.class);

        instance.setOutputKeyClass(Text.class);  //最终的key输出
        instance.setOutputValueClass(Text.class);//最终的v输出

        instance.addCacheFile(new URI("/home/alwaysdazz/桌面/Hadoop/day10/join操作/部门.txt"));  //将部门数据缓存起来，会在maptask获取到该文件的路径。

        Path path = new Path("/home/alwaysdazz/6.txt");
        if(fileSystem.exists(path)){
            fileSystem.delete(path,true);
        }

        FileInputFormat.setInputPaths(instance,new Path("/home/alwaysdazz/桌面/Hadoop/day10/join操作/员工.txt"));//设置输入路径
        FileOutputFormat.setOutputPath(instance,path);

        System.exit(instance.waitForCompletion(true) ? 0 : 1);

    }
}
}
