package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.CouPonPromoVO;
import com.sftc.web.model.vo.swaggerRequest.CouponRequestVO;
import com.sftc.web.model.vo.swaggerResponse.CouponsListVO;
import com.sftc.web.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@Api(description = "优惠券相关接口")
@RequestMapping("coupons")
public class CouponController {

    @Resource
    private CouponService couponService;

    @ApiOperation(value = "获取用户优惠券列表",httpMethod = "POST")
    @RequestMapping(value="/list",method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse getUserCouponList(@RequestBody CouponRequestVO body) throws Exception {
        return couponService.getUserCouponList(body);
    }

    @ApiOperation(value = "兑换优惠券",httpMethod = "POST")
    @RequestMapping(value="/exchange",method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse exchangeCoupon(@RequestBody CouPonPromoVO body) throws Exception {
        return couponService.exchangeCoupon(body);
    }

}
