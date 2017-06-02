package com.sftc.web.controller.api;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.sfmodel.Promo;
import com.sftc.web.service.CouponService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description: 用户优惠券控制器
 * @date 2017/6/2
 * @Time 下午3:17
 */
@Controller
@RequestMapping("coupons")
public class CouponController {

    @Resource
    private CouponService couponService;

    @RequestMapping(value = "/user/list", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse getUserCouponList(@RequestBody UserParam userParam) throws Exception {
        return couponService.getUserCouponList(userParam);
    }

    @RequestMapping(value = "/user/exchange", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse exchangeCoupon(@RequestBody Promo promo) throws Exception {
        return couponService.exchangeCoupon(promo);
    }
}
