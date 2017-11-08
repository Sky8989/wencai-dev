package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.tools.common.DateUtils;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Date;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_QUOTES_URL;
import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;
import static com.sftc.tools.sf.SFTokenHelper.COMMON_ACCESSTOKEN;
import static com.sftc.tools.sf.SFTokenHelper.COMMON_UUID;

@Component
public class OrderPayLogic {

    private Gson gson = new Gson();

    @Resource
    private UserMapper userMapper;
    @Resource
    private TokenMapper tokenMapper;

    /**
     * 计价
     */
    public APIResponse countPrice(APIRequest request) {

        JSONObject jsonObject = JSONObject.fromObject(request.getRequestParam());
        HttpPost post = new HttpPost(SF_QUOTES_URL);
        JSONObject requestObject = jsonObject.getJSONObject("request");
        String uuid = (String) requestObject.getJSONObject("merchant").get("uuid");
        String access_token = (String) requestObject.getJSONObject("token").get("access_token");

        // 预约时间处理
        String reserve_time = (String) requestObject.getString("reserve_time");
//        if(reserve_time == null || reserve_time.equals("")){
//            Date date = new Date();
//            reserve_time = String.valueOf(date.getTime());
//        }

        //同城下单参数增加 C 端小程序标识和订单类型表示   NORMAL/RESERVED/DIRECTED
        requestObject.put("request_source", "C_WX_APP");
        requestObject.put("type", "NORMAL");    //默认为普通

        requestObject.remove("reserve_time");
        if (reserve_time != null && !reserve_time.equals("")) {
            reserve_time = DateUtils.iSO8601DateWithTimeStampAndFormat(reserve_time, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            requestObject.put("reserve_time", reserve_time);
            requestObject.remove("type");
            requestObject.put("type", "RESERVED");
        }

        JSONObject attributeOBJ = requestObject.getJSONObject("attributes");
        if (attributeOBJ.containsKey("source")) {
            if (attributeOBJ.getString("source") != null && attributeOBJ.getString("source").equals("DIRECTED")) {
                requestObject.remove("type");
                requestObject.put("type", "DIRECTED");
            }
            attributeOBJ.remove("source");
        }

        boolean uuidFlag = (uuid != null) && !(uuid.equals(""));
        boolean tokenFlag = (access_token != null) && !(access_token.equals(""));

        if ((uuidFlag && !tokenFlag) || (!uuidFlag && tokenFlag))
            return APIUtil.paramErrorResponse("uuid 和 access_token 不能只传一个");

        if (tokenFlag) {
            post.addHeader("PushEnvelope-Device-Token", access_token);
        } else {
            // 下单时，如果还没登录，计价时uuid和token都没有，需要先写死
            jsonObject.getJSONObject("request").getJSONObject("merchant").put("uuid", COMMON_UUID);
            post.addHeader("PushEnvelope-Device-Token", COMMON_ACCESSTOKEN);
        }

        String res = APIPostUtil.post(gson.toJson(jsonObject), post);
        JSONObject respObject = JSONObject.fromObject(res);

        if (respObject.get("error") != null) {
            return APIUtil.submitErrorResponse("计价失败", respObject);
        }

        return APIUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 支付订单
     */
    public APIResponse payOrder(APIRequest request) {
        JSONObject jsonObject = JSONObject.fromObject(request.getRequestParam());
        String token = jsonObject.getString("local_token");
        String uuid = jsonObject.getString("uuid");
        String access_token = jsonObject.getString("access_token");


        if (token == null || token.equals(""))
            return APIUtil.paramErrorResponse("token参数缺失");
        if (uuid == null || uuid.equals(""))
            return APIUtil.paramErrorResponse("uuid参数缺失");
        if (access_token == null || access_token.equals(""))
            return APIUtil.paramErrorResponse("access_token参数缺失");

        Token tokenPram = tokenMapper.selectUserIdByToken(token);
        if (tokenPram == null) return APIUtil.paramErrorResponse("token无效，库中无该token");


        User user = userMapper.selectUserByUserId(tokenPram.getUser_id());
        if(user == null) return APIUtil.selectErrorResponse("该token无对应的用户",null);
        String pay_url = SF_REQUEST_URL + "/" + uuid + "/js_pay?open_id=" + user.getOpen_id();
        HttpPost post = new HttpPost(pay_url);
        post.addHeader("PushEnvelope-Device-Token", access_token);
        String res = APIPostUtil.post("", post);
        JSONObject resultObject = JSONObject.fromObject(res);
        if (resultObject.containsKey("error")) {
            return APIUtil.submitErrorResponse("支付失败，请查看返回值", resultObject);
        }

        return APIUtil.getResponse(SUCCESS, resultObject);
    }
}
