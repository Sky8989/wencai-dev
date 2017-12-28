package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.ApiGetUtil;
import com.sftc.tools.api.ApiPostUtil;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.tools.constant.OrderConstant;
import com.sftc.tools.sf.SfTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.MultiplePackageMapper;
import com.sftc.web.model.dto.MultipleGroupUUIDDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.SfConstant.*;
import static com.sftc.tools.constant.SfConstant.SF_Multiple_PAY_URL;
import static com.sftc.tools.constant.WxConstant.WX_TEMPLATE_ID1;

/**
 * 好友多包裹逻辑业务层
 *
 * @author ： CatalpaFlat
 */
@Service
public class MultiplePackageServiceImpl implements MultiplePackageService {
    private static final Logger logger = LoggerFactory.getLogger(MultiplePackageServiceImpl.class.getName());

    @Resource
    private MultiplePackageMapper multiplePackageMapper;
    @Resource
    private MessageServiceImpl multipleMessageService;
    @Resource
    private OrderConstant orderConstant;

    /**
     * 批量计价
     *
     * @param requestPOJO 请求参数封装类
     * @return 计价结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse batchValuation(MultiplePackageVO requestPOJO) {
        /*---------------------------------------------------------------- 前端请求体获取解析 --------------------------------------------------------------------------------*/

        JSONObject sfRequestJson = new JSONObject();
        TokenUtils instance = TokenUtils.getInstance();
        String uuid = instance.getUserUUID();
        String accessToken = instance.getAccessToken();
        if (StringUtils.isBlank(uuid) || StringUtils.isBlank(accessToken)) {
            //获取公共uuid
            uuid = SfTokenHelper.COMMON_UUID;
            //获取公共access_token
            accessToken = SfTokenHelper.COMMON_ACCESSTOKEN;
        }
        //获取orderID
        String orderID = requestPOJO.getOrder_id();

        /*---------------------------------------------------------------- 查询数据库获取收件人和寄件人信息 --------------------------------------------------------------------------------*/
        //寄件人信息
        MultiplePackageDTO sourceInfo = multiplePackageMapper.querySourceOrderInfoByOrderID(orderID);
        if (sourceInfo == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "invalid order_id");
        }
        //收件人信息
        List<MultiplePackageDTO> targetInfos = multiplePackageMapper.queryTargetsOrderInfoByOrderID(orderID);
        if (targetInfos == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "invalid order_id");
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
                obj.getMultiplePackageAddressDTO().setCountry("中国");
                targetJson.put("address", obj.getMultiplePackageAddressDTO());
                targetJson.getJSONObject("address").remove("userId");
                targetJson.put("coordinate", obj.getMultiplePackageLLDTO());
                requestsMap.put("target", targetJson);

                //attributes-index
                JSONObject attributesJson = new JSONObject();
                attributesJson.put("index", index);
                attributesJson.put("pay_in_group", true);
                requestsMap.put("attributes", attributesJson);

                targetsArray.add(requestsMap);
                index++;
            }
        }

        sfRequestJson.put("requests", targetsArray);

        mosaicSourceRequestJson(requestPOJO, sfRequestJson, uuid, sourceInfo);


        logger.info("sfRequestJson:" + sfRequestJson);

        /*---------------------------------------------------------------- sf请求 --------------------------------------------------------------------------------*/
        Gson gson = new Gson();
        HttpPost post = new HttpPost(SF_Multiple_QUOTES_URL);
        // 下单设置请求头
        post.addHeader("PushEnvelope-Device-Token", accessToken);

        String res = ApiPostUtil.post(gson.toJson(sfRequestJson), post);

        if (StringUtils.isBlank(res)) {
            logger.error("sf返回体为空");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Failure of valuation");
        }

        JSONObject sfResponeObject = JSONObject.fromObject(res);

        if (sfResponeObject.containsKey(CustomConstant.ERROR)) {
            logger.error("计价失败" + sfResponeObject);
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Failure of valuation", sfResponeObject);
        }

        /*-------------------------------------------------- 修改数据库表c_order_express -> quote的uuid--------------------------------------------------------*/
        JSONArray quotesArray = sfResponeObject.getJSONArray("quotes");
        if (quotesArray == null) {
            logger.error("sf返回体quotes为空");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Failure of valuation");
        }
        for (int j = 0; j < quotesArray.size(); j++) {
            MultiplePackageDTO multiplePackageDTO = targetInfos.get(j);
            JSONObject quotesJson = (JSONObject) quotesArray.get(j);
            String quotesUuid = quotesJson.getString("uuid");
            multiplePackageMapper.updateQuotesUUidById(multiplePackageDTO.getOrderExpressId(), quotesUuid);
        }
        String totalQuote = "total_quote";
        if (sfResponeObject.containsKey(totalQuote)) {
            //修改返回参数
            JSONObject totalJson;
            String totalStr = "total";
            if (sfResponeObject.containsKey(totalStr)) {
                totalJson = sfResponeObject.getJSONObject("total");
            } else {
                totalJson = new JSONObject();
            }
            totalJson.put("price", sfResponeObject.getJSONObject(totalQuote).get("price"));
            totalJson.put("real_price", sfResponeObject.getJSONObject(totalQuote).get("real_price"));
            sfResponeObject.remove(totalQuote);
        }
        return ApiUtil.getResponse(SUCCESS, sfResponeObject);
    }

    /**
     * 批量下单
     *
     * @param requestPOJO 请求参数封装类
     * @return 下单结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse batchPlaceOrder(MultiplePackageVO requestPOJO) {
        /*---------------------------------------------------------------- 前端请求体获取解析 --------------------------------------------------------------------------------*/
        //获取orderID
        String orderID = requestPOJO.getOrder_id();
        //判断是否已经下过单
        MultipleGroupUUIDDTO isPlaceOrder = multiplePackageMapper.quaryIsPlaceOrderOrderId(orderID);
        if (isPlaceOrder != null) {
            String orderNumber = isPlaceOrder.getOrder_number();
            if (StringUtils.isNotBlank(orderNumber)) {
                String groupUUID = isPlaceOrder.getGroup_uuid();
                if (StringUtils.isBlank(groupUUID)) {
                    logger.error("group_uuid为空");
                    return ApiUtil.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "System error");
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("group_uuid", groupUUID);
                return ApiUtil.error(HttpStatus.CONFLICT.value(), "请勿重复下单", jsonObject);
            }
        }
         /*---------------------------------------------------------------- 查询数据库获取收件人和寄件人信息 --------------------------------------------------------------------------------*/

        //收件人信息
        List<MultiplePackageDTO> targetInfos = multiplePackageMapper.queryTargetsOrderInfoByOrderID(orderID);
        if (targetInfos == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "invalid order_id");
        }
        if (targetInfos.size() <= 1) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "The addressee cannot be less than 2");
        }
        //寄件人信息
        MultiplePackageDTO sourceInfo = multiplePackageMapper.querySourceOrderInfoByOrderID(orderID);
        if (sourceInfo == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "invalid order_id");
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
                obj.getMultiplePackageAddressDTO().setCountry("中国");
                targetJson.put("address", obj.getMultiplePackageAddressDTO());
                targetJson.getJSONObject("address").remove("userId");
                targetJson.put("coordinate", obj.getMultiplePackageLLDTO());
                requestsMap.put("target", targetJson);

                //quote-uuid
                JSONObject quoteJson = new JSONObject();
                quoteJson.put("uuid", obj.getQuoteUUId());
                requestsMap.put("quote", quoteJson);

                //默认为普通
                requestsMap.put("type", "NORMAL");

                targetsArray.add(requestsMap);
            }
        }
        sfRequestJson.put("requests", targetsArray);

        //request 请求封装拼接

        //获取uuid
        String uuid = TokenUtils.getInstance().getUserUUID();
        String reserveTime = mosaicSourceRequestJson(requestPOJO, sfRequestJson, uuid, sourceInfo);


        logger.info("sfRequestJson:" + sfRequestJson);

        /*---------------------------------------------------------------- sf请求 --------------------------------------------------------------------------------*/
        Gson gson = new Gson();
        HttpPost post = new HttpPost(SF_Multiple_REQUEST_URL);
        //获取access_token
        String accessToken = TokenUtils.getInstance().getAccessToken();
        // 下单设置请求头
        post.addHeader("PushEnvelope-Device-Token", accessToken);

        String res = ApiPostUtil.post(gson.toJson(sfRequestJson), post);

        if (StringUtils.isBlank(res)) {
            logger.error("sf返回体quotes为空");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Single failure");
        }

        JSONObject sfResponeObject = JSONObject.fromObject(res);
        if (sfResponeObject.containsKey(CustomConstant.ERROR)) {
            logger.error("下单失败：" + sfResponeObject);
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Single failure", sfResponeObject);
        }

        /*-------------------------------------------------- 修改数据库表c_order_express--------------------------------------------------------*/
        String keyRequests = "requests";
        if (!sfResponeObject.containsKey(keyRequests)) {
            logger.error("sf返回体requests为空");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Single failure");
        }

        JSONArray sfResponeRequestsArray = sfResponeObject.getJSONArray("requests");
        if (sfResponeRequestsArray == null || sfResponeRequestsArray.size() < 1) {
            logger.error("sf返回体requests为空");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Single failure");
        }
        int mapSize = sfResponeRequestsArray.size() * 6;
        Map<String, Object> map = new HashMap<>(mapSize);

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

        /*-------------------------------------------------- 修改数据库表c_order--------------------------------------------------------*/
        String requestgroupStr = "requestgroup";
        JSONObject requestGroupJson = sfResponeObject.getJSONObject(requestgroupStr);
        String groupUUId = requestGroupJson.getString("uuid");
        logger.info("sf响应体的groupUUId：" + groupUUId);
        String orderId = sourceInfo.getOrderId();
        multiplePackageMapper.updateorderById(orderId, groupUUId);

        /*-------------------------------------------------- 发送微信模板给寄件人--------------------------------------------------------*/
        /*
         * 用来判断跳转的链接 是 
         * OrderConstant.mystery_region_same_link  			false
         * 或 OrderConstant.mamy_mystery_region_same_link	true
         */
        boolean flag = sfResponeRequestsArray.size() != 1;
        // 好友同城跳转链接
        String path;
        String formId = requestPOJO.getForm_id();
        if (!StringUtils.isBlank(formId)) {
            JSONObject json = (JSONObject) sfResponeRequestsArray.get(0);
            String str = "merchant";
            if (json.containsKey(str)) {
                JSONObject merchant = json.getJSONObject(str);
                String uuId = merchant.getString("uuid");
                if (!flag) {
                    path = orderConstant.MYSTERY_REGION_SAME_LINK + "?order_id=" + orderID + "&uuid=" + uuId;
                } else {
                    path = orderConstant.MAMY_MYSTERY_REGION_SAME_LINK + "?order_id=" + orderID;
                }
                logger.info("----好友同城微信模板跳转链接--" + path);
                String[] messageArr = new String[2];
                messageArr[0] = requestNumSB + "";
                messageArr[1] = "您的顺丰订单下单成功！收件人是：" + shipNameSB;
                multipleMessageService.sendWXTemplateMessage(sourceInfo.getMultiplePackageAddressDTO().getUserUUId(),
                        messageArr, path, formId, WX_TEMPLATE_ID1);
            }
        }


        return ApiUtil.getResponse(SUCCESS, sfResponeObject);
    }

    /**
     * 批量支付
     *
     * @param requestParam 前端请求request
     * @return 支付结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse batchPay(MultiplePackagePayVO requestParam) {
        //获取请求参数对象
        if (requestParam == null) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "the request body is empty");
        }
        //group_uuid
        String groupUUId = requestParam.getGroup_uuid();
        String attributes = multiplePackageMapper.queryUserOpenIDByGroupUUId(groupUUId);
        JSONObject attributesObj = JSONObject.fromObject(attributes);
        String openId = null;
        if (attributesObj.containsKey("c_wxopenid")) {
            openId = attributesObj.getString("c_wxopenid");
        }
        if (StringUtils.isBlank(openId)) {
            logger.error("open_id为空");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "open_id is empty");
        }
        String payUrl;
        if (StringUtils.isBlank(requestParam.getCash())) {
            payUrl = SF_Multiple_PAY_URL +
                    "/" + groupUUId +
                    "/js_pay?open_id="
                    + openId;
        } else {
            payUrl = SF_Multiple_PAY_URL +
                    "/" + groupUUId +
                    "/js_pay?open_id="
                    + openId +
                    "&cash=" +
                    requestParam.getCash();
        }

        HttpPost post = new HttpPost(payUrl);
        //获取公共access_token
        String accessToken = TokenUtils.getInstance().getAccessToken();
        post.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = ApiPostUtil.post("", post);
        JSONObject resultObject = JSONObject.fromObject(res);
        if (resultObject.containsKey(CustomConstant.ERROR)) {
            logger.error("支付失败，请看请求体：" + resultObject);
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Failure of pay", resultObject.get("error"));
        }
        /*-------------------------------------------------- 修改数据库表c_order_express  route_state--------------------------------------------------------*/
        multiplePackageMapper.updateRouteStateByGroupID("PAYING", groupUUId);

        String payStr = resultObject.getString("payStr");
        JSONObject responseJson = new JSONObject();
        JSONObject payStrJson = JSONObject.fromObject(payStr);
        responseJson.put("payStr", payStrJson);

        return ApiUtil.getResponse(SUCCESS, responseJson);
    }

    /**
     * 是否支付成功
     *
     * @param requestParam 前端请求request
     * @return 支付结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse isPay(MultiplePackagePayVO requestParam) {
        //获取请求参数对象
        if (requestParam == null) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "the request body is empty");
        }
        //group_uuid
        String groupUUId = requestParam.getGroup_uuid();
        //获取openID
        String openID = multiplePackageMapper.queryUserOpenIDByGroupUUId(groupUUId);
        if (StringUtils.isBlank(openID)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "order_id is empty");
        }
        String payUrl = SF_Multiple_PAY_URL + "/" + groupUUId + "/paid?open_id=" + openID;
        HttpGet get = new HttpGet(payUrl);
        //获取公共access_token
        String accessToken = TokenUtils.getInstance().getAccessToken();
        get.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = ApiGetUtil.get(get);
        if (StringUtils.isBlank(res)) {
            logger.error("支付失败，sf返回体res为空");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "failure to pay");
        }
        JSONObject resultObject = JSONObject.fromObject(res);
        if (resultObject.containsKey(CustomConstant.ERROR)) {
            logger.error("支付失败，响应体：" + resultObject);
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "failure to pay", resultObject.get("error"));
        }

        /*-------------------------------------------------- 修改数据库表c_order_express  pay_state--------------------------------------------------------*/
        JSONObject requestGroupJson = resultObject.getJSONObject("request_group");
        if (requestGroupJson == null) {
            logger.error("支付失败，sf返回体requests为空");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "failure to pay");
        }
        String paid = requestGroupJson.getString("paid");
        String isTrue = "true";
        if (paid.equalsIgnoreCase(isTrue)) {
            String orderId = requestParam.getOrder_id();
            if (StringUtils.isBlank(orderId)) {
                return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "order_id is empty");
            }
            multiplePackageMapper.updatePayStatuByGroupID(orderId);
        }

        return ApiUtil.getResponse(SUCCESS, resultObject);
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
        sourceInfo.getMultiplePackageAddressDTO().setCountry("中国");
        sourceJson.put("address", sourceInfo.getMultiplePackageAddressDTO());
        sourceJson.getJSONObject("address").remove("userId");
        sourceJson.put("coordinate", sourceInfo.getMultiplePackageLLDTO());
        //寄件人信息
        requestJson.put("source", sourceJson);
        //merchant
        JSONObject merchantJson = new JSONObject();
        merchantJson.put("uuid", uuid);
        requestJson.put("merchant", merchantJson);
        //获取预约时间处理
        String reserveTime = requestPOJO.getReserve_time();
        String oldReserveTime = reserveTime;
        //订单类型为：预约
        if (!StringUtils.isBlank(reserveTime)) {
            reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserveTime, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            requestJson.put("reserve_time", reserveTime);
        }
        String productType = requestPOJO.getProduct_type();
        requestJson.put("product_type", productType);
        //同城下单参数增加 C 端小程序标识和订单类型表示   NORMAL/RESERVED/DIRECTED
        requestJson.put("request_source", "C_WX_APP");
        //默认为普通
        requestJson.put("type", "NORMAL");
        sfRequestJson.put("request", requestJson);
        return oldReserveTime;
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
        //获取预约时间处理
        String reserveTime = requestPOJO.getReserve_time();
        //订单类型为：预约
        if (!StringUtils.isBlank(reserveTime)) {
            reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserveTime, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            requestsMap.put("reserve_time", reserveTime);
        }
        //产品类型
        requestsMap.put("product_type", requestPOJO.getProduct_type());
        //付款类型
        requestsMap.put("pay_type", requestPOJO.getPay_type());
        return requestsMap;
    }


}
