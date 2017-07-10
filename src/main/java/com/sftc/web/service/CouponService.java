package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.sfmodel.Promo;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description: 顺丰优惠券接口操作
 * @date 2017/6/2
 * @Time 下午3:12
 */
public interface CouponService {

    /**
     * 根据用户查询优惠券
     * @param userParam
     * @return
     */
    APIResponse getUserCouponList(UserParam userParam);

    /**
     * 根据密语和token兑换优惠券
     * @param promo
     * @return
     */
    APIResponse exchangeCoupon(Promo promo);
}
