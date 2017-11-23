package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.constant.OrderConstant;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.MultiplePackageMapper;
import com.sftc.web.model.dto.MultiplePackageDTO;
import com.sftc.web.model.vo.swaggerOrderRequest.BatchPackagesVO;
import com.sftc.web.model.vo.swaggerOrderRequest.MultiplePackagePayVO;
import com.sftc.web.model.vo.swaggerOrderRequest.MultiplePackageVO;
import com.sftc.web.service.MultiplePackageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.*;
import static com.sftc.tools.constant.WXConstant.WX_template_id_1;

/**
 * 好友多包裹逻辑业务层
 *
 * @author ： CatalpaFlat
 * @date ：Create in 14:13 2017/11/17
 */
@Service
public class MultiplePackageServiceImpl implements MultiplePackageService {

    @Resource
    private MultiplePackageMapper multiplePackageMapper;
    @Resource
    private MessageServiceImpl multipleMessageService;

    /**
     * 批量计价
     *
     * @param request 请求参数封装类
     * @return 计价结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse batchValuation(APIRequest request) {
        /*---------------------------------------------------------------- 前端请求体获取解析 --------------------------------------------------------------------------------*/
        Object requestFromPOJOToJson = getRequestFromPOJOToJson(request);
        if (requestFromPOJOToJson instanceof APIResponse) {
            return (APIResponse) requestFromPOJOToJson;
        }
        MultiplePackageVO requestPOJO = (MultiplePackageVO) requestFromPOJOToJson;

        JSONObject sfRequestJson = new JSONObject();
        //获取公共uuid
        String uuid = SFTokenHelper.COMMON_UUID;
        //获取公共access_token
        String accessToken = SFTokenHelper.COMMON_ACCESSTOKEN;
        //获取orderID
        String orderID = requestPOJO.getOrder_id();

        /*---------------------------------------------------------------- 查询数据库获取收件人和寄件人信息 --------------------------------------------------------------------------------*/
        //寄件人信息
        MultiplePackageDTO sourceInfo = multiplePackageMapper.querySourceOrderInfoByOrderID(orderID);
        if (sourceInfo == null) {
            return APIUtil.selectErrorResponse("无效orderId", null);
        }
        //收件人信息
        List<MultiplePackageDTO> targetInfos = multiplePackageMapper.queryTargetsOrderInfoByOrderID(orderID);
        if (targetInfos == null) {
            return APIUtil.selectErrorResponse("无效orderId", null);
        }

        /*---------------------------------------------------------------- sf请求体封装拼接 --------------------------------------------------------------------------------*/
        //requests请求体内容
        Map<String, Object> requestsMap = mosaicTargetRequestsJson(requestPOJO, targetInfos);

        //requests信息数组对象封装
        JSONArray targetsArray = new JSONArray();
        int index = 1;
        for (MultiplePackageDTO obj : targetInfos) {
            if (obj != null) {
                JSONObject targetJson = new JSONObject();
                targetJson.put("address", obj.getMultiplePackageAddressDTO());
                targetJson.put("coordinate", obj.getMultiplePackageLLDTO());
                requestsMap.put("target", targetJson);

                //attributes-index
                JSONObject attributesJson = new JSONObject();
                attributesJson.put("index", index);
                requestsMap.put("attributes", attributesJson);

                targetsArray.add(requestsMap);
                index++;
            }
        }

        sfRequestJson.put("requests", targetsArray);

        mosaicSourceRequestJson(requestPOJO, sfRequestJson, uuid, sourceInfo);


        LoggerFactory.getLogger(this.getClass().getName()).info("sfRequestJson:" + sfRequestJson);

        /*---------------------------------------------------------------- sf请求 --------------------------------------------------------------------------------*/
        Gson gson = new Gson();
        HttpPost post = new HttpPost(SF_Multiple_QUOTES_URL);
        // 下单设置请求头
        post.addHeader("PushEnvelope-Device-Token", accessToken);

        String res = APIPostUtil.post(gson.toJson(sfRequestJson), post);

        if (StringUtils.isBlank(res)) {
            return APIUtil.submitErrorResponse("计价失败", "sf返回体为空");
        }

        JSONObject sfResponeObject = JSONObject.fromObject(res);

        String errorStr = "error";
        if (sfResponeObject.containsKey(errorStr)) {
            return APIUtil.submitErrorResponse("计价失败", sfResponeObject);
        }

        /*-------------------------------------------------- 修改数据库表sftc_order_express -> quote的uuid--------------------------------------------------------*/
        JSONArray quotesArray = sfResponeObject.getJSONArray("quotes");
        if (quotesArray == null) {
            return APIUtil.submitErrorResponse("计价失败", "sf返回体quotes为空");
        }
        for (int j = 0; j < quotesArray.size(); j++) {
            MultiplePackageDTO multiplePackageDTO = targetInfos.get(j);
            JSONObject quotesJson = (JSONObject) quotesArray.get(j);
            String quotesUuid = quotesJson.getString("uuid");
            multiplePackageMapper.updateQuotesUUidById(multiplePackageDTO.getOrderExpressId(), quotesUuid);
        }

        return APIUtil.getResponse(SUCCESS, sfResponeObject);
    }

    /**
     * 批量下单
     *
     * @param request 请求参数封装类
     * @return 下单结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse batchPlaceOrder(APIRequest request) {
        /*---------------------------------------------------------------- 前端请求体获取解析 --------------------------------------------------------------------------------*/
        Object requestFromPOJOToJson = getRequestFromPOJOToJson(request);
        if (requestFromPOJOToJson instanceof APIResponse) {
            return (APIResponse) requestFromPOJOToJson;
        }
        MultiplePackageVO requestPOJO = (MultiplePackageVO) requestFromPOJOToJson;

        //获取orderID
        String orderID = requestPOJO.getOrder_id();

         /*---------------------------------------------------------------- 查询数据库获取收件人和寄件人信息 --------------------------------------------------------------------------------*/

        //收件人信息
        List<MultiplePackageDTO> targetInfos = multiplePackageMapper.queryTargetsOrderInfoByOrderID(orderID);
        if (targetInfos == null) {
            return APIUtil.selectErrorResponse("无效orderId", null);
        }
        if (targetInfos.size()<=1){
            return APIUtil.submitErrorResponse("收件人不能少于2人", null);
        }
        //寄件人信息
        MultiplePackageDTO sourceInfo = multiplePackageMapper.querySourceOrderInfoByOrderID(orderID);
        if (sourceInfo == null) {
            return APIUtil.selectErrorResponse("无效orderId", null);
        }


        /*---------------------------------------------------------------- sf请求体封装拼接 --------------------------------------------------------------------------------*/
        JSONObject sfRequestJson = new JSONObject();

        //requests请求体内容
        Map<String, Object> requestsMap = mosaicTargetRequestsJson(requestPOJO, targetInfos);

        //attributes-pay_in_group
        JSONObject attributesJson = new JSONObject();
        attributesJson.put("pay_in_group", true);
        requestsMap.put("attributes", attributesJson);

        //requests信息数组对象封装
        JSONArray targetsArray = new JSONArray();
        for (MultiplePackageDTO obj : targetInfos) {
            if (obj != null) {
                JSONObject targetJson = new JSONObject();
                targetJson.put("address", obj.getMultiplePackageAddressDTO());
                targetJson.put("coordinate", obj.getMultiplePackageLLDTO());
                requestsMap.put("target", targetJson);

                //quote-uuid
                JSONObject quoteJson = new JSONObject();
                quoteJson.put("uuid", obj.getQuoteUUId());
                requestsMap.put("quote", quoteJson);

                targetsArray.add(requestsMap);
            }
        }
        sfRequestJson.put("requests", targetsArray);

        //request 请求封装拼接

        //获取uuid
        String uuid = TokenUtils.getInstance().getUserUUID();
        String reserveTime = mosaicSourceRequestJson(requestPOJO, sfRequestJson, uuid, sourceInfo);


        LoggerFactory.getLogger(this.getClass().getName()).info("sfRequestJson:" + sfRequestJson);

        /*---------------------------------------------------------------- sf请求 --------------------------------------------------------------------------------*/
        Gson gson = new Gson();
        HttpPost post = new HttpPost(SF_Multiple_REQUEST_URL);
        //获取access_token
        String accessToken = TokenUtils.getInstance().getAccess_token();
        // 下单设置请求头
        post.addHeader("PushEnvelope-Device-Token", accessToken);

        String res = APIPostUtil.post(gson.toJson(sfRequestJson), post);

        if (StringUtils.isBlank(res)) {
            return APIUtil.submitErrorResponse("下单失败", "sf返回体为空");
        }

        JSONObject sfResponeObject = JSONObject.fromObject(res);
        String errorStr = "error";
        if (sfResponeObject.containsKey(errorStr)) {
            return APIUtil.submitErrorResponse("下单失败", sfResponeObject);
        }

        /*-------------------------------------------------- 修改数据库表sftc_order_express--------------------------------------------------------*/
        String keyRequests = "requests";
        if (!sfResponeObject.containsKey(keyRequests)) {
            return APIUtil.submitErrorResponse("下单失败", "sf返回体requests为空");
        }

        JSONArray sfResponeRequestsArray = sfResponeObject.getJSONArray("requests");
        if (sfResponeRequestsArray == null || sfResponeRequestsArray.size() < 1) {
            return APIUtil.submitErrorResponse("下单失败", "sf返回体requests为空");
        }
        int mapSize = sfResponeRequestsArray.size() * 6;
        Map<String, Object> map = new HashMap<>(mapSize);
        if (StringUtils.isBlank(reserveTime)) {
            reserveTime = String.valueOf(System.currentTimeMillis());
        }
        StringBuilder requestNumSB = new StringBuilder();
        StringBuilder shipNameSB = new StringBuilder();
        String payType = requestPOJO.getPay_type();
        String type = "FREIGHT_PREPAID";
        //到付寄付状态改变
        if (type.equalsIgnoreCase(payType)) {
            map.put("status", "INIT");
        } else {
            map.put("status", "WAIT_HAND_OVER");
        }
        for (int j = 0; j < sfResponeRequestsArray.size(); j++) {
            JSONObject requestsJson = (JSONObject) sfResponeRequestsArray.get(j);
            MultiplePackageDTO multiplePackageDTO = targetInfos.get(j);
            String orderExpressId = multiplePackageDTO.getOrderExpressId();
            String uuid1 = requestsJson.getString("uuid");
            String requestNum = requestsJson.getString("request_num");
            String orderTime = Long.toString(System.currentTimeMillis());
            String receiver = requestsJson.getJSONObject("target").getJSONObject("address").getString("receiver");
            map.put("orderExpressId", orderExpressId);
            map.put("uuid", uuid1);
            map.put("requestNum", requestNum);
            map.put("orderTime", orderTime);
            map.put("reserveTime", reserveTime);
            multiplePackageMapper.updateOrderExpressById(map);
            requestNumSB.append(requestNum);
            shipNameSB.append(receiver);
            if (j != (sfResponeRequestsArray.size() - 1)) {
                requestNumSB.append(",");
                shipNameSB.append(",");
            }
        }

        /*-------------------------------------------------- 修改数据库表sftc_order--------------------------------------------------------*/
        JSONObject requestGroupJson = sfResponeObject.getJSONObject("requestgroup");
        String groupUUId = requestGroupJson.getString("uuid");
        String orderId = sourceInfo.getOrderId();
        multiplePackageMapper.updateorderById(orderId, groupUUId);

        /*-------------------------------------------------- 发送微信模板给寄件人--------------------------------------------------------*/
        // 普通同城跳转链接
        String path;
        String formId = requestPOJO.getForm_id();
        if (!StringUtils.isBlank(formId)) {
            JSONObject json = (JSONObject) sfResponeRequestsArray.get(0);
            String str = "merchant";
            if (json.containsKey(str)) {
                JSONObject merchant = json.getJSONObject(str);
                String uuId = merchant.getString("uuid");
                path = OrderConstant.BASIS_REGION_SAME_LINK + "?order_id=" + orderID + "&uuid=" + uuId;
                String[] messageArr = new String[2];
                messageArr[0] = requestNumSB + "";
                messageArr[1] = "您的顺丰订单下单成功！收件人是：" + shipNameSB;
                multipleMessageService.sendWXTemplateMessage(Integer.valueOf(sourceInfo.getMultiplePackageAddressDTO().getUserId()),
                        messageArr, path, formId, WX_template_id_1);
            }
        }


        return APIUtil.getResponse(SUCCESS, sfResponeObject);
    }

    /**
     * 批量支付
     *
     * @param request 前端请求request
     * @return 支付结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse batchPay(APIRequest request) {
        //获取请求参数对象
        MultiplePackagePayVO requestParam = (MultiplePackagePayVO) request.getRequestParam();
        if (requestParam == null) {
            return APIUtil.paramErrorResponse("请求参数为空");
        }
        //group_uuid
        String groupUUId = requestParam.getGroup_uuid();
        String openID = multiplePackageMapper.queryUserOpenIDByGroupUUId(groupUUId);
        if (StringUtils.isBlank(openID)) {
            return APIUtil.selectErrorResponse("open_id为空", null);
        }
        String payUrl = SF_Multiple_PAY_URL + "/" + groupUUId + "/js_pay?open_id=" + openID;
        HttpPost post = new HttpPost(payUrl);
        //获取公共access_token
        String accessToken = TokenUtils.getInstance().getAccess_token();
        post.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = APIPostUtil.post("", post);
        JSONObject resultObject = JSONObject.fromObject(res);
        String str = "error";
        if (resultObject.containsKey(str)) {
            return APIUtil.submitErrorResponse("支付失败，请查看返回值", resultObject);
        }
        /*-------------------------------------------------- 修改数据库表sftc_order_express  route_state--------------------------------------------------------*/


        multiplePackageMapper.updateRouteStateByGroupID("PAYING",groupUUId);

        String payStr = resultObject.getString("payStr");
        JSONObject responseJson = new JSONObject();
        JSONObject payStrJson = JSONObject.fromObject(payStr);
        responseJson.put("payStr", payStrJson);

        return APIUtil.getResponse(SUCCESS, responseJson);
    }

    /**
     * 是否支付成功
     *
     * @param request 前端请求request
     * @return 支付结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse isPay(APIRequest request) {
        //获取请求参数对象
        MultiplePackagePayVO requestParam = (MultiplePackagePayVO) request.getRequestParam();
        if (requestParam == null) {
            return APIUtil.paramErrorResponse("请求参数为空");
        }
        //group_uuid
        String groupUUId = requestParam.getGroup_uuid();
        //获取openID
        String openID = multiplePackageMapper.queryUserOpenIDByGroupUUId(groupUUId);
        if (StringUtils.isBlank(openID)) {
            return APIUtil.selectErrorResponse("open_id为空", null);
        }
        String payUrl = SF_Multiple_PAY_URL + "/" + groupUUId + "/paid?open_id=" + openID;
        HttpGet get = new HttpGet(payUrl);
        //获取公共access_token
        String accessToken = TokenUtils.getInstance().getAccess_token();
        get.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = APIPostUtil.get("", payUrl);
        if (StringUtils.isBlank(res)){
            return APIUtil.submitErrorResponse("支付判断失败", "sf返回体res为空");
        }
        JSONObject resultObject = JSONObject.fromObject(res);
        String str = "error";
        if (resultObject.containsKey(str)) {
            return APIUtil.submitErrorResponse("支付判断失败，请查看返回值", resultObject);
        }

        /*-------------------------------------------------- 修改数据库表sftc_order_express  pay_state--------------------------------------------------------*/
        JSONObject requestGroupJson = resultObject.getJSONObject("request_group");
        if (requestGroupJson == null) {
            return APIUtil.submitErrorResponse("支付判断失败", "sf返回体requests为空");
        }
        String paid = requestGroupJson.getString("paid");
        String isTrue = "true";
        if (paid.equalsIgnoreCase(isTrue)) {
            String orderId = requestParam.getOrder_id();
            if (StringUtils.isBlank(orderId)) {
                return APIUtil.paramErrorResponse("订单id为空");
            }
            multiplePackageMapper.updatePayStatuByGroupID(orderId);
        }

        return APIUtil.getResponse(SUCCESS, resultObject);
    }

    /**
     * 拼接寄件人请求json
     *
     * @param requestPOJO 前端请求体对象
     *                    param sfRequestJson sf请求体
     * @param uuid        公共请求uuid
     * @param sourceInfo  寄件人信息
     * @return 预约时间
     */
    private String mosaicSourceRequestJson(MultiplePackageVO requestPOJO, JSONObject sfRequestJson, String uuid, MultiplePackageDTO sourceInfo) {
        //requset信息
        JSONObject requestJson = new JSONObject();
        JSONObject sourceJson = new JSONObject();
        sourceJson.put("address", sourceInfo.getMultiplePackageAddressDTO());
        sourceJson.put("coordinate", sourceInfo.getMultiplePackageLLDTO());
        //寄件人信息
        requestJson.put("source", sourceJson);
        //merchant
        JSONObject merchantJson = new JSONObject();
        merchantJson.put("uuid", uuid);
        requestJson.put("merchant", merchantJson);
        //获取预约时间处理
        String reserveTime = requestPOJO.getReserve_time();
        //订单类型为：预约
        if (!StringUtils.isBlank(reserveTime)) {
            reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserveTime, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            requestJson.put("reserve_time", reserveTime);
        }
        //同城下单参数增加 C 端小程序标识和订单类型表示   NORMAL/RESERVED/DIRECTED
        requestJson.put("request_source", "C_WX_APP");
        //默认为普通
        requestJson.put("type", "NORMAL");
        sfRequestJson.put("request", requestJson);
        return reserveTime;
    }

    /**
     * 拼接收件人请求json
     *
     * @param requestPOJO 前端请求体对象
     * @param targetInfos 收件人信息
     * @return requestsMap
     */
    private Map<String, Object> mosaicTargetRequestsJson(MultiplePackageVO requestPOJO, List<MultiplePackageDTO> targetInfos) {
        Map<String, Object> requestsMap = new HashMap<>(targetInfos.size());
        //包裹
        List<BatchPackagesVO> packagesVO = requestPOJO.getPackages();
        JSONArray packagesArrays = JSONArray.fromObject(packagesVO);
        requestsMap.put("packages", packagesArrays);

        //产品类型
        requestsMap.put("product_type", requestPOJO.getProduct_type());
        //付款类型
        requestsMap.put("pay_type", requestPOJO.getPay_type());
        return requestsMap;
    }

    /**
     * 获取请求体中对象转为json
     *
     * @param request 请求参数封装类
     * @return 返回转化结果
     */
    private Object getRequestFromPOJOToJson(APIRequest request) {
        //获取请求参数对象
        MultiplePackageVO requestParam = (MultiplePackageVO) request.getRequestParam();
        if (requestParam == null) {
            return APIUtil.paramErrorResponse("请求参数为空");
        }
        return requestParam;
    }


}
