import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class Test {


    public static void main(String[] args) throws IOException {


        Configuration conf = new Configuration();

        conf.addResource("hdfs://127.0.0.1:9000");

        conf.addResource("alwaysdazz");

      //  conf.addResource("/");

        FileSystem filer = FileSystem.get(conf);

        FileStatus fileStatus = filer.getFileStatus(new Path("/aaa"));

        //获取分组
        System.out.println(fileStatus.getGroup());

        //获取最后的访问时间
        System.out.println( fileStatus.getAccessTime());

        //下载
        System.out.println(fileStatus.isDirectory());

         //判断路径下的所有文件及目录

        System.out.println(fileStatus.getPath());



    }



}
