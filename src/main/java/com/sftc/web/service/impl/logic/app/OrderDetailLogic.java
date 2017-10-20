package com.sftc.web.service.impl.logic.app;

import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.*;
import com.sftc.tools.sf.SFExpressHelper;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.mybatis.*;
import com.sftc.web.model.*;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.Message;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.entity.OrderExpressTransform;
import com.sftc.web.model.reqeustParam.UserContactParam;
import com.sftc.web.model.sfmodel.Orders;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.Object;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_ORDERROUTE_URL;
import static com.sftc.tools.constant.SFConstant.SF_ORDER_SYNC_URL;
import static com.sftc.tools.sf.SFTokenHelper.COMMON_ACCESSTOKEN;

@Component
public class OrderDetailLogic {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private GiftCardMapper giftCardMapper;
    @Resource
    private OrderExpressTransformMapper orderExpressTransformMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private OrderExpressDao orderExpressDao;

    /**
     * 订单详情接口
     */
    public APIResponse selectOrderDetail(APIRequest request) {

        String order_id = (String) request.getParameter("order_id");
        if (order_id == null || order_id.length() == 0)
            return APIUtil.paramErrorResponse("order_id不能为空");

        OrderDTO orderDTO1 = orderMapper.selectOrderDetailByOrderId(order_id);

        if (orderDTO1.getRegion_type() != null && orderDTO1.getRegion_type().equals("REGION_SAME")) {
           
                APIResponse apiResponse = syncOrderExpress(order_id);
                if (apiResponse != null) return apiResponse;
        }

        OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(order_id);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<OrderExpressDTO> dtoList = new ArrayList<OrderExpressDTO>();
        if (orderDTO == null) return APIUtil.getResponse(SUCCESS, null);
        List<OrderExpressDTO> orderExpress = orderDTO.getOrderExpressList();
        for (OrderExpressDTO orderExpressDTO : orderExpress) {
            User receiver = userMapper.selectUserByUserId(orderExpressDTO.getShip_user_id());
            if (receiver != null && receiver.getAvatar() != null) {
                // 扩展收件人头像
                orderExpressDTO.setShip_avatar(receiver.getAvatar());
            }
            dtoList.add(orderExpressDTO);
        }

        // order
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        String resultJson = new Gson().toJson(orderDTO);
        Map<String, Object> orderMap = g.fromJson(resultJson, new TypeToken<Map<String, Object>>() {
        }.getType());
        User sender = userMapper.selectUserByUserId(orderDTO.getSender_user_id());
        orderMap.put("sender_avatar", sender.getAvatar());

        // giftCard
        GiftCard giftCard = giftCardMapper.selectGiftCardById(orderDTO.getGift_card_id());

        resultMap.put("order", orderMap);
        resultMap.put("giftCard", giftCard);

        //查询是否有未读收到好友地址消息 若有则消除
        //remarkMessageReceiveAddress(order.getOrderExpressDTOList(), order.getSender_user_id());

        return APIUtil.getResponse(SUCCESS, resultMap);
    }

    /**
     * 订单快递详情
     */
    public APIResponse selectExpressDetail(APIRequest request) {

        // Param
        String uuid = (String) request.getParameter("uuid");
        if (uuid == null || uuid.equals(""))
            return APIUtil.paramErrorResponse("uuid不能为空");
        // order
        OrderDTO order = orderMapper.selectOrderDetailByUuid(uuid);
        if (order == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        JSONObject respObject = new JSONObject();

        String regionType = order.getRegion_type();

        if (regionType == null) { //增加对未提交订单的查询，此时regionType无值
            respObject.put("order", order);
            return APIUtil.getResponse(SUCCESS, respObject);
        }

        if (regionType.equals("REGION_NATION")) { // 大网

            // 兜底单
            OrderExpressTransform orderExpressTransform = orderExpressTransformMapper.selectExpressTransformByUUID(uuid);
            respObject.put("transform", orderExpressTransform);

            // sort
            String sort = (String) request.getParameter("sort");
            sort = sort == null ? "desc" : sort;
            // GET
            String url = SF_ORDERROUTE_URL + uuid + "&sort=" + sort;
            HttpGet get = new HttpGet(url);
            String access_token = SFTokenHelper.getToken();
            get.addHeader("Authorization", "bearer " + access_token);
            String res = APIGetUtil.get(get);

            if (!res.equals("[]")) {
                JSONArray routeList = JSONArray.fromObject(res);
                if (routeList != null)
                    respObject.put("sf", routeList);
            } else {
                respObject.put("sf", new JSONObject());
            }

        } else if (regionType.equals("REGION_SAME")) { // 同城

            // 同城订单需要access_token
            String access_token = (String) request.getParameter("access_token");
            access_token = (access_token == null || access_token.equals("") ? COMMON_ACCESSTOKEN : access_token);

            respObject = SFExpressHelper.getExpressDetail(uuid, access_token);
            //处理错误信息
            if (respObject.containsKey("error") || respObject.containsKey("errors") || respObject.containsKey("ERROR")) {
                return APIUtil.selectErrorResponse("查询失败", respObject);
            }

            // 已支付的订单，如果status为PAYING，则要改为WAIT_HAND_OVER
            OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
            String order_status = respObject.getJSONObject("request").getString("status");
            String directed_code = null;
            int is_directed = 0;
            if (respObject.containsKey("request")) {
                JSONObject req = respObject.getJSONObject("request");
                if (req != null && req.containsKey("attributes")) {
                    JSONObject attributuOBJ = respObject.getJSONObject("request").getJSONObject("attributes");
                    if (attributuOBJ != null) {
                        if (attributuOBJ.containsKey("source") && attributuOBJ.getString("source") != null) {
                            if (attributuOBJ.getString("source").equals("DIRECTED")) {
                                is_directed = 1;
                            }
                        }
                        if (attributuOBJ.containsKey("directed_code")) {
                            directed_code = attributuOBJ.getString("directed_code");
                            orderExpress.setDirected_code(directed_code);
                        }
                    }
                }
            }

            if (order_status.equals("WAIT_HAND_OVER")) { // 当同城查询出来的状态是待揽件 我方库中也要存为待揽件
                orderExpress.setState("WAIT_HAND_OVER");
            }
            orderExpressDao.save(orderExpress);

            boolean payed = respObject.getJSONObject("request").getBoolean("payed");
            if (payed && order_status.equals("PAYING") && order.getPay_method().equals("FREIGHT_PREPAID")) {
                respObject.getJSONObject("request").put("status", "WAIT_HAND_OVER");
                OrderExpress oe = orderExpressMapper.selectExpressByUuid(uuid);
                oe.setState("WAIT_HAND_OVER");
                oe.setDirected_code(directed_code);
                oe.setIs_directed(is_directed);
                orderExpressDao.save(oe);
                order = orderMapper.selectOrderDetailByUuid(uuid);
            }
            APIResponse apiResponse = syncOrderExpress(order.getId());
            if (apiResponse != null) return apiResponse;
            order = orderMapper.selectOrderDetailByUuid(uuid);
        }

        respObject.put("order", order);

        return APIUtil.getResponse(SUCCESS, respObject);
    }

    //同步同城的订单信息
    private APIResponse syncOrderExpress(String orderid) {
        // handle url
        String ORDERS_URL = SF_ORDER_SYNC_URL;
        String uuids = "";

        List<OrderExpress> orderExpressList = orderExpressMapper.findAllOrderExpressByOrderId(orderid);

        for (OrderExpress oe : orderExpressList) { //此处订单若过多 url过长 sf接口会报414
            Order order = orderMapper.selectOrderDetailByOrderId(oe.getOrder_id());
            if (order != null && order.getRegion_type() != null && order.getRegion_type().equals("REGION_SAME")) {
                // 只有同城的订单能同步快递状态
                uuids = uuids + oe.getUuid() + ",";
            }
        }

        User user = userMapper.getUuidAndtoken(orderid);

        if (uuids.equals("")) return null; //无需同步的订单 直接返回
        uuids = uuids.substring(0, uuids.length() - 1);
        ORDERS_URL = ORDERS_URL.replace("{uuid}", uuids);

        // POST
        List<Orders> orderses = null;
        try {
            orderses = APIResolve.getOrdersJson(ORDERS_URL, user.getToken().getAccess_token());
        } catch (Exception e) {
            return APIUtil.submitErrorResponse("正在同步订单状态，稍后重试", e);
        }

        // update express status
        if (orderses != null) {
            for (Orders orders : orderses) {
                String uuid = orders.getUuid();
                String order_status = orders.getStatus();
                OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
                orderExpress.setState(order_status);
                if (orders.getAttributes() != null) {
                    orderExpress.setAttributes(orders.getAttributes());
                }
                orderExpressDao.save(orderExpress);
            }
        }
        return null;
    }

    //////////////////////消除 收到地址 的通知消息//////////////////////
    private void remarkMessageReceiveAddress(List<OrderExpress> orderExpressList, int user_id) {
        List<Message> messageReceiveAddress = messageMapper.selectMessageReceiveAddress(user_id);
        //如果生成了“收到地址”通知消息
        if (messageReceiveAddress.size() > 0) {
            boolean flag = false;
            // 查看生成消息的快递id 是否和所查询快递id的相同
            for (OrderExpress orderExpress : orderExpressList) {
                if (orderExpress.getId() == messageReceiveAddress.get(0).getExpress_id()) {
                    flag = true;
                }
            }
            if (flag) messageMapper.updateIsRead(messageReceiveAddress.get(0).getId());
        }
    }

}
