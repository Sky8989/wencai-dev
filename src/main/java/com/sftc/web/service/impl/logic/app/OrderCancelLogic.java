package com.sftc.web.service.impl.logic.app;


import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.OrderDao;
import com.sftc.web.dao.mybatis.OrderCancelMapper;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.OrderCancel;
import com.sftc.web.model.OrderExpress;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;

@Component
public class OrderCancelLogic {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private OrderCancelMapper orderCancelMapper;
    @Resource
    private OrderDao orderDao;

    //////////////////// Public Method ////////////////////

    /**
     * 取消订单
     */
    @Transactional
    public APIResponse cancelOrder(Object object) {
        JSONObject paramJsonObject = JSONObject.fromObject(object);
        //获取订单id，便于后续取消订单操作的取用
        String id = paramJsonObject.getString("order_id");
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
            APIResponse cancelResult = cancelNATIONOrder(id);
            if (cancelResult.getState() == 200) {
                addCancelRecord(id, "主动软取消", "大网");
            }
            return cancelResult;
        } else { // false 同城单 或者 未提交单
            return cancelSAMEOrder(id, access_token);
        }
    }

    /**
     * 取消大网超时订单
     */
    public void cancelNationUnCommitOrder(String order_id, long timeOutInterval) {
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        if (Long.parseLong(order.getCreate_time()) + timeOutInterval < System.currentTimeMillis()) { // 超时
            // 取消大网订单
            cancelNATIONOrder(order_id);
            addCancelRecord(order_id, "超时软取消", "大网");
        }
    }

    /**
     * 取消同城超时订单
     */
    public void cancelSameUnCommitOrder(String order_id, long timeOutInterval) {
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        if (Long.parseLong(order.getCreate_time()) + timeOutInterval < System.currentTimeMillis()) { // 超时
            // 取消同城订单
            orderMapper.updateCancelOrderById(order_id);
            // 同城 超时未填写或者支付超时 都更新为超时OVERTIME
            orderExpressMapper.updateOrderExpressOvertime(order_id);
            addCancelRecord(order_id, "超时软取消", "同城");
        }
    }

    //////////////////// Private Method ////////////////////

    // 取消大网订单
    private APIResponse cancelNATIONOrder(String order_id) {
        try {
            Order order = orderDao.findOne(order_id);
            order.setIs_cancel("Cancelled");
            orderDao.save(order);
            orderExpressMapper.updateOrderExpressCanceled(order_id);
            return APIUtil.getResponse(APIStatus.SUCCESS, "订单取消成功");
        } catch (Exception e) {
            return APIUtil.logicErrorResponse("数据库操作异常", e);
        }
    }

    // 取消同城订单 与 未提交单
    private APIResponse cancelSAMEOrder(String order_id, String access_token) {

        List<OrderExpress> arrayList = orderExpressMapper.findAllOrderExpressByOrderId(order_id);
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);

        boolean isDidCommitToSF = order.getRegion_type() != null && !(order.getRegion_type().equals(""));
        if (isDidCommitToSF) { // 已经提交到顺丰，需要先从顺丰取消
            StringBuilder stringBuilder = new StringBuilder();
            //遍历所有的快递列表
            for (OrderExpress eachOrderExpress : arrayList) {
                if (eachOrderExpress.getUuid() != null && !"".equals(eachOrderExpress.getUuid())) {
                    stringBuilder.append(eachOrderExpress.getUuid());
                    stringBuilder.append(",");
                }
            }
            //这是订单已经提交付款了的操作
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            // 下面是 顺丰方面取消订单的逻辑
            String str = "{\"event\":{\"type\":\"CANCEL\",\"source\":\"MERCHANT\"}}";
            String myUrl = SF_REQUEST_URL + "/" + stringBuilder.toString() + "/events";
            HttpPost post = new HttpPost(myUrl);
            post.addHeader("PushEnvelope-Device-Token", access_token);
            String res = APIPostUtil.post(str, post);
            JSONObject resJSONObject = JSONObject.fromObject(res);
            if (resJSONObject.containsKey("error") || resJSONObject.containsKey("errors") || !resJSONObject.containsKey("requests")) {
                return APIUtil.submitErrorResponse("订单取消失败", resJSONObject);
            }
        }

        // 订单还未提交给顺丰的情况，只更新order的信息即可
        // 订单已提交，仍然需要更新
        Order order1 = orderDao.findOne(order_id);
        order1.setIs_cancel("Cancelled");
        orderDao.save(order1);
        orderExpressMapper.updateOrderExpressCanceled(order_id);
        // 添加订单取消记录
        addCancelRecord(order_id, isDidCommitToSF ? "主动取消" : "主动软取消", "同城");

        return APIUtil.getResponse(APIStatus.SUCCESS, null);
    }

    private void addCancelRecord(String order_id, String reason, String question_describe) {
        OrderCancel orderCancel = new OrderCancel();
        orderCancel.setOrder_id(order_id);
        orderCancel.setReason(reason);
        orderCancel.setQuestion_describe(question_describe);
        orderCancel.setCreate_time(Long.toString(System.currentTimeMillis()));
        orderCancelMapper.addCancelOrder(orderCancel);
    }
}
