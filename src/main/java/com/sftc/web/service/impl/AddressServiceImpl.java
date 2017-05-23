package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.AIPPost;
import com.sftc.tools.api.APIResponse;

import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.AddressMapper;
import com.sftc.web.service.AddressService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 地址操作接口实现
 * @date 17/4/1
 * @Time 下午9:32
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private AddressMapper addressMapper;
    Gson gson = new Gson();
    private static String ADDADDRESS_URL = "http://api-dev.sf-rush.com/addresses";
    /**
     * 新增地址
     */
    @Override
    public APIResponse addAddress(Object object) {
        APIStatus status= APIStatus.SUCCESS;
        String str = gson.toJson(object);
        HttpPost post = new HttpPost(ADDADDRESS_URL);
        post.addHeader("PushEnvelope-Device-Token","97uAK7HQmDtsw5JMOqad");//97uAK7HQmDtsw5JMOqad
        String res = AIPPost.getPost(str, post);
        JSONObject jsonObject = JSONObject.fromObject(res);
        if(jsonObject.get("error")!=null){
            status = APIStatus.ADDRESS_FAIL;
        }
        return APIUtil.getResponse(status, jsonObject);
    }
}
