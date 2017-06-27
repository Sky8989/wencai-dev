package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.sfmodel.Coupon;
import com.sftc.web.model.sfmodel.Promo;
import com.sftc.web.service.CouponService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("couponService")
public class CouponServiceImpl implements CouponService {

    /**
     * 根据用户查询优惠券
     */
    public APIResponse getUserCouponList(UserParam userParam) {
        APIStatus status = APIStatus.SUCCESS;
        String COUPON_LIST_API = "http://api-dev.sf-rush.com/coupons/by_user/user_uuid?status=INIT,ACTIVE,DISABLED,USED&limit=20&offset=0";
        COUPON_LIST_API = COUPON_LIST_API.replace("user_uuid", userParam.getId() + "");
        List<Coupon> couponList = null;
        try {
            couponList = APIResolve.getCouponsJson(COUPON_LIST_API, userParam.getToken(), "GET");
        } catch (Exception e) {
            status = APIStatus.SELECT_FAIL;
        }
        return APIUtil.getResponse(status, couponList);
    }

    /**
     * 根据密语和token兑换优惠券
     */
    public APIResponse exchangeCoupon(Promo promo) {
        APIStatus status = APIStatus.SUCCESS;
        String COUPON_EXCHANGE_API = "http://api-dev.sf-rush.com/coupons/coupons?promo_code=";
        COUPON_EXCHANGE_API += promo.getPromo_code();
        List<Coupon> couponList = null;
        try {
            couponList = APIResolve.getCouponsJson(COUPON_EXCHANGE_API, promo.getToken(), "GET");
        } catch (Exception e) {
            status = APIStatus.SELECT_FAIL;
        }
        return APIUtil.getResponse(status, couponList);
    }
}
