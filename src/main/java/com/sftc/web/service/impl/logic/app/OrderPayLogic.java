package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_QUOTES_URL;
import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;

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
        JSONObject requestObject = jsonObject.getJSONObject("request");

        // 处理商户uuid和access_token
        String uuid = TokenUtils.getInstance().getUserUUID();
        String access_token = TokenUtils.getInstance().getAccess_token();
        if (StringUtils.isNotBlank(uuid) || StringUtils.isNotBlank(access_token)) {
            // 下单时如果还没验证手机号，计价时uuid和token都没有，需要使用公共的商户。
            // 这里要提醒前端，验证完手机号后，要重新计价一次，因为顺丰服务端要求，计价和下单的商户必须是同一个。
            //（如果不重新计价，就会出现问题：用公共商户计价，用个人商户下单，从而出现`订单信息与报价不符合`的问题。）
            uuid = SFTokenHelper.COMMON_UUID;
            access_token = SFTokenHelper.COMMON_ACCESSTOKEN;
        }
        JSONObject merchantObj = new JSONObject();
        merchantObj.put("uuid", uuid);
        merchantObj.put("access_token", access_token);
        requestObject.put("merchant", merchantObj);

        // 同城下单参数增加 C端小程序标识和订单类型表示 NORMAL/RESERVED/DIRECTED
        requestObject.put("request_source", "C_WX_APP");
        requestObject.put("type", "NORMAL"); // 默认为普通

        // 预约时间处理
        String reserve_time = requestObject.getString("reserve_time");
        requestObject.remove("reserve_time");
        if (reserve_time != null && !reserve_time.equals("")) {
            reserve_time = DateUtils.iSO8601DateWithTimeStampAndFormat(reserve_time, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            requestObject.put("reserve_time", reserve_time);
            requestObject.put("type", "RESERVED");
        }

        // 面对面参数处理，原本`DIRECTED`是放在request.attributes.source，现在放在request.type，这个改动暂时放在服务端
        JSONObject attributeOBJ = requestObject.getJSONObject("attributes");
        if (attributeOBJ.containsKey("source")) {
            if (attributeOBJ.getString("source") != null && attributeOBJ.getString("source").equals("DIRECTED")) {
                requestObject.put("type", "DIRECTED");
            }
            attributeOBJ.remove("source");
        }

        // 计价
        HttpPost post = new HttpPost(SF_QUOTES_URL);
        post.addHeader("PushEnvelope-Device-Token", access_token);
        String res = APIPostUtil.post(gson.toJson(jsonObject), post);
        JSONObject respObject = JSONObject.fromObject(res);

        // 处理计价失败结果
        if (respObject.get("error") != null) {
            String errorMessage = "计价失败";
            try {
                errorMessage = respObject.getJSONObject("error").getString("message");
            } catch (Exception e) {
                e.fillInStackTrace();
            }
            return APIUtil.submitErrorResponse(errorMessage, respObject.get("error"));
        }

        return APIUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 支付订单
     */
    public APIResponse payOrder(APIRequest request) {
        JSONObject jsonObject = JSONObject.fromObject(request.getRequestParam());

        String uuid = jsonObject.getString("uuid");
        if (uuid == null || uuid.equals(""))
            return APIUtil.paramErrorResponse("uuid参数缺失");

        String access_token = TokenUtils.getInstance().getAccess_token();

        int user_id = TokenUtils.getInstance().getUserId();

        User user = userMapper.selectUserByUserId(user_id);
        if (user == null) return APIUtil.selectErrorResponse("用户信息错误，未找到该用户", null);
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
