package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.model.vo.swaggerRequest.CouPonPromoVO;
import com.sftc.web.service.CouponService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sftc.tools.constant.SFConstant.SF_COUPON_EXCHANGE_API;
import static com.sftc.tools.constant.SFConstant.SF_COUPON_LIST_URL;
import static com.sftc.tools.sf.SFResultHelper.*;

@Service("couponService")
public class CouponServiceImpl implements CouponService {

    //    private String status_valid = "INIT,ACTIVE";
    //    private String status_invalid = "DISABLED,USED";
    private static final String status_all = "INIT,ACTIVE,DISABLED,USED";

    /**
     * 根据用户查询优惠券
     * <p>
     * 顺丰的优惠券有 `INIT,ACTIVE,DISABLED,USED` 四种状态，本可以直接有效为 `INIT,ACTIVE`，无效为 `DISABLED,USED`，直接作为条件参数传给顺丰的接口。
     * 但这里有个问题，`INIT,ACTIVE` 中包含了已过期的优惠券。也就是虽然状态为 `INIT,ACTIVE`，但仍然有可能是过期的，没有及时变为`DISABLED`状态。
     * 所以，这里直接获取全部的优惠券，再手动分组。
     * <有效> ：(`INIT,ACTIVE` && 未过期)
     * <无效> ：(`DISABLED,USED` || (`INIT,ACTIVE` && 已过期))
     */
    public APIResponse getUserCouponList(APIRequest apiRequest) throws Exception {

        JSONObject paramOBJ = JSONObject.fromObject(apiRequest.getRequestParam());
        if (!paramOBJ.containsKey("uuid")) return APIUtil.paramErrorResponse("Parameter missing uuid");
        if (!paramOBJ.containsKey("status")) return APIUtil.paramErrorResponse("Parameter missing status");

        String uuid = paramOBJ.getString("uuid");
        String limit = String.valueOf(500);
        String offset = paramOBJ.containsKey("offset") ? String.valueOf(paramOBJ.getInt("offset") - 1) : String.valueOf(0);
        String status = paramOBJ.getString("status");

        String coupon_list_url = SF_COUPON_LIST_URL.
                replace("{user_uuid}", uuid).
                replace("{status}", status_all).
                replace("{limit}", limit).
                replace("{offset}", offset);
        // 调用顺丰接口
        HttpGet httpGet = new HttpGet(coupon_list_url);
        httpGet.addHeader("PushEnvelope-Device-Token", TokenUtils.getInstance().getAccess_token());
        String res = APIGetUtil.get(httpGet);

        // 处理错误信息
        JSONObject resJSONObject = JSONObject.fromObject(res);
        if (resJSONObject.containsKey(ERROR_STRING_1) || resJSONObject.containsKey(ERROR_STRING_2)
                || resJSONObject.containsKey(ERROR_STRING_3) || resJSONObject.containsKey(ERROR_STRING_4)) {
            return APIUtil.submitErrorResponse("查询失败", resJSONObject);
        }

        JSONObject final_res = resolveCouponResult(status, resJSONObject);
        return APIUtil.getResponse(APIStatus.SUCCESS, final_res);
    }

    /**
     * 根据密语和token兑换优惠券
     */
    public APIResponse exchangeCoupon(APIRequest apiRequest) throws Exception {
        CouPonPromoVO couPonPromoVO = (CouPonPromoVO) apiRequest.getRequestParam();
        // 过滤参数 处理参数
        if (couPonPromoVO.getPromo_code() == null)
            return APIUtil.paramErrorResponse("请输入密语");
        if ("".equals(couPonPromoVO.getPromo_code()) || couPonPromoVO.getPromo_code().contains(" "))
            return APIUtil.paramErrorResponse("Don't input '' or ' ' ");

        String promo_code = URLEncoder.encode(couPonPromoVO.getPromo_code(), "UTF-8");
        String coupon_exchange_url = SF_COUPON_EXCHANGE_API.replace("{promo_code}", promo_code);

        // 调用顺丰接口
        HttpPost httpPost = new HttpPost(coupon_exchange_url);
        httpPost.addHeader("PushEnvelope-Device-Token", TokenUtils.getInstance().getAccess_token());
        String res = APIPostUtil.post("", httpPost);

        // 处理错误信息
        JSONObject resJSONObject = JSONObject.fromObject(res);
        if (resJSONObject.containsKey(ERROR_STRING_1) || resJSONObject.containsKey(ERROR_STRING_2)
                || resJSONObject.containsKey(ERROR_STRING_3) || resJSONObject.containsKey(ERROR_STRING_4)) {
            return APIUtil.submitErrorResponse("兑换失败：" + resJSONObject.getJSONObject("error").getString("message"), resJSONObject);
        }

        return APIUtil.getResponse(APIStatus.SUCCESS, resJSONObject);
    }

    private JSONObject resolveCouponResult(String status_temp, JSONObject resJSONObject) throws ParseException {

        if (status_temp.equals("valid")) return filterOvertimeCoupon(resJSONObject);
        if (status_temp.equals("invalid")) return getOvertimeCoupon(resJSONObject);
        // 如果没有要求字段 则返回所有优惠券 status_all 原样返回
        return resJSONObject;
    }

    // 获取可用的优惠券
    private JSONObject filterOvertimeCoupon(JSONObject resJSONObject) throws ParseException {

        JSONArray oldcoupons = resJSONObject.getJSONArray("coupons");
        if (oldcoupons == null) return resJSONObject;
        if (oldcoupons.size() == 0) return resJSONObject;

        JSONArray newcoupons = new JSONArray();
        for (int i = 0; i < oldcoupons.size(); i++) {
            JSONObject singleCoupon = oldcoupons.getJSONObject(i);
            if (singleCoupon.containsKey("status")) {
                if (singleCoupon.getString("status").equals("INIT") || singleCoupon.getString("status").equals("ACTIVE")) { // 可用
                    // 需要排除过期的优惠券
                    String expire_time = singleCoupon.getString("expire_time");
                    if (dateToStamp(expire_time) > System.currentTimeMillis()) {
                        newcoupons.add(oldcoupons.get(i));
                    }
                }
            }
        }

        resJSONObject.remove("coupons");
        resJSONObject.put("coupons", newcoupons);
        return resJSONObject;
    }

    // 获取不可用的优惠券
    private JSONObject getOvertimeCoupon(JSONObject resJSONObject) throws ParseException {

        JSONArray oldcoupons = resJSONObject.getJSONArray("coupons");
        if (oldcoupons == null) return resJSONObject;
        if (oldcoupons.size() == 0) return resJSONObject;

        JSONArray newcoupons = new JSONArray();
        for (int i = 0; i < oldcoupons.size(); i++) {
            JSONObject singleCoupon = oldcoupons.getJSONObject(i);
            if (singleCoupon.containsKey("status")) {
                if (singleCoupon.getString("status").equals("DISABLED") || singleCoupon.getString("status").equals("USED")) { // 不可用
                    newcoupons.add(oldcoupons.get(i));
                } else { // 可用，但已过期
                    String expire_time = singleCoupon.getString("expire_time");
                    if (dateToStamp(expire_time) < System.currentTimeMillis()) {
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
