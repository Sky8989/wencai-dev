package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.constant.SFConstant;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.service.IndexService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SFEnvironment.*;

@Service
public class IndexServiceImpl implements IndexService {

    public APIResponse setupEnvironment(APIRequest request) {

        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());

        if (!requestObject.containsKey("environment"))
            return APIUtil.paramErrorResponse("参数environment不能为空");
        String environment = (String) requestObject.get("environment");
        if (environment == null || environment.equals(""))
            return APIUtil.paramErrorResponse("参数environment不能为空");

        if (environment.equals("product")) {
            SFConstant.setEnvironment(SFEnvironmentProduct);
        } else if (environment.equals("stage")) {
            SFConstant.setEnvironment(SFEnvironmentStage);
        } else if (environment.equals("dev")) {
            SFConstant.setEnvironment(SFEnvironmentDev);
        } else {
            return APIUtil.paramErrorResponse("参数environment不正确");
        }

        JSONObject respObject = new JSONObject();
        respObject.put("sftc_domain", SFConstant.getSfSameDomain());

        return APIUtil.getResponse(SUCCESS, respObject);
    }

    public APIResponse setupCommonToken(APIRequest request) {

        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        if (!requestObject.containsKey("access_token"))
            return APIUtil.paramErrorResponse("参数access_token不能为空");
        String access_token = (String) requestObject.get("access_token");
        if (access_token == null || access_token.equals(""))
            return APIUtil.paramErrorResponse("参数access_token不能为空");

        SFTokenHelper.COMMON_ACCESSTOKEN = access_token;

        JSONObject respObject = new JSONObject();
        respObject.put("access_token", access_token);
        return APIUtil.getResponse(SUCCESS, respObject);
    }
}
