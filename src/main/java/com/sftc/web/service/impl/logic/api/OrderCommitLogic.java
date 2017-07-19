package com.sftc.web.service.impl.logic.api;

import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.mapper.AddressHistoryMapper;
import com.sftc.web.mapper.AddressMapper;
import com.sftc.web.mapper.OrderExpressMapper;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.AddressHistory;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.sfmodel.Address;
import com.sftc.web.model.sfmodel.Coordinate;
import com.sftc.web.model.sfmodel.Source;
import com.sftc.web.model.sfmodel.Target;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUBMIT_FAIL;
import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_CREATEORDER_URL;
import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;

@Component
public class OrderCommitLogic {

    private Gson gson = new Gson();
    private Logger logger = Logger.getLogger(this.getClass());

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private AddressHistoryMapper addressHistoryMapper;

    //////////////////// Public Method ////////////////////

    /**
     * 普通订单提交
     */
    public APIResponse normalOrderCommit(APIRequest request) {
        Object requestBody = request.getRequestParam();
        // Param Verify
        String paramVerifyMessage = orderCommitVerify(requestBody);
        if (paramVerifyMessage != null) { // Param Error
            return APIUtil.paramErrorResponse(paramVerifyMessage);
        }

        JSONObject requestObject = JSONObject.fromObject(requestBody);
        if (requestObject.containsKey("request")) { // 同城
            return normalSameOrderCommit(requestBody);
        } else { // 大网
            return normalNationOrderCommit(requestBody);
        }
    }

    /**
     * 好友订单提交
     */
    public APIResponse friendOrderCommit(APIRequest request) {
        Object requestBody = request.getRequestParam();
        // Param Verify
        String paramVerifyMessage = orderCommitVerify(requestBody);
        if (paramVerifyMessage != null) { // Param Error
            return APIUtil.paramErrorResponse(paramVerifyMessage);
        }

        JSONObject requestObject = JSONObject.fromObject(requestBody);
        if (requestObject.containsKey("request")) { // 同城
            return friendSameOrderCommit(requestObject);
        } else { // 大网
            return friendNationOrderCommit(requestObject);
        }
    }

    /**
     * 大网预约订单提交
     */
    public void nationOrderReserveCommit(int order_id) {

        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        for (OrderExpress oe : order.getOrderExpressList()) {
            if (Long.parseLong(oe.getReserve_time()) > System.currentTimeMillis()) return; // 还未到预约时间

            // 大网订单提交参数
            JSONObject sf = new JSONObject();
            sf.put("orderid", oe.getUuid());
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
            sf.put("express_type", order.getDistribution_method());
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

                if (resultObject.get("Message") != null || (messageType != null && messageType.contains("ERROR"))) {
                    logger.error("大网预约单提交失败: " + resultObject);
                } else {
                    // 存储快递信息
                    orderMapper.updateOrderRegionType(order.getId(), "REGION_NATION");
                    orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), oe.getUuid(), null);
                    orderExpressMapper.updateOrderExpressStatus(oe.getId(), "WAIT_HAND_OVER");
                    String ordernum = resultObject.getString("ordernum");
                    orderExpressMapper.updateOrderNumber(oe.getId(), ordernum);
                }
            }
        }
    }

    //////////////////// Private Method ////////////////////

    /// 订单提交接口验参
    private String orderCommitVerify(Object object) {
        JSONObject jsonObject = JSONObject.fromObject(object);
        boolean requestObject = jsonObject.containsKey("request");  // 同城
        boolean sfObject = jsonObject.containsKey("sf");            // 大网

        if (!jsonObject.containsKey("order")) {
            return "参数order不能为空";
        }

        if (!requestObject && !sfObject) {
            return "参数sf和request不能都为空";
        } else if (requestObject && sfObject) {
            return "参数sf和request不能同时存在";
        }

        return null;
    }

    /// 好友同城订单提交
    private APIResponse friendSameOrderCommit(JSONObject requestObject) {
        // Param
        int order_id = ((Double) requestObject.getJSONObject("order").get("order_id")).intValue();
        if (order_id < 0)
            return APIUtil.paramErrorResponse("order_id不能为空");

        String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        for (OrderExpress oe : order.getOrderExpressList()) {
            // 拼接同城订单参数中的 source 和 target
            Source source = new Source();
            Address address = new Address();
            address.setProvince(order.getSender_province());
            address.setCity(order.getSender_city());
            address.setRegion(order.getSender_area());
            address.setStreet(order.getSender_addr());
            address.setReceiver(order.getSender_name());
            address.setMobile(order.getSender_mobile());
            Coordinate coordinate = new Coordinate();
            coordinate.setLongitude(order.getLongitude());
            coordinate.setLatitude(order.getLatitude());
            source.setAddress(address);
            source.setCoordinate(coordinate);

            Target target = new Target();
            Address targetAddress = new Address();
            targetAddress.setProvince(oe.getShip_province());
            targetAddress.setCity(oe.getShip_city());
            targetAddress.setRegion(oe.getShip_area());
            targetAddress.setStreet(oe.getShip_addr());
            targetAddress.setReceiver(oe.getShip_name());
            targetAddress.setMobile(oe.getShip_mobile());
            Coordinate targetCoordinate = new Coordinate();
            targetCoordinate.setLongitude(oe.getLongitude());
            targetCoordinate.setLatitude(oe.getLatitude());
            target.setAddress(targetAddress);
            target.setCoordinate(targetCoordinate);

            requestObject.getJSONObject("request").put("source", source);
            requestObject.getJSONObject("request").put("target", target);
            if (reserve_time != null && !reserve_time.equals("")) {
                String reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserve_time, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                requestObject.getJSONObject("request").put("reserve_time", reserveTime);
            }

            /// Request
            Object tempObj = JSONObject.toBean(requestObject);
            // tempJsonObj 是为了保证对顺丰接口的请求体的完整，不能包含其它的键值对，例如接口的请求参数"order"
            JSONObject tempJsonObj = JSONObject.fromObject(tempObj);
            tempJsonObj.remove("order");
            // Param
            String paramStr = gson.toJson(JSONObject.fromObject(tempJsonObj));
            // POST
            HttpPost post = new HttpPost(SF_REQUEST_URL);
            String token = (String) requestObject.getJSONObject("request").getJSONObject("merchant").get("access_token");
            post.addHeader("PushEnvelope-Device-Token", token);
            String resultStr = APIPostUtil.post(paramStr, post);
            JSONObject responseObject = JSONObject.fromObject(resultStr);

            if (!responseObject.containsKey("error")) {
                String uuid = (String) responseObject.getJSONObject("request").get("uuid");
                // 获取sf返回的编号
                String request_num = responseObject.getJSONObject("request").getString("request_num");

                /// 数据库操作
                // 订单表更新订单区域类型
                orderMapper.updateOrderRegionType(order_id, "REGION_SAME");
                // 快递表更新uuid和预约时间
                orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), uuid, reserve_time);
                String order_tiem = Long.toString(System.currentTimeMillis());
                orderExpressMapper.updateOrderTime(uuid, order_tiem);
                orderExpressMapper.updateOrderNumber(oe.getId(), request_num);

                // 插入地址
                setupAddress(order, oe);

            } else { // error
                return APIUtil.getResponse(SUBMIT_FAIL, responseObject);
            }
        }

        order = orderMapper.selectOrderDetailByOrderId(order_id);

        return APIUtil.getResponse(SUCCESS, order);
    }

    /// 好友大网订单提交
    private APIResponse friendNationOrderCommit(JSONObject requestObject) {
        // handle param
        int order_id = ((Double) requestObject.getJSONObject("order").get("order_id")).intValue();
        if (order_id < 0)
            return APIUtil.paramErrorResponse("order_id不能为空");

        String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
        orderMapper.updateOrderRegionType(order_id, "REGION_NATION");

        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        for (OrderExpress oe : order.getOrderExpressList()) {
            // 拼接大网订单地址参数
            JSONObject sf = requestObject.getJSONObject("sf");
            sf.put("orderid", oe.getUuid());
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
                if (reserve_time != null && !reserve_time.equals("")) { // 预约件处理
                    orderExpressMapper.updateOrderExpressUuidAndReserveTimeById(oe.getId(), null, reserve_time);
                } else {
                    // 立即提交订单
                    String paramStr = gson.toJson(JSONObject.fromObject(sf));
                    HttpPost post = new HttpPost(SF_CREATEORDER_URL);
                    post.addHeader("Authorization", "bearer " + SFTokenHelper.getToken());
                    String resultStr = APIPostUtil.post(paramStr, post);
                    JSONObject jsonObject = JSONObject.fromObject(resultStr);
                    String messageType = (String) jsonObject.get("Message_Type");

                    if (messageType != null && messageType.contains("ERROR")) {
                        return APIUtil.submitErrorResponse("下单失败", jsonObject);
                    } else {
                        // 存储订单信息
                        String order_time = Long.toString(System.currentTimeMillis());
                        orderExpressMapper.updateOrderTime(oe.getUuid(), order_time);

                        String ordernum = jsonObject.getString("ordernum");
                        orderExpressMapper.updateOrderNumber(oe.getId(), ordernum);
                        orderExpressMapper.updateOrderExpressStatus(oe.getId(), "WAIT_HAND_OVER");

                        // 插入地址
                        setupAddress(order, oe);
                    }
                }
            }
        }
        order = orderMapper.selectOrderDetailByOrderId(order_id);

        return APIUtil.getResponse(SUCCESS, order);
    }

    /// 普通同城订单提交
    private APIResponse normalSameOrderCommit(Object object) {

        JSONObject reqObject = JSONObject.fromObject(object);

        // 预约时间处理
        String reserve_time = (String) reqObject.getJSONObject("order").get("reserve_time");
        if (reserve_time != null && !reserve_time.equals("")) {
            String reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserve_time, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            reqObject.getJSONObject("request").put("reserve_time", reserveTime);
        }

        APIStatus status = SUCCESS;

        Order order = new Order(
                Long.toString(System.currentTimeMillis()),
                (String) reqObject.getJSONObject("request").get("pay_type"),
                (String) reqObject.getJSONObject("request").get("product_type"),
                0.0,
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("receiver"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("mobile"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("province"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("city"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("region"),
                (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("street"),
                (Double) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("coordinate").get("longitude"),
                (Double) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("coordinate").get("latitude"),
                "ORDER_BASIS",
                Integer.parseInt((String) reqObject.getJSONObject("order").get("sender_user_id"))
        );
        order.setImage((String) reqObject.getJSONObject("order").get("image"));
        order.setVoice((String) reqObject.getJSONObject("order").get("voice"));
        order.setWord_message((String) reqObject.getJSONObject("order").get("word_message"));
        order.setGift_card_id(Integer.parseInt((String) reqObject.getJSONObject("order").get("gift_card_id")));
        order.setVoice_time(Integer.parseInt((String) reqObject.getJSONObject("order").get("voice_time")));
        order.setRegion_type("REGION_SAME");

        HttpPost post = new HttpPost(SF_REQUEST_URL);
        post.addHeader("PushEnvelope-Device-Token", (String) reqObject.getJSONObject("request").getJSONObject("merchant").get("access_token"));
        JSONObject tempObject = JSONObject.fromObject(object);
        tempObject.remove("order");
        String requestSFParamStr = gson.toJson(tempObject);
        JSONObject respObject = JSONObject.fromObject(APIPostUtil.post(requestSFParamStr, post));

        if (!(respObject.containsKey("error") || respObject.containsKey("errors"))) {
            // 插入订单表
            orderMapper.addOrder(order);

            // 插入快递表
            OrderExpress orderExpress = new OrderExpress(
                    Long.toString(System.currentTimeMillis()),
                    Long.toString(System.currentTimeMillis()),
                    respObject.getJSONObject("request").getString("request_num"),
                    reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").getString("receiver"),
                    reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").getString("mobile"),
                    reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").getString("province"),
                    reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").getString("city"),
                    reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").getString("region"),
                    reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").getString("street"),
                    reqObject.getJSONObject("request").getJSONArray("packages").getJSONObject(0).getString("weight"),
                    reqObject.getJSONObject("request").getJSONArray("packages").getJSONObject(0).getString("type"),
                    respObject.getJSONObject("request").getString("status"),
                    Integer.parseInt((String) reqObject.getJSONObject("order").get("sender_user_id")),
                    order.getId(),
                    respObject.getJSONObject("request").getString("uuid"),
                    reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").getDouble("latitude"),
                    reqObject.getJSONObject("request").getJSONObject("target").getJSONObject("coordinate").getDouble("longitude")
            );
            orderExpress.setReserve_time(reserve_time);
            orderExpressMapper.addOrderExpress(orderExpress);

            // 插入地址
            setupAddress(order, orderExpress);

            // 返回结果添加订单编号
            respObject.put("order_id", order.getId());

        } else {
            status = SUBMIT_FAIL;
        }

        return APIUtil.getResponse(status, respObject);
    }

    /// 普通大网订单提交
    private APIResponse normalNationOrderCommit(Object object) {

        String orderId = SFOrderHelper.getOrderNumber();
        APIStatus status = SUCCESS;

        JSONObject requestObject = JSONObject.fromObject(object);
        JSONObject orderObject = requestObject.getJSONObject("order");
        JSONObject sf = requestObject.getJSONObject("sf");

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

        sf.put("orderid", orderId);

        String str = gson.toJson(requestObject.getJSONObject("sf"));

        // 插入订单表
        Order order = new Order(
                Long.toString(System.currentTimeMillis()),
                (String) sf.get("pay_method"),
                "",
                0,
                (String) sf.get("j_contact"),
                (String) sf.get("j_tel"),
                (String) sf.get("j_province"),
                (String) sf.get("j_city"),
                (String) sf.get("j_county"),
                (String) sf.get("j_address"),
                0,
                0,
                "ORDER_BASIS",
                Integer.parseInt((String) orderObject.get("sender_user_id"))
        );
        order.setImage((String) orderObject.get("image"));
        order.setVoice((String) orderObject.get("voice"));
        order.setWord_message((String) orderObject.get("word_message"));
        order.setGift_card_id(Integer.parseInt((String) orderObject.get("gift_card_id")));
        order.setVoice_time(Integer.parseInt((String) orderObject.get("voice_time")));
        order.setRegion_type("REGION_NATION");
        orderMapper.addOrder(order);

        // 插入快递表
        OrderExpress orderExpress = new OrderExpress(
                Long.toString(System.currentTimeMillis()),
                Long.toString(System.currentTimeMillis()),
                "",
                (String) sf.get("d_contact"),
                (String) sf.get("d_tel"),
                (String) sf.get("d_province"),
                (String) sf.get("d_city"),
                (String) sf.get("d_county"),
                (String) sf.get("d_address"),
                "",
                "",
                "WAIT_HAND_OVER",
                Integer.parseInt((String) orderObject.get("sender_user_id")),
                order.getId(),
                orderId,
                0.0,
                0.0
        );
        orderExpress.setReserve_time((String) requestObject.getJSONObject("order").get("reserve_time"));
        orderExpressMapper.addOrderExpress(orderExpress);

        // 插入地址
        setupAddress(order, orderExpress);

        String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
        JSONObject responseObject = new JSONObject();
        if (reserve_time == null || reserve_time.equals("")) {
            // POST
            HttpPost post = new HttpPost(SF_CREATEORDER_URL);
            post.addHeader("Authorization", "bearer " + SFTokenHelper.getToken());
            String res = APIPostUtil.post(str, post);
            responseObject = JSONObject.fromObject(res);

            if (responseObject.get("Message") != null || (responseObject.get("Message_Type") != null && ((String) responseObject.get("Message_Type")).contains("ERROR"))) {
                status = SUBMIT_FAIL;
            } else {
                // 返回结果添加订单编号
                responseObject.put("order_id", order.getId());
                String ordernum = responseObject.getString("ordernum");
                orderExpressMapper.updateOrderNumber(orderExpress.getId(), ordernum);
            }
        } else { // 预约件
            responseObject.put("order_id", order.getId());
            responseObject.put("message", "大网订单预约成功");
        }

        return APIUtil.getResponse(status, responseObject);
    }

    /// 插入地址
    private void setupAddress(Order order, OrderExpress oe) {
        // 插入地址表
        com.sftc.web.model.Address address = new com.sftc.web.model.Address(oe);
        addressMapper.addAddress(address);

        // 插入历史地址表
        List<com.sftc.web.model.Address> addressList = addressMapper.selectAddressByPhoneAndLongitudeAndLatitude(oe.getShip_mobile(), oe.getLongitude(), oe.getLatitude());
        if (addressList.size() == 1) { // 插入的时候就根据手机号和经纬度去重，相同的手机号和经纬度 只会存在一个历史地址记录
            AddressHistory addressHistory = new AddressHistory();
            addressHistory.setUser_id(order.getSender_user_id());
            addressHistory.setAddress_id(address.getId());
            addressHistory.setCreate_time(Long.toString(System.currentTimeMillis()));
            addressHistory.setIs_delete(0);
            int is_mystery = order.getOrder_type().equals("ORDER_MYSTERY") ? 1 : 0;
            addressHistory.setIs_mystery(is_mystery);
            addressHistoryMapper.insertAddressHistory(addressHistory);
        }
    }
}
