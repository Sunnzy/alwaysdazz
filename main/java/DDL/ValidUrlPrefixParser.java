package DDL;

import org.datanucleus.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/*
解析valid-url-prefix.conf文件
 * 	获取正确的访问路径前缀

 */
public class ValidUrlPrefixParser {

    private static final String CONFIG_NAME="valid-url-perfix.conf";//加载配置文件

    public static Collection<String> parse()//定义的string泛型的 parse
    {
        InputStream input = ValidUrlPrefixParser.class.getClassLoader().getResourceAsStream(CONFIG_NAME);//用累的记载机制来获取方法
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));//将文件读取

        Set<String> lines=new HashSet<String>();//hash容器
        String line;

        try{
        while((line=reader.readLine())!=null)
        {
            if(line.startsWith("#"))//如果开头以#则为注释 跳过本次循环
            {
                continue;
            }
            if(StringUtils.isEmpty(line))//如果为空 在跳出本次循环
            {
                continue;
            }
            lines.add(line);
        }
        }

        catch (IOException e)
        {
            throw new ServiceRuntimeException(ExceptionCode.IOCode.IO_EXCEPTION, "配置文件valid-url-prefix.conf读取异常");
        }
        finally
        {
            try {
                reader.close();
            } catch (IOException e) {
                throw new ServiceRuntimeException(ExceptionCode.IOCode.IO_EXCEPTION, "流关闭异常");
            }
        }

        return lines;
}

    //测试
//    public static void main(String[] args) {
//        System.out.println(ValidUrlPrefixParser.parse());
//    }

    }
