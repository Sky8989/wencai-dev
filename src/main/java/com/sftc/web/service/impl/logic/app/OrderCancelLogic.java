package com.sftc.web.service.impl.logic.app;


import com.github.pagehelper.util.StringUtil;
import com.sftc.tools.api.*;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.OrderCancelDao;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderCancel;
import com.sftc.web.model.entity.OrderExpress;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
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
    private OrderExpressDao orderExpressDao;
    @Resource
    private OrderCancelDao orderCancelDao;

    //////////////////// Public Method ////////////////////

    /**
     * 取消订单
     */
    @Transactional
    public APIResponse cancelOrder(APIRequest request) {
        JSONObject paramJsonObject = JSONObject.fromObject(request.getRequestParam());
        //获取订单id，便于后续取消订单操作的取用
        String id = paramJsonObject.getString("order_id");
        paramJsonObject.remove("order_id");
        String access_token = SFTokenHelper.COMMON_ACCESSTOKEN;
        //对重复取消订单的情况进行处理
        Order order = orderMapper.selectOrderDetailByOrderId(id);
        if (order == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        // 同城是硬取消
        return cancelSAMEOrder(id, access_token);
    }

    /**
     * 根据uuid取消订单
     *
     * @param request
     * @return
     */
    @Transactional
    public APIResponse cancelOrderByUuid(APIRequest request) {
        JSONObject paramJsonObject = JSONObject.fromObject(request.getRequestParam());
        //获取订单id，便于后续取消订单操作的取用
        String uuid = paramJsonObject.getString("uuid");
        paramJsonObject.remove("uuid");
        String access_token = TokenUtils.getInstance().getAccess_token();
        //对重复取消订单的情况进行处理
        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        if (orderExpress == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        // 同城是硬取消
        return cancelSAMEOrderByUuid(uuid, access_token);
    }

    /**
     * 取消同城超时订单
     */
    public void cancelSameUnCommitOrder(String order_id, long timeOutInterval) {
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        if (Long.parseLong(order.getCreate_time()) + timeOutInterval < System.currentTimeMillis()) { // 超时
            // 取消同城订单
//            Order order1 = orderDao.findOne(order_id);
//            order1.setIs_cancel(1);
//            orderDao.save(order1);
            orderMapper.updateCancelOrderById(order_id); //事务问题,先存在查的改为统一使用Mybatis
            // 同城 超时未填写或者支付超时 都更新为超时OVERTIME
            List<OrderExpress> orderExpress = orderExpressMapper.findAllOrderExpressByOrderId(order_id);
            for (OrderExpress orderExpress1 : orderExpress) {
                orderExpress1.setRoute_state("OVERTIME");
                orderExpress1.setPay_state("WAIT_PAY");
                orderExpressDao.save(orderExpress1);
            }
            addCancelRecord(order_id, "超时软取消", "同城");
        }
    }

    //////////////////// Private Method ////////////////////

    // 取消同城订单 与 未提交单
    private APIResponse cancelSAMEOrder(String order_id, String access_token) {

        List<OrderExpress> arrayList = orderExpressMapper.findAllOrderExpressByOrderId(order_id);
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        if (order == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        //遍历所有的快递列表
        for (OrderExpress eachOrderExpress : arrayList) {
            eachOrderExpress.setRoute_state("CANCELED");
            eachOrderExpress.setPay_state("WAIT_PAY");
            orderExpressDao.save(eachOrderExpress);
            // 添加订单取消记录
            addCancelRecord(order_id, "主动软取消", "同城");
        }
        return APIUtil.getResponse(APIStatus.SUCCESS, null);
    }

    /**
     * 根据uuid取消订单
     */
    private APIResponse cancelSAMEOrderByUuid(String uuid, String access_token) {
        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        boolean isDidCommitToSF = orderExpress.getOrder_number() != null && !(orderExpress.getOrder_number().equals(""));
        if (isDidCommitToSF) { // 已经提交到顺丰，需要先从顺丰取消
            // 下面是 顺丰方面取消订单的逻辑
            String str = "{\"event\":{\"type\":\"CANCEL\",\"source\":\"MERCHANT\"}}";
            String myUrl = SF_REQUEST_URL + "/" + uuid + "/events";
            HttpPost post = new HttpPost(myUrl);
            post.addHeader("PushEnvelope-Device-Token", access_token);
            String res = APIPostUtil.post(str, post);
            JSONObject resJSONObject = JSONObject.fromObject(res);
            if (resJSONObject.containsKey("error") || resJSONObject.containsKey("errors") || !resJSONObject.containsKey("requests")) {
                return APIUtil.submitErrorResponse("订单取消失败", resJSONObject);
            } else {
                orderExpress.setRoute_state("CANCELED");
                orderExpress.setPay_state("WAIT_PAY");
                orderExpressDao.save(orderExpress);
                orderMapper.updateCancelOrderById(orderExpress.getOrder_id());
            }

        } else {
            orderExpress.setRoute_state("CANCELED");
            orderExpress.setPay_state("WAIT_PAY");
            orderExpressDao.save(orderExpress);
            orderMapper.updateCancelOrderById(orderExpress.getOrder_id());
        }
        // 添加订单取消记录
        addCancelRecord(orderExpress.getOrder_id(), isDidCommitToSF ? "主动取消" : "主动软取消", "同城");
        return APIUtil.getResponse(APIStatus.SUCCESS, null);
    }


    private void addCancelRecord(String order_id, String reason, String question_describe) {
        OrderCancel orderCancel = new OrderCancel();
        orderCancel.setOrder_id(order_id);
        orderCancel.setReason(reason);
        orderCancel.setQuestion_describe(question_describe);
        orderCancel.setCreate_time(Long.toString(System.currentTimeMillis()));
        orderCancelDao.save(orderCancel);
    }
}
