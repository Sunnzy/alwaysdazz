package MyMapReduce.DBwrite;


import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;


import java.io.IOException;

public class MyIntputFormat extends FileInputFormat {


    //自定义读取类        偏移量     内容
    static class MyrecodrCard extends RecordReader<LongWritable, Text> {

        public MyrecodrCard(){}
        private FSDataInputStream open;
        private long start;
        private long postion;
        private long end;
        //如果不new则报空指针异常
        private LongWritable key = new LongWritable();
        private Text value = new Text();

        public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            //获取切片
            FileSplit fs = (FileSplit) inputSplit;//按照逻辑切片
            //获取配置信息文件
            FileSystem fileSystem = FileSystem.get(taskAttemptContext.getConfiguration());
            //open方法可以从流中的任意位置读取数据
            this.open = fileSystem.open(fs.getPath());//后期可能会再次输入 改为全局变量
            //以切片的位置设置初始位置
            this.start = fs.getStart();
            //读取块的位置
            this.postion = fs.getStart();
            //结束位置
            this.end = this.start + fs.getLength();
            //返回 Long，它指定使用 FileOpen 函数打开的文件中的当前读/写位置；或设置使用 FileOpen 函数打开的文件中的下一个读/写操作的位置。 相比 Seek，My 功能可使文件 I/O 操作的效率更高、性能更好
            //可以随意跳到任意地方并得到其位置
            this.open.seek(this.start);
        }

        //下一行的kv
        public boolean nextKeyValue() throws IOException, InterruptedException {
            //两行的获取数值
            if (this.postion < this.end) {
                //将这一行赋值给s
                String s = open.readLine();
                //这一行的字节长度 给了偏移量
                this.postion += s.getBytes().length;
                //第二次判断 如果还是没有达到终点
                if (this.postion < this.end) {//则再讲下一行的数据给了ss
                    String ss = open.readLine();
                    //再将ss获得的数据字节给了偏移量 则完成了两行的输入
                    this.postion += ss.getBytes().length;
                    //当前的偏移量
                    key.set(this.postion);
                    //当前偏移量的内容
                    value.set(s + "," + ss);
                    //将当前的值返回 如果为true
                    return true;
                } else {
                    key.set(this.postion);
                    value.set(s);
                    return true;
                }

            } else {
                return false;
            }
        }

        //获取当前的k
        public LongWritable getCurrentKey() throws IOException, InterruptedException {
            return key;
        }

        //获取当前的v
        public Text getCurrentValue() throws IOException, InterruptedException {
            return value;
        }

        //获取进程
        public float getProgress() throws IOException, InterruptedException {
            return 1.0f * (this.postion - this.start) / (this.end - this.start);
        }

        //关闭读入流
        public void close() throws IOException {
            this.open.close();
        }


    }

    public MyIntputFormat() {
    }

    public RecordReader<LongWritable,Text> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new MyrecodrCard();
    }


}