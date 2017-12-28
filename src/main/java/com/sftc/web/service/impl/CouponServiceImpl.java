package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.model.vo.swaggerRequest.CouPonPromoVO;
import com.sftc.web.model.vo.swaggerRequest.CouponRequestVO;
import com.sftc.web.service.CouponService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sftc.tools.constant.SfConstant.SF_COUPON_EXCHANGE_API;
import static com.sftc.tools.constant.SfConstant.SF_COUPON_LIST_URL;
import static com.sftc.tools.sf.SfResultHelper.*;

/**
 * @author Administrator
 */
@Service("couponService")
public class CouponServiceImpl implements CouponService {

    //    private String status_valid = "INIT,ACTIVE";
    //    private String status_invalid = "DISABLED,USED";
    /**
     * 所有优惠券的状态
     */
    private static final String STATUS_ALL = "INIT,ACTIVE,DISABLED,USED";

    /**
     * 根据用户查询优惠券
     * <p>
     * 顺丰的优惠券有 `INIT,ACTIVE,DISABLED,USED` 四种状态，本可以直接有效为 `INIT,ACTIVE`，无效为 `DISABLED,USED`，直接作为条件参数传给顺丰的接口。
     * 但这里有个问题，`INIT,ACTIVE` 中包含了已过期的优惠券。也就是虽然状态为 `INIT,ACTIVE`，但仍然有可能是过期的，没有及时变为`DISABLED`状态。
     * 所以，这里直接获取全部的优惠券，再手动分组。
     * <有效> ：(`INIT,ACTIVE` && 未过期)
     * <无效> ：(`DISABLED,USED` || (`INIT,ACTIVE` && 已过期))
     */
    @Override
    public ApiResponse getUserCouponList(CouponRequestVO couponRequestVO) throws Exception {

        if (StringUtils.isEmpty(couponRequestVO.getStatus())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Parameter missing status");
        }
        String uuid = TokenUtils.getInstance().getUserUUID();
        String limit = String.valueOf(1000);
        String offset = String.valueOf(0);
        String status = couponRequestVO.getStatus();

        String couponListUrl = SF_COUPON_LIST_URL.
                replace("{user_uuid}", uuid).
                replace("{status}", STATUS_ALL).
                replace("{limit}", limit).
                replace("{offset}", offset);
        // 调用顺丰接口
        HttpGet httpGet = new HttpGet(couponListUrl);
        httpGet.addHeader("PushEnvelope-Device-Token", TokenUtils.getInstance().getAccessToken());
        String res = ApiGetUtil.get(httpGet);

        // 处理错误信息
        JSONObject resJSONObject = JSONObject.fromObject(res);
        if (resJSONObject.containsKey(ERROR_STRING_1) || resJSONObject.containsKey(ERROR_STRING_2)
                || resJSONObject.containsKey(ERROR_STRING_3) || resJSONObject.containsKey(ERROR_STRING_4)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "查询失败", resJSONObject);
        }

        JSONObject finalRes = resolveCouponResult(status, resJSONObject);
        LoggerFactory.getLogger(CouponServiceImpl.class.getName()).info(finalRes.toString());
        return ApiUtil.getResponse(ApiStatus.SUCCESS, finalRes);
    }

    /**
     * 根据密语和token兑换优惠券
     */
    @Override
    public ApiResponse exchangeCoupon(CouPonPromoVO couPonPromoVO) throws Exception {
        // 过滤参数 处理参数
        if (couPonPromoVO.getPromo_code() == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "请输入密语");
        }
        String emptyStr = " ";
        if ("".equals(couPonPromoVO.getPromo_code()) || couPonPromoVO.getPromo_code().contains(emptyStr)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Don't input '' or ' ' ");
        }

        String promoCode = URLEncoder.encode(couPonPromoVO.getPromo_code(), "UTF-8");
        String couponExchangeUrl = SF_COUPON_EXCHANGE_API.replace("{promo_code}", promoCode);

        // 调用顺丰接口
        HttpPost httpPost = new HttpPost(couponExchangeUrl);
        httpPost.addHeader("PushEnvelope-Device-Token", TokenUtils.getInstance().getAccessToken());
        String res = ApiPostUtil.post("", httpPost);

        // 处理错误信息
        JSONObject resJSONObject = JSONObject.fromObject(res);
        if (resJSONObject.containsKey(ERROR_STRING_1) || resJSONObject.containsKey(ERROR_STRING_2)
                || resJSONObject.containsKey(ERROR_STRING_3) || resJSONObject.containsKey(ERROR_STRING_4)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "兑换失败：" + resJSONObject.getJSONObject("error").
                    getString("message"), resJSONObject);
        }

        return ApiUtil.getResponse(ApiStatus.SUCCESS, resJSONObject);
    }

    private JSONObject resolveCouponResult(String statusTemp, JSONObject resJSONObject) throws ParseException {
        String valid = "valid";
        String invalid = "invalid";
        if (statusTemp.equals(valid)) {
            return filterOvertimeCoupon(resJSONObject);
        }
        if (statusTemp.equals(invalid)) {
            return getOvertimeCoupon(resJSONObject);
        }
        // 如果没有要求字段 则返回所有优惠券 STATUS_ALL 原样返回
        return resJSONObject;
    }

    /**
     * 获取可用的优惠券
     */
    private JSONObject filterOvertimeCoupon(JSONObject resJSONObject) throws ParseException {

        JSONArray oldcoupons = resJSONObject.getJSONArray("coupons");
        if (oldcoupons == null) {
            return resJSONObject;
        }
        if (oldcoupons.size() == 0) {
            return resJSONObject;
        }

        JSONArray newcoupons = new JSONArray();
        for (int i = 0; i < oldcoupons.size(); i++) {
            JSONObject singleCoupon = oldcoupons.getJSONObject(i);
            if (singleCoupon.containsKey("status")) {
                // 可用
                if ("INIT".equals(singleCoupon.getString("status")) || "ACTIVE".equals(singleCoupon.getString("status"))) {
                    // 需要排除过期的优惠券
                    String expireTime = singleCoupon.getString("expire_time");
                    if (dateToStamp(expireTime) > System.currentTimeMillis()) {
                        newcoupons.add(oldcoupons.get(i));
                    }
                }
            }
        }

        resJSONObject.remove("coupons");
        resJSONObject.put("coupons", newcoupons);
        return resJSONObject;
    }

    /**
     * 获取不可用的优惠券
     */
    private JSONObject getOvertimeCoupon(JSONObject resJSONObject) throws ParseException {

        JSONArray oldcoupons = resJSONObject.getJSONArray("coupons");
        if (oldcoupons == null) {
            return resJSONObject;
        }
        if (oldcoupons.size() == 0) {
            return resJSONObject;
        }

        JSONArray newcoupons = new JSONArray();
        for (int i = 0; i < oldcoupons.size(); i++) {
            JSONObject singleCoupon = oldcoupons.getJSONObject(i);
            if (singleCoupon.containsKey("status")) {
                // 不可用
                if ("DISABLED".equals(singleCoupon.getString("status")) || "USED".equals(singleCoupon.getString("status"))) {
                    newcoupons.add(oldcoupons.get(i));
                } else { // 可用，但已过期
                    String expireTime = singleCoupon.getString("expire_time");
                    if (dateToStamp(expireTime) < System.currentTimeMillis()) {
                        newcoupons.add(oldcoupons.get(i));
                    }
                }
            }
        }

        resJSONObject.remove("coupons");
        resJSONObject.put("coupons", newcoupons);
        return resJSONObject;
    }

    /**
     * 将时间转换为时间戳
     */
    private long dateToStamp(String waitFormatPram) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        Date date = simpleDateFormat.parse(waitFormatPram);

        return date.getTime();
    }
}
