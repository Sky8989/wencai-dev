package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.CouPonPromoVO;
import com.sftc.web.model.vo.swaggerRequest.CouponRequestVO;

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
     *
     * @param body
     * @return
     */
    ApiResponse getUserCouponList(CouponRequestVO body) throws Exception;

    /**
     * 根据密语和token兑换优惠券
     *
     * @param body
     * @return
     */
    ApiResponse exchangeCoupon(CouPonPromoVO body) throws Exception;
}
