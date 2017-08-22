package com.sftc.web.service.impl.logic.api;


import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.OrderExpressMapper;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;

@Component
public class OrderCancelLogic {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;

    //////////////////// Public Method ////////////////////

    /**
     * 取消订单
     */
    public APIResponse cancelOrder(Object object) {
        JSONObject paramJsonObject = JSONObject.fromObject(object);
        //获取订单id，便于后续取消订单操作的取用
        int id = paramJsonObject.getInt("order_id");
        paramJsonObject.remove("order_id");
        String access_token = paramJsonObject.getString("access_token");
        //对重复取消订单的情况进行处理
        Order order = orderMapper.selectOrderDetailByOrderId(id);
        if ("Cancelled".equals(order.getIs_cancel()) || !"".equals(order.getIs_cancel())) {//is_cancel字段默认是空字符串
            //return APIUtil.getResponse(APIStatus.CANCEL_ORDER_FALT, null);
            return APIUtil.submitErrorResponse("订单已经取消，请勿重复取消操作", null);
        }
        // 不同地域类型的订单 进行不同的取消方式 大网是软取消 同城是硬取消
        if ("REGION_NATION".equals(order.getRegion_type())) {// true 大网单
            return cancelNATIONOrder(id);
        } else { // false 同城单 或者 未提交单
            return cancelSAMEOrder(id, access_token);
        }
    }

    /**
     * 取消大网超时订单
     */
    public void cancelNationUnCommitOrder(int order_id, long timeOutInterval) {
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        if (Long.parseLong(order.getCreate_time()) + timeOutInterval < System.currentTimeMillis()) { // 超时
            // 取消大网订单
            cancelNATIONOrder(order_id);
        }
    }

    /**
     * 取消同城超时订单
     */
    public void cancelSameUnCommitOrder(int order_id, long timeOutInterval) {
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        if (Long.parseLong(order.getCreate_time()) + timeOutInterval < System.currentTimeMillis()) { // 超时
            // 取消同城订单
            orderMapper.updateCancelOrderById(order_id);
//            orderExpressMapper.updateOrderExpressCanceled(order_id);
            // 同城 超时未填写或者支付超时 都更新为超时DANKAL_OVERTIME
            orderExpressMapper.updateOrderExpressOvertime(order_id);
        }
    }

    //////////////////// Private Method ////////////////////

    // 取消大网订单
    private APIResponse cancelNATIONOrder(int order_id) {
        orderMapper.updateCancelOrderById(order_id);
        orderExpressMapper.updateOrderExpressCanceled(order_id);
        return APIUtil.getResponse(APIStatus.SUCCESS, "该订单已做软取消处理");
    }


    // 取消同城订单 与 未提交单
    private APIResponse cancelSAMEOrder(int order_id, String access_token) {
        List<OrderExpress> arrayList = orderExpressMapper.findAllOrderExpressByOrderId(order_id);
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        StringBuilder stringBuilder = new StringBuilder();
        //遍历所有的快递列表
        for (OrderExpress eachOrderExpress : arrayList) {
            if (eachOrderExpress.getUuid() != null && !"".equals(eachOrderExpress.getUuid())) {
                stringBuilder.append(eachOrderExpress.getUuid());
                stringBuilder.append(",");
            }
        }

        orderMapper.updateCancelOrderById(order_id);
        orderExpressMapper.updateOrderExpressCanceled(order_id);

        boolean falg = order.getRegion_type() == null || "".equals(order.getRegion_type());
        if (!falg) { //这是订单已经提交付款了的操作
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            // 下面是 顺丰方面取消订单的逻辑
            String str = "{\"event\":{\"type\":\"CANCEL\",\"source\":\"MERCHANT\"}}";
            String myUrl = SF_REQUEST_URL + "/" + stringBuilder.toString() + "/events";
            HttpPost post = new HttpPost(myUrl);
            post.addHeader("PushEnvelope-Device-Token", access_token);
            String res = APIPostUtil.post(str, post);
            JSONObject resJSONObject = JSONObject.fromObject(res);
            if (resJSONObject.get("error") != null) {
                return APIUtil.submitErrorResponse("订单取消失败", resJSONObject);
            }
        } else { // 订单还未提交给顺丰的情况，只更新order的信息即可
            return APIUtil.getResponse(APIStatus.SUCCESS, null);
        }
        return APIUtil.getResponse(APIStatus.SUCCESS, null);
    }
}
