package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.sfmodel.Coupon;
import com.sftc.web.model.sfmodel.Promo;
import com.sftc.web.service.CouponService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sftc.tools.constant.SFConstant.SF_REGISTER_URL;
import static com.sftc.tools.sf.SFResultHelper.*;

@Service("couponService")
public class CouponServiceImpl implements CouponService {

    /**
     * 根据用户查询优惠券
     */
    public APIResponse getUserCouponList(APIRequest apiRequest) {
        APIStatus status = APIStatus.SUCCESS;
        UserParam userParam = (UserParam) apiRequest.getRequestParam();

        String COUPON_LIST_API = "http://api-dev.sf-rush.com/coupons/by_user/user_uuid?status=INIT,ACTIVE,DISABLED,USED&limit=20&offset=0";
        COUPON_LIST_API = COUPON_LIST_API.replace("user_uuid", userParam.getUuid() + "");
        // 调用顺丰接口
        HttpGet httpGet = new HttpGet(COUPON_LIST_API);
        httpGet.addHeader("PushEnvelope-Device-Token", userParam.getToken());
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
    public APIResponse exchangeCoupon(APIRequest apiRequest) {
        Promo promo = (Promo) apiRequest.getRequestParam();


        if ("".equals(promo.getPromo_code()) || promo.getPromo_code().contains(" "))
            return APIUtil.paramErrorResponse("Don't input '' or ' ' ");
        APIStatus status = APIStatus.SUCCESS;
        String COUPON_EXCHANGE_API = "http://api-dev.sf-rush.com/coupons?promo_code=";
        COUPON_EXCHANGE_API += promo.getPromo_code();

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
}
