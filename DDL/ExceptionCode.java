package DDL;

public class ExceptionCode {

//异常类 被ValidUrlPrefixParser类调用
    //IO通用异常

    public static class IOCode{


        public static final Integer IO_EXCEPTION=1000;
    }
    //mr错误码
    public static class COMMON{

        //对象复制报错
        public static final Integer BEAN_ATTR_COPY_EXCEPTION=2000;
        //日期格式转换错误
        public static final Integer DATE_FORMAT_EXCEPTION =2001;


    }


}
