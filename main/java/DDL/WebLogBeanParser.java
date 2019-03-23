package DDL;


import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
//调取数据 预处理数据 被webLogPreProcess调用
public class WebLogBeanParser {

    public static SimpleDateFormat df1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.CHINA);
    public static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    //列分隔符
    public static final String SPLIT_DELIMITER = "\t";

    public static WebLogBean parse(String line) //将数据按行读取
    {
        if (StringUtils.isEmpty(line)) {
            return null;
        }

        WebLogBean bean = new WebLogBean();//new一个weblog的实例
        String[] fields = line.split(" ");//将读取的行按照空格切开
        if (fields.length < 12)//判断脏数据
        {
            bean.setValid(false);
            return bean;
        }

        bean.setRemote_addr(fields[0]);
        bean.setRemote_user(fields[1]);
        String time_local = getFormatDate(fields[3].substring(1));//截取fields的第三个数据的第1个单词

        if (time_local == null) {
            bean.setValid(false);//如果时间为空则为脏数据
            return bean;
        }

        bean.setTime_local(time_local);//否则就是正确时间
        bean.setRequest(fields[6]);
        bean.setStatus(fields[8]);
        bean.setBody_bytes_sent(fields[9]);
        bean.setHttp_referer(fields[10]);

        //获取客户端浏览信息

        StringBuilder sb = new StringBuilder();//追加等同于字符串的拼接
        for (int i = 11; i < fields.length; i++) {
            sb.append(fields[i]);//将一行数据输出
        }
        bean.setHttp_user_agent(sb.toString());//整条数据为客户端浏览信息

        if (Integer.parseInt(bean.getStatus()) >= 400) ;//将字符串转换为数字比较大小
        {
            bean.setValid(false);//数据大于400为脏数据
        }
         return bean;
    }


    public static String getFormatDate(String time_local)
    {
        try {
            return df2.format(df1.parse(time_local));//将格式1转换为格式2
        } catch (ParseException e) {
            return null;
        }
    }

    public static void filtStaticResource(WebLogBean bean, Collection<String> pages)
    {
        if(!pages.contains(bean.getRequest()))//看看collection集合是否包含 getrequest 如果不包含为脏数据
        {
            bean.setValid(false);
        }
    }

    public static Date toData(String timestr)
    {
        if(StringUtils.isEmpty(timestr))//如果日期为空则返回null
        {
            return null;
        }

        try {
            return df2.parse(timestr);//将日期转为类型2
        } catch (ParseException e) {
            throw new ServiceRuntimeException(ExceptionCode.COMMON.DATE_FORMAT_EXCEPTION,"日期格式化错误");
        }
    }

    public static long timediff(String time1,String time2)
    {
        Date d1=toData(time1);
        Date d2=toData(time2);
        long millSeconds=d1.getTime()-d2.getTime();
        return millSeconds;
    }
}