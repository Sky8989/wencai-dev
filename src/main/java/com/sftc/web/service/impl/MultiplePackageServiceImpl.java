package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.dao.mybatis.MultiplePackageMapper;
import com.sftc.web.model.dto.MultiplePackageDTO;
import com.sftc.web.model.vo.swaggerOrderVO.BatchPackagesVO;
import com.sftc.web.model.vo.swaggerOrderVO.MultiplePackageVO;
import com.sftc.web.service.MultiplePackageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_Multiple_QUOTES_URL;
import static com.sftc.tools.constant.SFConstant.SF_Multiple_REQUEST_URL;

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
        String orderID = requestPOJO.getOrderID();

        /*---------------------------------------------------------------- 查询数据库获取收件人和寄件人信息 --------------------------------------------------------------------------------*/
        //寄件人信息
        MultiplePackageDTO sourceInfo = multiplePackageMapper.querySourceOrderInfoByOrderID(orderID);
        if (sourceInfo == null) {
            return APIUtil.selectErrorResponse("无效orderId", null);
        }
        //收件人信息
        List<MultiplePackageDTO> targetInfos = multiplePackageMapper.queryTargetsOrderInfoByOrderID(orderID);
        if (targetInfos == null) {
            return APIUtil.selectErrorResponse("无效orderId", sourceInfo);
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
        post.addHeader("PushEnvelope-Device-Token", uuid);

        String res = APIPostUtil.post(gson.toJson(sfRequestJson), post);

        if (StringUtils.isBlank(res)) {
            return APIUtil.submitErrorResponse("计价失败", "顺风返回体为空");
        }

        JSONObject sfResponeObject = JSONObject.fromObject(res);

        String errorStr = "error";
        if (sfResponeObject.containsKey(errorStr)) {
            return APIUtil.submitErrorResponse("计价失败", sfResponeObject);
        }

        /*-------------------------------------------------- 修改数据库表sftc_order_express -> quote的uuid--------------------------------------------------------*/
        JSONArray quotesArray = sfResponeObject.getJSONArray("quotes");
        if (quotesArray == null) {
            return APIUtil.submitErrorResponse("计价失败", "顺风返回体quotes为空");
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

        //获取公共uuid
        String uuid = SFTokenHelper.COMMON_UUID;
        //获取公共access_token
        String accessToken = SFTokenHelper.COMMON_ACCESSTOKEN;
        //获取orderID
        String orderID = requestPOJO.getOrderID();

         /*---------------------------------------------------------------- 查询数据库获取收件人和寄件人信息 --------------------------------------------------------------------------------*/
        //寄件人信息
        MultiplePackageDTO sourceInfo = multiplePackageMapper.querySourceOrderInfoByOrderID(orderID);
        if (sourceInfo == null) {
            return APIUtil.selectErrorResponse("无效orderId", null);
        }
        //收件人信息
        List<MultiplePackageDTO> targetInfos = multiplePackageMapper.queryTargetsOrderInfoByOrderID(orderID);
        if (targetInfos == null) {
            return APIUtil.selectErrorResponse("无效orderId", sourceInfo);
        }


        /*---------------------------------------------------------------- sf请求体封装拼接 --------------------------------------------------------------------------------*/
        JSONObject sfRequestJson = new JSONObject();

        //requests请求体内容
        Map<String, Object> requestsMap = mosaicTargetRequestsJson(requestPOJO, targetInfos);

        //attributes-pay_in_group
        JSONObject attributesJson = new JSONObject();
        attributesJson.put("pay_in_group", true);
        requestsMap.put("attributesJson", attributesJson);

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
        String reserveTime = mosaicSourceRequestJson(requestPOJO, sfRequestJson, uuid, sourceInfo);


        LoggerFactory.getLogger(this.getClass().getName()).info("sfRequestJson:" + sfRequestJson);

        Gson gson = new Gson();
        HttpPost post = new HttpPost(SF_Multiple_REQUEST_URL);
        // 下单设置请求头
        post.addHeader("PushEnvelope-Device-Token", accessToken);
        post.addHeader("PushEnvelope-Device-Token", uuid);

        String res = APIPostUtil.post(gson.toJson(sfRequestJson), post);

        if (StringUtils.isBlank(res)) {
            return APIUtil.submitErrorResponse("下单失败", "顺风返回体为空");
        }

        JSONObject sfResponeObject = JSONObject.fromObject(res);
        String errorStr = "error";
        if (sfResponeObject.containsKey(errorStr)) {
            return APIUtil.submitErrorResponse("下单失败", sfResponeObject);
        }

        /*-------------------------------------------------- 修改数据库表sftc_order_express--------------------------------------------------------*/
        String keyRequests = "requests";
        if (!sfResponeObject.containsKey(keyRequests)){
            return APIUtil.submitErrorResponse("下单失败", "顺风返回体requests为空");
        }
        JSONArray sfResponeRequestsArray = sfResponeObject.getJSONArray("requests");
        if (sfResponeRequestsArray == null || sfResponeRequestsArray.size() < 1) {
            return APIUtil.submitErrorResponse("下单失败", "顺风返回体requests为空");
        }

        int mapSize = sfResponeRequestsArray.size() * 6;
        Map<String, Object> map = new HashMap<>(mapSize);
        for (int j = 0; j < sfResponeRequestsArray.size(); j++) {
            JSONObject requestsJson = (JSONObject) sfResponeRequestsArray.get(j);
            MultiplePackageDTO multiplePackageDTO = targetInfos.get(j);
            String orderExpressId = multiplePackageDTO.getOrderExpressId();
            String uuid1 = requestsJson.getString("uuid");
            String requestNum = requestsJson.getString("request_num");
            String status = requestsJson.getString("status");
            String orderTime = Long.toString(System.currentTimeMillis());
            map.put("orderExpressId", orderExpressId);
            map.put("uuid", uuid1);
            map.put("requestNum", requestNum);
            map.put("status", status);
            map.put("orderTime", orderTime);
            map.put("reserveTime", reserveTime);
            multiplePackageMapper.updateOrderExpressById(map);
        }

        /*-------------------------------------------------- 修改数据库表sftc_order--------------------------------------------------------*/
        JSONObject requestGroupJson = sfResponeObject.getJSONObject("requestgroup");
        String groupUUId = requestGroupJson.getString("uuid");
        String orderId = sourceInfo.getOrderId();
        multiplePackageMapper.updateorderById(orderId, groupUUId);


        return APIUtil.getResponse(SUCCESS, sfResponeObject);
    }

    /**
     * 拼接寄件人请求json
     *
     * @param requestPOJO   前端请求体对象
     * @param sfRequestJson sf请求体
     * @param uuid          公共请求uuid
     * @param sourceInfo    寄件人信息
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
        List<BatchPackagesVO> packagesVO = requestPOJO.getPackagesVO();
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
