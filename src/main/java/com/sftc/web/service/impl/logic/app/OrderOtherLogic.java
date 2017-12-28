package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.ApiGetUtil;
import com.sftc.tools.api.ApiPostUtil;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.tools.screenshot.HtmlScreenShotUtil;
import com.sftc.tools.sf.SfTokenHelper;
import com.sftc.web.model.others.WeightSegment;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderAddressDetermineVO;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderContantsVO;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderPictureVO;
import com.sftc.web.model.vo.swaggerRequest.CoordinateVO;
import com.sftc.web.model.vo.swaggerRequest.DetermineTargetVO;
import com.sftc.web.service.QiniuService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.DkConstant.DK_PHANTOMJS_WEB_URL;
import static com.sftc.tools.constant.SfConstant.SF_CONSTANTS_URL;
import static com.sftc.tools.constant.SfConstant.SF_DETERMINE_URL;
import static com.sftc.tools.sf.SfTokenHelper.COMMON_ACCESSTOKEN;

@Component
public class OrderOtherLogic {

    @Resource
    private QiniuService qiniuService;

    private Gson gson = new Gson();

    private static final String SF_CONSTANT = "constant";
    private static final String SF_VALUE = "value";
    private static final String TIME_LIMIT = "TIME_LIMIT";
    private static final String PACKAGE_TYPE = "PACKAGE_TYPE";

    /**
     * 预约时间规则 (获取订单基础数据)
     */
    public ApiResponse timeConstants(OrderContantsVO orderContantsVO) {

        String constantsUrl = SF_CONSTANTS_URL + orderContantsVO.getConstants() + "?latitude="
                + orderContantsVO.getLatitude() + "&longitude=" + orderContantsVO.getLongitude();
        HttpGet get = new HttpGet(constantsUrl);
        String accessToken = SfTokenHelper.COMMON_ACCESSTOKEN;
        get.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = ApiGetUtil.get(get);
        JSONObject jsonObject = JSONObject.fromObject(res);
        if (jsonObject.get(CustomConstant.ERRORS) != null || jsonObject.get(CustomConstant.ERROR) != null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "获取订单基础数据失败",jsonObject);
        }
        try {
            if (jsonObject.containsKey(SF_CONSTANT)) {
                if (jsonObject.getJSONObject(SF_CONSTANT).containsKey(SF_VALUE)) {
                    if (jsonObject.getJSONObject(SF_CONSTANT).getJSONObject(SF_VALUE).containsKey(TIME_LIMIT)) {
                        JSONArray timeLimitArr = jsonObject.getJSONObject("constant").getJSONObject("value").getJSONArray("TIME_LIMIT");
                        for (int i = 0; i < timeLimitArr.size(); i++) {
                            JSONObject timeLimitObj = timeLimitArr.getJSONObject(i);
                            if (timeLimitObj.containsKey("code")) {
                                if ("START_TIME".equals(timeLimitObj.getString("code"))
                                        || "END_TIME".equals(timeLimitObj.getString("code"))) {
                                    if (timeLimitObj.containsKey("name") && timeLimitObj.getString("name") != null) {
                                        String tmpTimeLimit = timeLimitObj.getString("name");
                                        if (!tmpTimeLimit.contains("+")) {
                                            continue;
                                        }

                                        String tmpPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
                                        SimpleDateFormat sdf = new SimpleDateFormat(tmpPattern);
                                        Date limitDate = sdf.parse(tmpTimeLimit, new ParsePosition(0));
                                        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                                        TimeZone timeZone = TimeZone.getTimeZone("ETC/GMT-8");
                                        String resTimeLimit = DateFormatUtils.format(limitDate, pattern, timeZone);
                                        timeLimitObj.put("name", resTimeLimit);
                                    }
                                }
                            }
                        }
                    }
                    // 处理包裹基础数据
                    if (jsonObject.getJSONObject(SF_CONSTANT).getJSONObject(SF_VALUE).containsKey(PACKAGE_TYPE)) {
                        JSONArray packageTypeArr = jsonObject.getJSONObject("constant").getJSONObject("value").getJSONArray("PACKAGE_TYPE");
                        for (int i = 0; i < packageTypeArr.size(); i++) {
                            JSONObject packageTypeOBJ = packageTypeArr.getJSONObject(i);
                            if (packageTypeOBJ.containsKey("weight_segment") && (packageTypeOBJ.getJSONArray("weight_segment").size() != 0)) {
                                JSONArray weightArr = packageTypeOBJ.getJSONArray("weight_segment");
                                JSONArray newWightArr = new JSONArray();
                                int size = weightArr.size();
                                for (int j = 0; j < size; j++) {
                                    JSONObject weightOBJ = weightArr.getJSONObject(j);
                                    if (weightOBJ != null) {
                                        newWightArr.add(WeightSegment.getWeightSegmentJson(weightOBJ, j, size));
                                    }
                                }
                                packageTypeOBJ.put("weight_segment", newWightArr);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ApiUtil.getResponse(SUCCESS, jsonObject);
    }

    /**
     * 判断是否可同城下单
     */
    public ApiResponse determineOrderAddress(OrderAddressDetermineVO orderAddressDetermineVO) {

        Map<String, String> errorMap = new HashMap<>(1, 1);

        /*-------------------------------------- 寄件人经纬度参数处理 ---------------------------------------*/
        if (orderAddressDetermineVO.getSource() == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "寄件人经纬度缺失");
        } else {
            double sourceLongitude = orderAddressDetermineVO.getSource().getLongitude();
            double sourceLatitude = orderAddressDetermineVO.getSource().getLatitude();
            if (sourceLatitude == 0 || sourceLongitude == 0) {
                errorMap.put("reason", "Longitude or Latitude is not available");
                return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "寄件人经纬度参数不正确",errorMap);
            }
            //拼接顺丰所需寄件人经纬度参数
            CoordinateVO sourceCoordinateVO = new CoordinateVO();
            sourceCoordinateVO.setLatitude(sourceLatitude);
            sourceCoordinateVO.setLongitude(sourceLongitude);

            orderAddressDetermineVO.getSource().setLongitude(0);
            orderAddressDetermineVO.getSource().setLatitude(0);
            orderAddressDetermineVO.getSource().setCoordinate(sourceCoordinateVO);
        }

        /*-------------------------------------- 收件人经纬度参数处理 ---------------------------------------*/
        if (orderAddressDetermineVO.getTarget() != null) {
            double targetLongitude = orderAddressDetermineVO.getTarget().getLongitude();
            double targetLatitude = orderAddressDetermineVO.getTarget().getLatitude();
            if (targetLatitude == 0 || targetLongitude == 0) {
                errorMap.put("reason", "Longitude or Latitude is not available");
                return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "收件人经纬度参数不正确",errorMap);
            }

            //拼接顺丰所需收件人经纬度参数
            CoordinateVO targetCoordinateVO = new CoordinateVO();
            targetCoordinateVO.setLatitude(targetLatitude);
            targetCoordinateVO.setLongitude(targetLongitude);

            orderAddressDetermineVO.getTarget().setLongitude(0);
            orderAddressDetermineVO.getTarget().setLatitude(0);
            orderAddressDetermineVO.getTarget().setCoordinate(targetCoordinateVO);
        } else { //如果前端不传 target 参数，将 source 参数 复制给 target
            DetermineTargetVO determineTargetVO = gson.fromJson(gson.toJson(orderAddressDetermineVO.getSource()), DetermineTargetVO.class);
            orderAddressDetermineVO.setTarget(determineTargetVO);
        }

        //顺丰请求 request 参数
        JSONObject reqParamOBJ = new JSONObject();
        reqParamOBJ.put("request", orderAddressDetermineVO);

        String token = COMMON_ACCESSTOKEN;

        //顺丰接口
        String paramStr = gson.toJson(JSONObject.fromObject(reqParamOBJ));
        HttpPost post = new HttpPost(SF_DETERMINE_URL);
        post.addHeader("PushEnvelope-Device-Token", token);
        String resultStr = ApiPostUtil.post(paramStr, post);
        JSONObject responseObject = JSONObject.fromObject(resultStr);

        // state = 400 不能同城
        if (responseObject.containsKey(CustomConstant.ERROR)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "您所在的区域尚未开放下单，敬请期待！");
        } else { // state = 200 可同城
            Map<String, Boolean> map = new HashMap<>(1, 1);
            map.put("determine", true);
            return ApiUtil.getResponse(SUCCESS, map);
        }
    }

    /**
     * 订单分享屏幕截图
     */
    public ApiResponse screenShot(OrderPictureVO orderPictureVO) {

        String orderId = orderPictureVO.getOrder_id();
        String name = orderPictureVO.getName();
        String url = DK_PHANTOMJS_WEB_URL + orderId;
        if (name != null) {
            try {
                name = URLEncoder.encode(name, "UTF-8");
                int i = 4;
                if (name.length() > i) {
                    name = name.substring(0, 4) + "...";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            url += "&name=" + name;
        }

        String imgName = System.currentTimeMillis() + ".jpg";
        // 保存图片 现在返回了base64
        String result = HtmlScreenShotUtil.screenShot(url, imgName);
        //imgName = DK_PHANTOMJS_IMAGES + imgName; 本地全路径

        JSONObject resultObject = new JSONObject();
        if (result.endsWith(CustomConstant.SUCCESS)) {
            // 上传七牛云，七牛路径
            String imgSrc = qiniuService.uploadImageWithBase64(result.replace(CustomConstant.SUCCESS, ""));
            resultObject.put("img", imgSrc);
            return ApiUtil.getResponse(SUCCESS, resultObject);
        } else {
            resultObject.put(CustomConstant.ERROR, result);
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "生成图片失败",resultObject);
        }
    }
}
