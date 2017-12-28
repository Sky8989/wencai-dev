package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.ApiPostUtil;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.tools.sf.SfTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.vo.swaggerOrderRequest.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.SfConstant.SF_QUOTES_URL;
import static com.sftc.tools.constant.SfConstant.SF_REQUEST_URL;

@Component
public class OrderPayLogic {

    private Gson gson = new Gson();

    @Resource
    private UserMapper userMapper;

    /**
     * 计价
     */
    public ApiResponse countPrice(PriceRequestVO priceRequestVO) {

        PriceRequest priceRequest = priceRequestVO.getRequest();

        // 处理商户uuid和access_token
        boolean isCommonMerchant = false;
        String uuid = TokenUtils.getInstance().getUserUUID();

        String access_token = TokenUtils.getInstance().getAccessToken();
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(access_token)) {
            // 下单时如果还没验证手机号，计价时uuid和token都没有，需要使用公共的商户。
            // 这里要提醒前端，验证完手机号后，要重新计价一次，因为顺丰服务端要求，计价和下单的商户必须是同一个。
            //（如果不重新计价，就会出现问题：用公共商户计价，用个人商户下单，从而出现`订单信息与报价不符合`的问题。）
            uuid = SfTokenHelper.COMMON_UUID;
            access_token = SfTokenHelper.COMMON_ACCESSTOKEN;
            isCommonMerchant = true;
        }
        OrderMerchantVO orderMerchantVO = new OrderMerchantVO();
        orderMerchantVO.setUuid(uuid);
        priceRequest.setMerchant(orderMerchantVO);

        // 同城下单参数增加 C端小程序标识和订单类型表示 NORMAL/RESERVED/DIRECTED
        priceRequest.setRequest_source("C_WX_APP");
        // 默认为普通
        priceRequest.setType("NORMAL");

        // 预约时间处理
        String reserveTime = priceRequest.getReserve_time();
        priceRequest.setReserve_time(null);
        if (StringUtils.isNotBlank(reserveTime)) {
            reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserveTime, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            priceRequest.setReserve_time(reserveTime);
            priceRequest.setType("RESERVED");
        }

        // 面对面参数处理，原本`DIRECTED`是放在request.attributes.source，现在放在request.type，这个改动暂时放在服务端
        AttributesVO attributesVO = priceRequest.getAttributes();

        if (attributesVO.getSource() != null && attributesVO.getSource().equals(CustomConstant.DIRECTED)) {
            priceRequest.setType(CustomConstant.DIRECTED);
        }
        attributesVO.setSource(null);


        // 计价
        HttpPost post = new HttpPost(SF_QUOTES_URL);
        post.addHeader("PushEnvelope-Device-Token", access_token);
        String res = ApiPostUtil.post(gson.toJson(priceRequestVO), post);
        JSONObject respObject = JSONObject.fromObject(res);

        // 处理计价失败结果
        if (respObject.get(CustomConstant.ERROR) != null) {
            String errorMessage = "计价失败";
            try {
                errorMessage = respObject.getJSONObject(CustomConstant.ERROR).getString("message");
            } catch (Exception e) {
                e.fillInStackTrace();
            }
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), errorMessage, respObject.get("error"));
        }

        // 公共token商户计价，优惠券返回空数组
        if (isCommonMerchant) {
            respObject.put("coupons", new JSONArray());
        }

        return ApiUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 支付订单
     */

    public ApiResponse payOrder(OrderPayVO orderPayVO) {

        String uuid = orderPayVO.getUuid();
        String accessToken = TokenUtils.getInstance().getAccessToken();

        String userUUid = TokenUtils.getInstance().getUserUUID();

        User user = userMapper.selectUserByUserUUId(userUUid);
        if (user == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "用户信息错误，未找到该用户");
        }
        String payUrl;
        if (orderPayVO.getCash() != null) {
            payUrl = SF_REQUEST_URL + "/" + uuid + "/js_pay?open_id=" + user.getOpenId() + "&cash=" + orderPayVO.getCash();
        } else {
            payUrl = SF_REQUEST_URL + "/" + uuid + "/js_pay?open_id=" + user.getOpenId();
        }
        HttpPost post = new HttpPost(payUrl);
        post.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = ApiPostUtil.post("", post);
        JSONObject resultObject = JSONObject.fromObject(res);
        if (resultObject.containsKey(CustomConstant.ERROR)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), resultObject.getJSONObject(CustomConstant.ERROR).getString("message"),
                    resultObject.getJSONObject(CustomConstant.ERROR));
        }

        return ApiUtil.getResponse(SUCCESS, resultObject);
    }
}
