package com.sftc.web.service.impl.logic.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.APIGetUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFExpressHelper;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.Object;
import java.util.HashMap;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_ORDERROUTE_URL;
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

    /**
     * 订单详情接口
     */
    public APIResponse selectOrderDetail(APIRequest request) {

        String order_id = (String) request.getParameter("order_id");
        if (order_id == null || order_id.length() == 0)
            return APIUtil.paramErrorResponse("order_id不能为空");

        Order order = orderMapper.selectOrderDetailByOrderId(Integer.parseInt(order_id));

        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (order == null) return APIUtil.getResponse(SUCCESS, null);

        for (OrderExpress oe : order.getOrderExpressList()) {
            User receiver = userMapper.selectUserByUserId(oe.getShip_user_id());
            if (receiver != null && receiver.getAvatar() != null) {
                // 扩展收件人头像
                oe.setShip_avatar(receiver.getAvatar());
            }
        }

        // order
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        String resultJson = new Gson().toJson(order);
        Map<String, Object> orderMap = g.fromJson(resultJson, new TypeToken<Map<String, Object>>() {
        }.getType());
        User sender = userMapper.selectUserByUserId(order.getSender_user_id());
        orderMap.put("sender_avatar", sender.getAvatar());

        // giftCard
        GiftCard giftCard = giftCardMapper.selectGiftCardById(order.getGift_card_id());

        resultMap.put("order", orderMap);
        resultMap.put("giftCard", giftCard);

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
        Order order = orderMapper.selectOrderDetailByUuid(uuid);
        if (order == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        JSONObject respObject = new JSONObject();

        String regionType = order.getRegion_type();
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

            // 已支付的订单，如果status为PAYING，则要改为WAIT_HAND_OVER
            String order_status = respObject.getJSONObject("request").getString("status");
            boolean payed = respObject.getJSONObject("request").getBoolean("payed");
            if (payed && order_status.equals("PAYING")) {
                respObject.getJSONObject("request").put("status", "WAIT_HAND_OVER");
                orderExpressMapper.updateOrderExpressStatusByUUID(uuid, "WAIT_HAND_OVER");
                order = orderMapper.selectOrderDetailByUuid(uuid);
            }
        }

        respObject.put("order", order);

        return APIUtil.getResponse(SUCCESS, respObject);
    }
}
