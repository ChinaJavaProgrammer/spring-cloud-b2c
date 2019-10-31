package spring.cloud.product.util;

import com.alibaba.fastjson.JSONObject;

public class ResponseUtil {


    public static JSONObject successResult(Object object){
        JSONObject json = new JSONObject();
        json.put("code",200);
        json.put("message","执行成功");
        json.put("success",true);
        json.put("result",object);
        return json;
    }

    public static JSONObject falseResult(int code,String message){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("message",message);
        json.put("success",false);
        return json;
    }
}
