package DF;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;

public class MyUDTF extends UDAF {

        public static class ToString implements UDAFEvaluator {

            String line = "";  //因为我们最终的返回类型为String，所以我们自定义一个变量为String类型。
            public void init() {
                //初始化，会在每个阶段进行运行。
                line = "";
            }

            public boolean iterate(String a,String b){
                //每条数据的处理逻辑，map阶段逻辑，注意iterate的参数来自于我们的表字段。
                if(a!=null||b!=null){
                    line += a + b;
                    return true;
                }
//            line += "";
                return true;
            }

            public String terminate(){
                //最终的输出结果，这里返回的就是一个字符串
                return line;
            }

            public String terminatePartial(){
                //map阶段的输出结果
                return line;
            }

            public boolean merge(String other){
                return iterate(line,other); //a,b,c,d,e,f,g,h,
            }
        }

    }

