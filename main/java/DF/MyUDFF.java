package DF;

import org.apache.hadoop.hive.ql.exec.UDF;

public class MyUDFF extends UDF {

        public String evaluate(String a,String b)
        {
            return a+b;
        }

}
