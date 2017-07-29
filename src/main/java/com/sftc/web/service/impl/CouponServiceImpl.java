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

import static com.sftc.tools.constant.SFConstant.SF_REGISTER_URL;

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
        return APIUtil.getResponse(status, JSONObject.fromObject(res));
    }

    /**
     * 根据密语和token兑换优惠券
     */
    public APIResponse exchangeCoupon(APIRequest apiRequest) {
        Promo promo = (Promo) apiRequest.getRequestParam();
        APIStatus status = APIStatus.SUCCESS;
        String COUPON_EXCHANGE_API = "http://api-dev.sf-rush.com/coupons?promo_code=";
        COUPON_EXCHANGE_API += promo.getPromo_code();
//        List<Coupon> couponList = null;
//        try {
//            couponList = APIResolve.getCouponsJson(COUPON_EXCHANGE_API, promo.getToken(), "POST");
//        } catch (Exception e) {
//            status = APIStatus.SELECT_FAIL;
//        }
//        return APIUtil.getResponse(status, couponList);
        // 调用顺丰接口
        HttpPost httpPost = new HttpPost(COUPON_EXCHANGE_API);
        httpPost.addHeader("PushEnvelope-Device-Token", promo.getToken());
        String res = APIPostUtil.post("", httpPost);
        return APIUtil.getResponse(status, JSONObject.fromObject(res));

    }
}
