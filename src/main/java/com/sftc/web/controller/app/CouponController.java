package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.model.sfmodel.Promo;
import com.sftc.web.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Api(description = "优惠券")
@RequestMapping("coupons")
public class CouponController {

    @Resource
    private CouponService couponService;

    @ApiOperation(value = "获取用户优惠券列表",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid",value = "用户uuid",paramType = "query",defaultValue = "2c9a85895ddfeba0015de3dac77f1b9a"),
            @ApiImplicitParam(name = "token",value = "用户的access_token",paramType = "query",defaultValue = "dNjTzJTWAkKzPdZsVBNi"),
            @ApiImplicitParam(name = "status",value = "优惠券状态",paramType = "query",defaultValue = "all")
    })
    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getUserCouponList(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return couponService.getUserCouponList(apiRequest);
    }

    @ApiOperation(value = "兑换优惠券")
    @RequestMapping(value = "/user/exchange", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse exchangeCoupon(@RequestBody Promo promo) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(promo);
        return couponService.exchangeCoupon(apiRequest);
    }



}
