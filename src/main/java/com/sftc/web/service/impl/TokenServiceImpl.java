package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.service.TokenService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {


    /**
     * token机制的中控器
     * @param apiRequest
     * @return
     * @throws Exception
     */
    public APIResponse token(APIRequest apiRequest) throws Exception {
        JSONObject paramOBJ = JSONObject.fromObject(apiRequest.getRequestParam());
        ///当有参数system_refresh_token时，提供token刷新服务
        if (paramOBJ.containsKey("system_refresh_token")){
            if (!"".equals(paramOBJ.getString("system_refresh_token"))){
                return refreshToken(paramOBJ.getString("system_refresh_token"));
            }else {
                return APIUtil.paramErrorResponse("system_refresh_token can't been ''");
            }

        }




        return null;
    }

    //////////////////////////private methods//////////////////////////

    /**
     * 通过微信登陆来获取token
     * @return APIResponse
     */
    private APIResponse WXGetToken(){
        return null;
    }


    /**
     * 通过手机号加验证码来获取token
     * @return APIResponse
     */
    private APIResponse mobileGetToken(){

        return null;
    }

    /**
     * 刷新token的入口
     * @return APIResponse
     */
    private APIResponse refreshToken(String system_refresh_token){
        System.out.println("-   -system_refresh_token:"+system_refresh_token);

        return APIUtil.getResponse(APIStatus.SUCCESS,null);
    }
    //////////////////////////Task methods//////////////////////////

    //一个定时刷新token的调度任务

}
