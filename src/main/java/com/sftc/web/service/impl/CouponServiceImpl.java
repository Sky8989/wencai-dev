package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.sfmodel.Coupon;
import com.sftc.web.model.sfmodel.Promo;
import com.sftc.web.service.CouponService;
import net.sf.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sftc.tools.constant.SFConstant.SF_LOGIN;
import static com.sftc.tools.constant.SFConstant.SF_REGISTER_URL;
import static com.sftc.tools.sf.SFResultHelper.*;

@Service("couponService")
public class CouponServiceImpl implements CouponService {

    /**
     * 根据用户查询优惠券
     */
    public APIResponse getUserCouponList(APIRequest apiRequest) throws Exception {
        String status_valid = "INIT,ACTIVE";
        String status_invalid = "DISABLED,USED";
        String status_all = "INIT,ACTIVE,DISABLED,USED";


        APIStatus status = APIStatus.SUCCESS;
        JSONObject paramOBJ = JSONObject.fromObject(apiRequest.getRequestParam());
        if (!paramOBJ.containsKey("uuid")) return APIUtil.paramErrorResponse("Parameter missing uuid");
        if (!paramOBJ.containsKey("token")) return APIUtil.paramErrorResponse("Parameter missing token");
        if (!paramOBJ.containsKey("status")) return APIUtil.paramErrorResponse("Parameter missing token");

        String uuid = paramOBJ.getString("uuid");
        String token = paramOBJ.getString("token");

        //limit offset是可选项
        String limit = paramOBJ.containsKey("limit") ? paramOBJ.getString("limit") : String.valueOf(20);
        String offset = paramOBJ.containsKey("offset") ? String.valueOf(paramOBJ.getInt("offset") - 1) : String.valueOf(0);

        //处理范围：有效/无效
        String status_temp = paramOBJ.getString("status");
        String status_final;
        if (status_temp.equals("valid")) {
            status_final = status_valid;
        } else if (status_temp.equals("invalid")) {
            status_final = status_invalid;
        } else {
            status_final = status_all;
        }

        String COUPON_LIST_API = "http://api-dev.sf-rush.com/coupons/by_user/user_uuid?status=" + status_final + "&limit=" + limit + "&offset=" + offset;
        COUPON_LIST_API = COUPON_LIST_API.replace("user_uuid", uuid + "");
        // 调用顺丰接口
        HttpGet httpGet = new HttpGet(COUPON_LIST_API);
        httpGet.addHeader("PushEnvelope-Device-Token", token);
        String res = APIGetUtil.get(httpGet);

        //处理错误信息
        JSONObject resJSONObject = JSONObject.fromObject(res);
        if (resJSONObject.containsKey(ERROR_STRING_1) || resJSONObject.containsKey(ERROR_STRING_2)
                || resJSONObject.containsKey(ERROR_STRING_3) || resJSONObject.containsKey(ERROR_STRING_4)) {
            return APIUtil.submitErrorResponse("查询失败", resJSONObject);
        }

        return APIUtil.getResponse(status, resJSONObject);
    }

    /**
     * 根据密语和token兑换优惠券
     */
    public APIResponse exchangeCoupon(APIRequest apiRequest) throws Exception {
        Promo promo = (Promo) apiRequest.getRequestParam();
        //过滤参数 处理参数
        if ("".equals(promo.getPromo_code()) || promo.getPromo_code().contains(" "))
            return APIUtil.paramErrorResponse("Don't input '' or ' ' ");
        APIStatus status = APIStatus.SUCCESS;
        String COUPON_EXCHANGE_API = "http://api-dev.sf-rush.com/coupons?promo_code=";
        COUPON_EXCHANGE_API += promo.getPromo_code();

        //更新商户信息
        APIResponse apiResponse = updateMerchantAddress(promo.getToken());
        if (apiResponse != null) return apiResponse;


        // 调用顺丰接口
        HttpPost httpPost = new HttpPost(COUPON_EXCHANGE_API);
        httpPost.addHeader("PushEnvelope-Device-Token", promo.getToken());
        String res = APIPostUtil.post("", httpPost);

        //处理错误信息
        JSONObject resJSONObject = JSONObject.fromObject(res);
        if (resJSONObject.containsKey(ERROR_STRING_1) || resJSONObject.containsKey(ERROR_STRING_2)
                || resJSONObject.containsKey(ERROR_STRING_3) || resJSONObject.containsKey(ERROR_STRING_4)) {
            return APIUtil.submitErrorResponse("兑换失败", resJSONObject);
        }

        return APIUtil.getResponse(status, resJSONObject);

    }

    ////////////////////处理用户无初试住址值的问题////////////////////
    ////////////////////暂时放在兑换优惠券的逻辑中，后期应将此逻辑放到注册或者登陆的逻辑中////////////////////
    private APIResponse updateMerchantAddress(String access_token) throws Exception {
        String json = "{\"merchant\":{\"name\":\"new_name\",\"attributes\":{},\"summary\":{},\"email\":\"123@gmail.com\",\"address\":{\"type\":\"LIVE\",\"country\":\"中国\",\"province\":\"广东\",\"city\":\"深圳\",\"region\":\"南山区\",\"street\":\"深圳市南山区文心五路海岸城五楼周大福\",\"zipcode\":\"518000\",\"receiver\":\"兑换优惠券\",\"mobile\":\"13632383955\",\"marks\":{},\"longitude\":113.942215,\"latitude\":22.52261}}}";

        RequestBody rb = RequestBody.create(null, json);
        Request request = new Request.Builder().
                url(SF_LOGIN).
                addHeader("Content-Type", "application/json").
                addHeader("PushEnvelope-Device-Token", access_token)
                .put(rb).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.code() == 200) return null;//正常情况返回null
        return APIUtil.logicErrorResponse("更新商户信息失败", response.body());
    }

}
