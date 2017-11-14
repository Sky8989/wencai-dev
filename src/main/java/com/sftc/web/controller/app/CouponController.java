package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequestVO.Promo;
import com.sftc.web.model.vo.swaggerRequestVO.CouponRequestVO;
import com.sftc.web.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Api(description = "优惠券相关接口")
@RequestMapping("coupons")
public class CouponController {

    @Resource
    private CouponService couponService;

    @ApiOperation(value = "获取用户优惠券列表",httpMethod = "POST")
    @RequestMapping(value="/list",method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getUserCouponList(@RequestBody CouponRequestVO couponRequestVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(couponRequestVO);
        return couponService.getUserCouponList(apiRequest);
    }

    @ApiOperation(value = "兑换优惠券",httpMethod = "POST")
    @RequestMapping(value="/exchange",method = RequestMethod.POST)
    public @ResponseBody
    APIResponse exchangeCoupon(@RequestBody Promo promo) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(promo);
        return couponService.exchangeCoupon(apiRequest);
    }



}
