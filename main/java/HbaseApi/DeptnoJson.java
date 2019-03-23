package HbaseApi;

import net.sf.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeptnoJson {

    public static void main(String[] args) {

        //这是我们拿到的json格式
        String str = "{\"pid\":\"00000000003044\"}";

        /**{"1":{"zjf":10,"a":1,"b":9},
         "2",{"zjf":8,"a":2,b:"6"}
         }   ===>这样的格式可以看成Map<String,Map<String,Object>>
         **/

        /**
         * {"1":"a","2":"b"}  ===>这样的格式可以看成Map<String,String>
         */

        JSONObject jsonObject = JSONObject.fromObject(str);//这里将字符串放入数组中,拿到一个json的实例
        String pid = jsonObject.getString("pid");//通过实例拿到了获得pid的m由于是字符串类型则需要String来接受
        System.out.println(pid);//这里输出是 00000000003044

        //{"1":{"zjf":10,"a":1,"b":9},由array数组里面包括了一个map数组
        ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();//这个没用到

        //{"zjf":10,"a":1,"b":9}
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("CiIForceOcc24M_Exp_Count",1);// 放入键值对 kv
        stringObjectHashMap.put("CiISurrenderAndCancel_Exp_Count",2);

        JSONObject jsonObject1 = JSONObject.fromObject(stringObjectHashMap);//放进json看看结果
        System.out.println(jsonObject1);//{"CiIForceOcc24M_Exp_Count":1,"CiISurrenderAndCancel_Exp_Count":2}

        //{"1"{"CiIForceOcc24M_Exp_Count":1}}
        HashMap<String, HashMap<String, Object>> stringHashMapHashMap = new HashMap<>();
        stringHashMapHashMap.put("1",stringObjectHashMap);//将KV放进去

        //直接输出看看结果是什么东东,此时我们已经将值放好了
        System.out.println(JSONObject.fromObject(stringHashMapHashMap));//{"1":{"CiIForceOcc24M_Exp_Count":1,"CiISurrenderAndCancel_Exp_Count":2}}

     //=================================

        //按需求将值放入JSON后 在将Map转成Json发送给前端

        HashMap<String, HashMap<String, Object>> stable = new HashMap<String, HashMap<String, Object>>();

        HashMap<String, Object> sf = new HashMap<String, Object>();

    //  {"1":{"zjf":10,"a":1,"b":9}
        sf.put("a",1);
        sf.put("b",9);
        sf.put("zjf",10);
        stable.put("1",sf);


        HashMap<String, Object> sf1 = new HashMap<String, Object>();
      //  "2",{"zjf":8,"a":2,b:"6"}

        sf1.put("zjf",8);
        sf1.put("a",2);
        sf1.put("b",6);
        stable.put("2",sf1);

    }


}
