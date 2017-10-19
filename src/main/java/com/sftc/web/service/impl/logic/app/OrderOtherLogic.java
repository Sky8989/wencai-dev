package com.sftc.web.service.impl.logic.app;

import com.sftc.tools.api.APIGetUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.screenshot.HtmlScreenShotUtil;
import com.sftc.web.service.QiniuService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.DKConstant.DK_PHANTOMJS_WEB_URL;
import static com.sftc.tools.constant.SFConstant.SF_CONSTANTS_URL;

@Component
public class OrderOtherLogic {

    @Resource
    private QiniuService qiniuService;

    /**
     * 预约时间规则 (获取订单常量)
     */
    public APIResponse timeConstants(APIRequest request) {
        String constantsUrl = SF_CONSTANTS_URL + request.getParameter("constants") + "?latitude=" + request.getParameter("latitude") + "&longitude=" + request.getParameter("longitude");
        HttpGet get = new HttpGet(constantsUrl);
        get.addHeader("PushEnvelope-Device-Token", (String) request.getParameter("access_token"));
        String res = APIGetUtil.get(get);
        JSONObject jsonObject = JSONObject.fromObject(res);
        if (jsonObject.get("errors") != null || jsonObject.get("error") != null)
            return APIUtil.submitErrorResponse("获取常量失败", jsonObject);

        try {
            if (jsonObject.containsKey("constant")) {
                if (jsonObject.getJSONObject("constant").containsKey("value")) {
                    if (jsonObject.getJSONObject("constant").getJSONObject("value").containsKey("TIME_LIMIT")) {
                        JSONArray timeLimitArr = jsonObject.getJSONObject("constant").getJSONObject("value").getJSONArray("TIME_LIMIT");
                        for (int i = 0; i < timeLimitArr.size(); i++) {
                            JSONObject timeLimitObj = timeLimitArr.getJSONObject(i);
                            if (timeLimitObj.containsKey("code") && (timeLimitObj.getString("code").equals("START_TIME") || timeLimitObj.getString("code").equals("END_TIME"))) {
                                if (timeLimitObj.containsKey("name") && timeLimitObj.getString("name") != null) {
                                    String tmpTimeLimit = timeLimitObj.getString("name");
                                    if (!tmpTimeLimit.contains("+")) continue;

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return APIUtil.getResponse(SUCCESS, jsonObject);
    }

    /**
     * 订单分享屏幕截图
     */
    public APIResponse screenShot(APIRequest request) {

        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        if (!requestObject.containsKey("order_id"))
            return APIUtil.paramErrorResponse("order_id不能为空");
        String order_id = requestObject.getString("order_id");
        String name = requestObject.containsKey("name") ? (String) requestObject.get("name") : null;
        String url = DK_PHANTOMJS_WEB_URL + order_id;
        if (name != null) {
            try {
                name = URLEncoder.encode(name, "UTF-8");
                if (name.length() > 4) {
                    name = name.substring(0, 4) + "...";
                } else {
                    name = name;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            url += "&name=" + name;
        }

        String imgName = System.currentTimeMillis() + ".jpg";
        // 保存图片
        String result = HtmlScreenShotUtil.screenShot(url, imgName); // 现在返回了base64
//        imgName = DK_PHANTOMJS_IMAGES + imgName; // 本地全路径

        JSONObject resultObject = new JSONObject();
        if (result.endsWith("success")) {
            // 上传七牛云，七牛路径
            String imgSrc = qiniuService.uploadImageWithBase64(result.replace("success", ""));
            resultObject.put("img", imgSrc);
            return APIUtil.getResponse(SUCCESS, resultObject);
        } else {
            resultObject.put("error", result);
            return APIUtil.submitErrorResponse("生成图片失败", resultObject);
        }
    }
}