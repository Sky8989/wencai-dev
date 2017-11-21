package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.model.sfmodel.Promo;
import com.sftc.web.service.CouponService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sftc.tools.constant.SFConstant.*;
import static com.sftc.tools.sf.SFResultHelper.*;

@Service("couponService")
public class CouponServiceImpl implements CouponService {

    private String status_valid = "INIT,ACTIVE";
    private String status_invalid = "DISABLED,USED";
    private String status_all = "INIT,ACTIVE,DISABLED,USED";

    /**
     * 根据用户查询优惠券
     */
    public APIResponse getUserCouponList(APIRequest apiRequest) throws Exception {

        JSONObject paramOBJ = JSONObject.fromObject(apiRequest.getRequestParam());
        if (!paramOBJ.containsKey("uuid")) return APIUtil.paramErrorResponse("Parameter missing uuid");
        if (!paramOBJ.containsKey("token")) return APIUtil.paramErrorResponse("Parameter missing token");
        if (!paramOBJ.containsKey("status")) return APIUtil.paramErrorResponse("Parameter missing token");

        String uuid = paramOBJ.getString("uuid");
        String token = paramOBJ.getString("token");

        //limit offset是可选项
//        String limit = paramOBJ.containsKey("limit") ? paramOBJ.getString("limit") : String.valueOf(20);
        String limit = String.valueOf(500);
        String offset = paramOBJ.containsKey("offset") ? String.valueOf(paramOBJ.getInt("offset") - 1) : String.valueOf(0);

        //处理范围：有效/无效
        String status = paramOBJ.getString("status");
//        String status_final;
//        if (status_temp.equals("valid")) {
//            status_final = status_valid;
//        } else if (status_temp.equals("invalid")) {
//            status_final = status_invalid;
//        } else {
//            status_final = status_all;
//        }
        String status_final = status_all;

        String coupon_list_url = SF_COUPON_LIST_URL.
                replace("{user_uuid}", uuid).
                replace("{status}", status_final).
                replace("{limit}", limit).
                replace("{offset}", offset);
        // 调用顺丰接口
        HttpGet httpGet = new HttpGet(coupon_list_url);
        httpGet.addHeader("PushEnvelope-Device-Token", token);
        String res = APIGetUtil.get(httpGet);

        //处理错误信息
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
        Promo promo = (Promo) apiRequest.getRequestParam();
        //过滤参数 处理参数
        if ("".equals(promo.getPromo_code()) || promo.getPromo_code().contains(" "))
            return APIUtil.paramErrorResponse("Don't input '' or ' ' ");
        APIStatus status = APIStatus.SUCCESS;
        String coupon_exchange_url = SF_COUPON_EXCHANGE_API.replace("{promo_code}", promo.getPromo_code());

//        //更新商户信息
//        APIResponse apiResponse = updateMerchantAddress(promo.getToken());
//        if (apiResponse != null) return apiResponse;

        // 调用顺丰接口
        HttpPost httpPost = new HttpPost(coupon_exchange_url);
        httpPost.addHeader("PushEnvelope-Device-Token", promo.getToken());
        String res = APIPostUtil.post("", httpPost);

        //处理错误信息
        JSONObject resJSONObject = JSONObject.fromObject(res);
        if (resJSONObject.containsKey(ERROR_STRING_1) || resJSONObject.containsKey(ERROR_STRING_2)
                || resJSONObject.containsKey(ERROR_STRING_3) || resJSONObject.containsKey(ERROR_STRING_4)) {
            return APIUtil.submitErrorResponse("兑换失败：" + resJSONObject.getJSONObject("error").getString("message"), resJSONObject);
        }

        return APIUtil.getResponse(status, resJSONObject);

    }

//    private APIResponse updateMerchantAddress(String access_token) throws Exception {
//        String json = "{\"merchant\":{\"name\":\"new_name\",\"attributes\":{},\"summary\":{},\"" +
//                "email\":\"123@gmail.com\",\"address\":{\"type\":\"LIVE\",\"country\":\"中国\",\"province\":\"广东\",\"" +
//                "city\":\"深圳\",\"region\":\"南山区\",\"street\":\"深圳市南山区文心五路海岸城五楼周大福\",\"zipcode\":\"518000\",\"receiver\":" +
//                "\"兑换优惠券\",\"mobile\":\"13632383955\",\"marks\":{},\"longitude\":113.942215,\"latitude\":22.52261}}}";
//        RequestBody rb = RequestBody.create(null, json);
//        Request request = new Request.Builder().
//                url(SF_LOGIN).
//                addHeader("Content-Type", "application/json").
//                addHeader("PushEnvelope-Device-Token", access_token)
//                .put(rb).build();
//        OkHttpClient client = new OkHttpClient();
//        okhttp3.Response response = client.newCall(request).execute();
//        if (response.code() == 200) return null;//正常情况返回null
//        return APIUtil.logicErrorResponse("更新商户信息失败", response.body());
//    }

    private JSONObject resolveCouponResult(String status_temp, JSONObject resJSONObject) throws ParseException {

        if (status_temp.equals("valid")) return filterOvertimeCoupon(resJSONObject);
        if (status_temp.equals("invalid")) return getOvertimeCoupon(resJSONObject);
        //如果没有要求字段 则返回所有优惠券status_all  原样返回
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
                    // 过期时间小于当前时间  代表已过去
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
            if (singleCoupon.containsKey("status")) { // 不可用
                if (singleCoupon.getString("status").equals("DISABLED") || singleCoupon.getString("status").equals("USED")) {
                    newcoupons.add(oldcoupons.get(i));
                } else { // 可用，但已过期
                    String expire_time = singleCoupon.getString("expire_time");
                    // 过期时间小于当前时间  代表已过去
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
