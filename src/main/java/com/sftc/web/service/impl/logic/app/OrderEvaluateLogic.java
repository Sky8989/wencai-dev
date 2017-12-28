package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.ApiPostUtil;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.tools.sf.SfTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.EvaluateMapper;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.model.entity.Evaluate;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.vo.swaggerOrderRequest.EvaluateAttributesVO;
import com.sftc.web.model.vo.swaggerOrderRequest.EvaluateMessageVO;
import com.sftc.web.model.vo.swaggerOrderRequest.EvaluateRequestVO;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.SfConstant.SF_REQUEST_URL;

@Component
public class OrderEvaluateLogic {

    private Gson gson = new Gson();
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private EvaluateMapper evaluateMapper;

    /**
     * 评价某个订单的单一包裹
     */
    public ApiResponse evaluateSingle(EvaluateRequestVO evaluateRequestVO) {

        String userUuid = TokenUtils.getInstance().getUserUUID();
        // 生成 顺丰订单评价接口 需要的信息
        String str = gson.toJson(evaluateRequestVO);

        EvaluateMessageVO evaluateMessageVO = evaluateRequestVO.getRequest();
        EvaluateAttributesVO attributes = evaluateMessageVO.getAttributes();
        String uuid = evaluateMessageVO.getUuid();
        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        if (orderExpress == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "该uuid无对应快递信息，请检查uuid",uuid);
        }

        // 向顺丰的接口发送评价信息
        String evaluateUrl = SF_REQUEST_URL + "/" + uuid + "/attributes/merchant_comment";
        HttpPut put = new HttpPut(evaluateUrl);
        put.addHeader("PushEnvelope-Device-Token", SfTokenHelper.COMMON_ACCESSTOKEN);
        String res = ApiPostUtil.post(str, put);
        JSONObject jsonObject = JSONObject.fromObject(res);
        if (jsonObject.get(CustomConstant.ERRORS) != null || jsonObject.get(CustomConstant.ERROR) != null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "评价失败",jsonObject);
        } else {
            /// 评价成功后，向评价表存入 评价记录
            Evaluate evaluate = new Evaluate();
            evaluate.setMerchant_comments(attributes.getMerchant_comments());
            evaluate.setMerchant_score(attributes.getMerchant_score());
            evaluate.setMerchant_tags(attributes.getMerchant_tags());
            evaluate.setOrderExpress_id(orderExpress.getId());
            evaluate.setUuid(uuid);
            evaluate.setUser_uuid(userUuid);
            evaluate.setCreate_time(Long.toString(System.currentTimeMillis()));
            evaluateMapper.addEvaluate(evaluate);
            jsonObject = jsonObject.getJSONObject("request").getJSONObject("attributes");
        }
        return ApiUtil.getResponse(SUCCESS, jsonObject);
    }
}
