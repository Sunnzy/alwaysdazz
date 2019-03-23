import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

public class PATH {


    public static FileSystem ff= null;

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        ff = FileSystem.newInstance(new URI("hdfs://192.168.1.137:9000/"), conf, "alwaysdazz");

        forfor(new Path("/"));
    }



    public static void forfor(Path parh) throws IOException {

    //    FileStatus[] fileStatuses = ff.listStatus(new Path(path));

        FileStatus[] fileStatuses1 = ff.listStatus(new Path(String.valueOf(parh)));
        for(FileStatus fl:fileStatuses1)
        {
            if(fl.isDirectory())
            {
                System.out.println(fl.getPath());
               Path path = fl.getPath();
                forfor(path);

            }
            System.out.println(fl.getPath());

        }

    }

}
