package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.dao.mybatis.EvaluateMapper;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.entity.Evaluate;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;

@Component
public class OrderEvaluateLogic {

    private Gson gson = new Gson();

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private EvaluateMapper evaluateMapper;

    /**
     * 评价某个订单的单一包裹
     */
    public APIResponse evaluateSingle(APIRequest request) {

        // 生成 顺丰订单评价接口 需要的信息
        String str = gson.toJson(request.getRequestParam());
        JSONObject jsonObject;
        JSONObject jsonObjectParam = JSONObject.fromObject(request.getRequestParam());
        JSONObject requestOBJ = jsonObjectParam.getJSONObject("request");
        JSONObject attributes = jsonObjectParam.getJSONObject("request").getJSONObject("attributes");
        String uuid = requestOBJ.getString("uuid");
        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        boolean isNationFlag = false;
        if (orderExpress == null) {
            return APIUtil.selectErrorResponse("该uuid无对应快递信息，请检查uuid", request);
        } else {
            Order order = orderMapper.selectOrderDetailByOrderId(orderExpress.getOrder_id());
            if (order != null && "REGION_NATION".equals(order.getRegion_type())) {
                isNationFlag = true;
            }
        }

        ///如果 订单是大网单 则不请求顺丰接口，只保存在自己数据库中
        if (isNationFlag) {
            /// 评价成功后，向评价表存入 评价记录
            Evaluate evaluate = new Evaluate();
            evaluate.setMerchant_comments(attributes.getString("merchant_comments"));
            evaluate.setMerchant_score(attributes.getString("merchant_score"));
            evaluate.setMerchant_tags(attributes.getString("merchant_tags"));
            evaluate.setOrderExpress_id(orderExpress.getId());
            evaluate.setUuid(uuid);
            evaluate.setUser_id(requestOBJ.getInt("user_id"));
            evaluate.setCreate_time(Long.toString(System.currentTimeMillis()));
            evaluateMapper.addEvaluate(evaluate);
            return APIUtil.getResponse(SUCCESS, evaluate);
        }

        /// 向顺丰的接口发送评价信息
        String evaluate_url = SF_REQUEST_URL + "/" + uuid + "/attributes/merchant_comment";
        HttpPut put = new HttpPut(evaluate_url);
//        put.addHeader("PushEnvelope-Device-Token", (String) requestOBJ.get("access_token"));
        put.addHeader("PushEnvelope-Device-Token", SFTokenHelper.COMMON_ACCESSTOKEN);
        String res = APIPostUtil.post(str, put);
        jsonObject = JSONObject.fromObject(res);
        if (jsonObject.get("errors") != null || jsonObject.get("error") != null) {
            return APIUtil.submitErrorResponse("评价失败", jsonObject);
        } else {
            /// 评价成功后，向评价表存入 评价记录
            Evaluate evaluate = new Evaluate();
            evaluate.setMerchant_comments(attributes.getString("merchant_comments"));
            evaluate.setMerchant_score(attributes.getString("merchant_score"));
            evaluate.setMerchant_tags(attributes.getString("merchant_tags"));
            evaluate.setOrderExpress_id(orderExpress.getId());
            evaluate.setUuid(uuid);
            evaluate.setUser_id(requestOBJ.getInt("user_id"));
            evaluate.setCreate_time(Long.toString(System.currentTimeMillis()));
            evaluateMapper.addEvaluate(evaluate);
            jsonObject = jsonObject.getJSONObject("request").getJSONObject("attributes");
        }
        return APIUtil.getResponse(SUCCESS, jsonObject);
    }
}
