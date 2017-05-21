package com.sftc.tools.api;

import com.google.gson.Gson;
import com.sftc.web.model.Result;
import com.sftc.web.model.Token;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2017/5/19.
 */
//public class APIToken {
//    private static String GET_TOKEN = "http://api-dev.sf-rush.com/merchants/me/token";
//    static Gson gson = new Gson();
//    public static String getToken(Object object){
//        APIStatus status = APIStatus.SUCCESS;
//        String str = gson.toJson(object);
//        String result = AIPPost.getPost(str, GET_TOKEN);
//
////        if(result.getError()!=null){
////            status = APIStatus.ERROR;
////        }
//        JSONObject jsonObject = JSONObject.fromObject(result);
//        Result result1 = new Result();
//        result1 =  (Result)JSONObject.toBean(jsonObject,result.getClass());
//        System.out.println(result1.getToken().getAccess_token());
//        return result1.getToken().getAccess_token();
//    }
//}
