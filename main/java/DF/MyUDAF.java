package DF;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;

public class MyUDAF extends UDAF {

    //用内部类实现UDAF函数方法
    public static class tt implements UDAFEvaluator {
       //由于返回的是字符串所有定义一个全局变量的字符串（大多是结果的拼接都是字符串）
        String line = "";
        //每个阶段都会执行一次初始化
        public void init() {

            line = "";
        }

        // 这个是判断输入字符串a和b是否结束如果为null则证明没有后续的输入
        public boolean iterate(String a, String b) {
            //如果不为null 在将以a为key为聚合条件 聚合b
            if (a!= null||b!= null) {
                line += a + b;
                return true;
            }
            line+="";
            return true;
        }
        //这是最终的输出结果,返回一个字符串
        public String terminate()
        {
            return line;
        }
        //map阶段的输出结果
        public String terminatepartial()
        {

                return line;
        }
        //
        public boolean merge(String other)
        {
            return iterate(line,other);

        }
    }
}