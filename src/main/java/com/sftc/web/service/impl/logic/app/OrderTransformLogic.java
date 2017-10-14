package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.dao.jpa.OrderDao;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderExpressTransformMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.OrderExpressTransform;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_CREATEORDER_URL;

@Component
public class OrderTransformLogic {

    private Gson gson = new Gson();

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private OrderExpressTransformMapper orderExpressTransformMapper;
    @Resource
    private OrderDao orderDao;

    /**
     * 根据同城订单的uuid，把原本同城的单下到大网
     */
    public APIResponse transformOrderFromSameToNation(APIRequest request) {

        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        String uuid = (String) requestObject.get("uuid");

        if (uuid == null || uuid.equals(""))
            return APIUtil.paramErrorResponse("uuid不能为空");

        OrderExpress oe = orderExpressMapper.selectExpressByUuid(uuid);
        if (oe == null)
            return APIUtil.submitErrorResponse("订单不存在", null);

        Order order = orderMapper.selectOrderDetailByOrderId(oe.getOrder_id());

        if (!order.getRegion_type().equals("REGION_SAME"))
            return APIUtil.submitErrorResponse("此订单非同城单，不能进行兜底操作", null);

        // 大网订单提交参数
        JSONObject sf = new JSONObject();
        String orderid = SFOrderHelper.getOrderNumber();
        sf.put("orderid", orderid);
        sf.put("j_contact", order.getSender_name());
        sf.put("j_mobile", order.getSender_mobile());
        sf.put("j_tel", order.getSender_mobile());
        sf.put("j_country", "中国");
        sf.put("j_province", order.getSender_province());
        sf.put("j_city", order.getSender_city());
        sf.put("j_county", order.getSender_area());
        sf.put("j_address", order.getSender_addr());
        sf.put("d_contact", oe.getShip_name());
        sf.put("d_mobile", oe.getShip_mobile());
        sf.put("d_tel", oe.getShip_mobile());
        sf.put("d_country", "中国");
        sf.put("d_province", oe.getShip_province());
        sf.put("d_city", oe.getShip_city());
        sf.put("d_county", oe.getShip_area());
        sf.put("d_address", oe.getShip_addr());
        sf.put("pay_method", order.getPay_method());
//        sf.put("express_type", order.getDistribution_method());
        sf.put("express_type", "2"); // 同城专送是JISUDA 需要换成1 顺丰次日、2 顺丰隔日、5 顺丰次晨。

        // handle pay_method
        String pay_method = (String) sf.get("pay_method");
        if (pay_method != null && !pay_method.equals("")) {
            if (pay_method.equals("FREIGHT_PREPAID")) { // FREIGHT_PREPAID 寄付 1
                pay_method = "1";
            } else if (pay_method.equals("FREIGHT_COLLECT")) { // FREIGHT_COLLECT 到付 2
                pay_method = "2";
            }
            sf.remove("pay_method");
            sf.put("pay_method", pay_method);
        }

        if (!oe.getState().equals("WAIT_FILL")) {
            // 订单提交
            String paramStr = gson.toJson(JSONObject.fromObject(sf));
            HttpPost post = new HttpPost(SF_CREATEORDER_URL);
            post.addHeader("Authorization", "bearer " + SFTokenHelper.getToken());
            String resultStr = APIPostUtil.post(paramStr, post);
            JSONObject resultObject = JSONObject.fromObject(resultStr);
            String messageType = (String) resultObject.get("Message_Type");

            if (messageType != null && messageType.contains("ERROR")) {
                return APIUtil.submitErrorResponse("下单失败", resultObject);
            } else {
                // 插入兜底表
                String same_uuid = oe.getUuid();
                String nation_uuid = (String) resultObject.get("ordernum");
                OrderExpressTransform oet = new OrderExpressTransform();
                oet.setExpress_id(oe.getId());
                oet.setSame_uuid(same_uuid);
                oet.setNation_uuid(orderid);
                oet.setIs_read(0);
                oet.setCreate_time(System.currentTimeMillis() + "");
                orderExpressTransformMapper.insertExpressTransform(oet);

                // 更新order表
                Order order1 = orderDao.findOne(order.getId());
                order1.setRegion_type("REGION_NATION");
                orderDao.save(order1);         // 更改订单区域类型为大网

                // 更新express表
                orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), orderid, ""); // 更新uuid
                orderExpressMapper.updateOrderExpressStatus(oe.getId(), "WAIT_HAND_OVER");      // 更新快递状态
                orderExpressMapper.updateOrderNumber(oe.getId(), nation_uuid);                          // express order_number
            }
            return APIUtil.getResponse(SUCCESS, resultObject);
        } else {
            return APIUtil.submitErrorResponse("快递订单状态异常，好友包裹尚未被领取", null);
        }
    }

    /**
     * 设置兜底记录已读
     */
    public APIResponse readExpressTransform(APIRequest request) {

        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        if (!requestObject.containsKey("express_transform_id"))
            return APIUtil.paramErrorResponse("express_transform_id不能为空");

        int express_transform_id = requestObject.getInt("express_transform_id");
        orderExpressTransformMapper.updateExpressTransformReadStatusById(express_transform_id);

        OrderExpressTransform orderExpressTransform = orderExpressTransformMapper.selectExpressTransformByID(express_transform_id);
        if (orderExpressTransform == null)
            return APIUtil.submitErrorResponse("兜底记录不存在", null);

        return APIUtil.getResponse(SUCCESS, orderExpressTransform);
    }
}
