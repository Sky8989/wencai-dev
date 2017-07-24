package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.sfmodel.Promo;
import com.sftc.web.service.CouponService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("coupons")
public class CouponController {

    @Resource
    private CouponService couponService;

    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getUserCouponList(@RequestBody UserParam userParam) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(userParam);
        return couponService.getUserCouponList(apiRequest);
    }

    @RequestMapping(value = "/user/exchange", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse exchangeCoupon(@RequestBody Promo promo) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(promo);
        return couponService.exchangeCoupon(apiRequest);
    }
}
