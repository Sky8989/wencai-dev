package com.sftc.web.service.impl.logic.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sftc.tools.api.*;
import com.sftc.tools.common.DateUtils;
import com.sftc.tools.common.EmojiFilter;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.mapper.*;
import com.sftc.web.model.AddressBook;
import com.sftc.web.model.AddressHistory;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.sfmodel.Address;
import com.sftc.web.model.sfmodel.Coordinate;
import com.sftc.web.model.sfmodel.Source;
import com.sftc.web.model.sfmodel.Target;
import com.sftc.web.service.MessageService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;
import java.util.TreeMap;

import static com.sftc.tools.api.APIStatus.SUBMIT_FAIL;
import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_CREATEORDER_URL;
import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;
import static com.sftc.tools.constant.WXConstant.WX_template_id_1;

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
    @Resource
    private MessageService messageService;
    @Resource
    private AddressBookMapper addressBookMapper;

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

        // 增加对emoji的过滤
        if (requestObject.containsKey("request")) { // 同城
            boolean containsEmoji = EmojiFilter.containsEmoji(requestObject.getJSONObject("request").getString("packages"));
            if (containsEmoji) return APIUtil.paramErrorResponse("Don't input emoji");
        }

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

        // 增加对emoji的过滤
        if (requestObject.containsKey("request")) { // 同城
            boolean containsEmoji = EmojiFilter.containsEmoji(requestObject.getJSONObject("request").getString("packages"));
            if (containsEmoji) return APIUtil.paramErrorResponse("Don't input emoji");
        }

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

            //过滤 不在预约时间周期内的订单      当前时间>X>当前时间-1800000的订单才下单 ，处于其补集区间的订单则跳出
            if (Long.parseLong(oe.getReserve_time()) >= System.currentTimeMillis()
                    || Long.parseLong(oe.getReserve_time()) <= System.currentTimeMillis() - 1800000)
                return; // 1未到预约时间,则不下单 2 预约时间超出上一个下单时间周期，属于过期订单 也不下单

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
                    //reserve_time置为null,不修改，sql中已经非null处理
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
    public APIResponse friendSameOrderCommit(JSONObject requestObject) {
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

            // TODO 把门牌号加到下单的参数json中
            Object removeStreet = requestObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").remove("street");
            String newStreet = removeStreet.toString() + order.getSupplementary_info();
            requestObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").put("street", newStreet);
            Object removeStreet2 = requestObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").remove("street");
            String newStreet2 = removeStreet2.toString() + order.getSupplementary_info();
            requestObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").put("street", newStreet2);


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

                //更新订单状态
                orderExpressMapper.updateOrderExpressStatus(oe.getId(), responseObject.getJSONObject("request").getString("status"));

                // 插入地址
                //setupAddress(order, oe);
                //使用新的地址插入工具
                //setupAddress2(order, oe);

            } else { // error
                //手动操作事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
                    // 处理street 和 门牌号的拼接
                    Object j_address = sf.remove("j_address");
                    Object d_address = sf.remove("d_address");
                    sf.put("j_address", j_address + order.getSupplementary_info());
                    sf.put("d_address", d_address + oe.getSupplementary_info());


                    // 立即提交订单
                    String paramStr = gson.toJson(JSONObject.fromObject(sf));
                    HttpPost post = new HttpPost(SF_CREATEORDER_URL);
                    post.addHeader("Authorization", "bearer " + SFTokenHelper.getToken());
                    String resultStr = APIPostUtil.post(paramStr, post);
                    JSONObject jsonObject = JSONObject.fromObject(resultStr);
                    String messageType = (String) jsonObject.get("Message_Type");

                    if (messageType != null && messageType.contains("ERROR")) {
                        //手动操作事务回滚
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return APIUtil.submitErrorResponse("下单失败", jsonObject);
                    } else {
                        // 存储订单信息
                        String order_time = Long.toString(System.currentTimeMillis());
                        orderExpressMapper.updateOrderTime(oe.getUuid(), order_time);

                        String ordernum = jsonObject.getString("ordernum");
                        orderExpressMapper.updateOrderNumber(oe.getId(), ordernum);
                        orderExpressMapper.updateOrderExpressStatus(oe.getId(), "WAIT_HAND_OVER");

                        // 插入地址
                        //setupAddress(order, oe);
                        //使用新的地址插入工具
                        //setupAddress2(order, oe);
                    }
                }
            }
        }
        order = orderMapper.selectOrderDetailByOrderId(order_id);

        return APIUtil.getResponse(SUCCESS, order);
    }

    /// 普通同城订单提交
    public APIResponse normalSameOrderCommit(Object object) {

        JSONObject reqObject = JSONObject.fromObject(object);
        JSONObject requestOBJ = reqObject.getJSONObject("request");
        JSONObject orderOBJ = reqObject.getJSONObject("order");
        JSONObject sourceOBJ = requestOBJ.getJSONObject("source");
        JSONObject targetOBJ = requestOBJ.getJSONObject("target");
        JSONObject sourceAddressOBJ = sourceOBJ.getJSONObject("address");
        JSONObject targetAddressOBJ = targetOBJ.getJSONObject("address");

        //处理supplementary_info非必填项的问题
        if (!sourceAddressOBJ.containsKey("supplementary_info")) {
            sourceAddressOBJ.put("supplementary_info", "");
        }
        if (!targetAddressOBJ.containsKey("supplementary_info")) {
            targetAddressOBJ.put("supplementary_info", "");
        }

        // 预约时间处理
        String reserve_time = (String) reqObject.getJSONObject("order").get("reserve_time");
        if (reserve_time != null && !reserve_time.equals("")) {
//            String reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserve_time, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String reserveTime = DateUtils.iSO8601DateWithTimeStampAndFormat(reserve_time, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            reqObject.getJSONObject("request").put("reserve_time", reserveTime);
        }

        APIStatus status = SUCCESS;


        Order order = new Order(
                Long.toString(System.currentTimeMillis()),
                (String) requestOBJ.get("pay_type"),
                (String) requestOBJ.get("product_type"),
                0.0,
                (String) sourceAddressOBJ.get("receiver"),
                (String) sourceAddressOBJ.get("mobile"),
                (String) sourceAddressOBJ.get("province"),
                (String) sourceAddressOBJ.get("city"),
                (String) sourceAddressOBJ.get("region"),
                (String) sourceAddressOBJ.get("street"),
                (String) sourceAddressOBJ.get("supplementary_info"), //增加门牌号
                (Double) sourceOBJ.getJSONObject("coordinate").get("longitude"),
                (Double) sourceOBJ.getJSONObject("coordinate").get("latitude"),
                "ORDER_BASIS",
                Integer.parseInt((String) orderOBJ.get("sender_user_id"))
        );
        order.setImage((String) orderOBJ.get("image"));
        order.setVoice((String) orderOBJ.get("voice"));
        order.setWord_message((String) orderOBJ.get("word_message"));
        order.setGift_card_id(Integer.parseInt((String) orderOBJ.get("gift_card_id")));
        order.setVoice_time(Integer.parseInt((String) orderOBJ.get("voice_time")));
        order.setRegion_type("REGION_SAME");

        HttpPost post = new HttpPost(SF_REQUEST_URL);
        post.addHeader("PushEnvelope-Device-Token", (String) requestOBJ.getJSONObject("merchant").get("access_token"));
//        JSONObject tempObject = JSONObject.fromObject(object);
//        tempObject.remove("order");
        //使用修改后的请求体

        JSONObject tempObject = JSONObject.fromObject(reqObject);
        tempObject.remove("order");
//         TODO 把门牌号加到下单的参数json中
        String oldSourceAddressStreet = sourceAddressOBJ.getString("street");
        String oldTargetAddressStreet = targetAddressOBJ.getString("street");

        // TODO 把门牌号加到下单的参数json中
        Object removeStreet = tempObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").remove("street");
        String newStreet = removeStreet.toString() + order.getSupplementary_info();
        tempObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").put("street", newStreet);
        Object removeStreet2 = tempObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").remove("street");
        String newStreet2 = removeStreet2.toString() + tempObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").getString("supplementary_info");
        tempObject.getJSONObject("request").getJSONObject("target").getJSONObject("address").put("street", newStreet2);


        //向sf下单
        String requestSFParamStr = gson.toJson(tempObject);
        JSONObject respObject = JSONObject.fromObject(APIPostUtil.post(requestSFParamStr, post));

        if (!(respObject.containsKey("error") || respObject.containsKey("errors"))) {
            // 插入订单表
            orderMapper.addOrder2(order);

            // 插入快递表
            OrderExpress orderExpress = new OrderExpress(
                    Long.toString(System.currentTimeMillis()),
                    Long.toString(System.currentTimeMillis()),
                    respObject.getJSONObject("request").getString("request_num"),
                    targetAddressOBJ.getString("receiver"),
                    targetAddressOBJ.getString("mobile"),
                    targetAddressOBJ.getString("province"),
                    targetAddressOBJ.getString("city"),
                    targetAddressOBJ.getString("region"),
                    oldTargetAddressStreet,
                    targetAddressOBJ.getString("supplementary_info"),
                    requestOBJ.getJSONArray("packages").getJSONObject(0).getString("weight"),
                    requestOBJ.getJSONArray("packages").getJSONObject(0).getString("type"),
                    respObject.getJSONObject("request").getString("status"),
                    Integer.parseInt((String) reqObject.getJSONObject("order").get("sender_user_id")),
                    order.getId(),
                    respObject.getJSONObject("request").getString("uuid"),
                    targetOBJ.getJSONObject("coordinate").getDouble("latitude"),
                    targetOBJ.getJSONObject("coordinate").getDouble("longitude")
            );
            orderExpress.setReserve_time(reserve_time);
            orderExpressMapper.addOrderExpress2(orderExpress);

            // 插入地址
            //setupAddress(order, orderExpress);
            /**
             * 使用地址映射插入工具
             */
            String create_time = Long.toString(System.currentTimeMillis());
            setupAddress2(order, orderExpress);

            // 返回结果添加订单编号
            respObject.put("order_id", order.getId());

            /// 发送微信模板消息
            if (reqObject.getJSONObject("order").containsKey("form_id")
                    && !"".equals(reqObject.getJSONObject("order").getString("form_id"))) {
                String[] messageArr = new String[2];
                messageArr[0] = respObject.getJSONObject("request").getString("request_num");
                messageArr[1] = "您的顺丰订单下单成功(同城)！寄件人是："
                        + (String) reqObject.getJSONObject("request").getJSONObject("source").getJSONObject("address").get("receiver");
                String form_id = reqObject.getJSONObject("order").getString("form_id");
                messageService.sendWXTemplateMessage(Integer.parseInt((String) reqObject.getJSONObject("order").get("sender_user_id")),
                        messageArr, "", form_id, WX_template_id_1);
            }

        } else {
            status = SUBMIT_FAIL;
            //手动操作事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return APIUtil.submitErrorResponse("提交失败", respObject);
        }

        return APIUtil.getResponse(status, respObject);
    }

    /// 普通大网订单提交
    public APIResponse normalNationOrderCommit(Object object) {

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

//        String str = gson.toJson(requestObject.getJSONObject("sf"));
        //处理supplementary_info非必填项的问题
        if (!sf.containsKey("j_supplementary_info")) {
            sf.put("j_supplementary_info", "");
        }
        if (!sf.containsKey("d_supplementary_info")) {
            sf.put("d_supplementary_info", "");
        }


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
                sf.getString("j_supplementary_info"),//增加门牌号
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
        orderMapper.addOrder2(order);

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
                sf.getString("d_supplementary_info"),//增加门牌号
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
        orderExpressMapper.addOrderExpress2(orderExpress);

        // 插入地址
//        setupAddress(order, orderExpress);
        nationInsertAddressBookAndAddressHistory(order.getSender_user_id(), sf, Long.toString(System.currentTimeMillis()));

        String reserve_time = (String) requestObject.getJSONObject("order").get("reserve_time");
        JSONObject responseObject = new JSONObject();
        if (reserve_time == null || reserve_time.equals("")) {
            // 处理 street和门牌号的拼接
            JSONObject paramTemp = JSONObject.fromObject(sf);
            Object j_address = paramTemp.remove("j_address");
            paramTemp.put("j_address", j_address + paramTemp.getString("j_supplementary_info"));
            String str = gson.toJson(paramTemp);

            // POST
            HttpPost post = new HttpPost(SF_CREATEORDER_URL);
            post.addHeader("Authorization", "bearer " + SFTokenHelper.getToken());
            String res = APIPostUtil.post(str, post);
            responseObject = JSONObject.fromObject(res);

            if (responseObject.get("Message") != null || (responseObject.get("Message_Type") != null && ((String) responseObject.get("Message_Type")).contains("ERROR"))) {
                status = SUBMIT_FAIL;
                //手动操作事务回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return APIUtil.submitErrorResponse("SF提示错误", responseObject);
            } else {
                // 返回结果添加订单编号
                String ordernum = responseObject.getString("ordernum");
                orderExpressMapper.updateOrderNumber(orderExpress.getId(), ordernum);
                orderExpress.setOrder_number(ordernum);

            }
        } else { // 预约件
            responseObject.put("message", "大网订单预约成功");
        }

        /// 发送微信模板消息
        if (orderObject.containsKey("form_id") && !"".equals(orderObject.getString("form_id"))) {
            String[] messageArr = new String[2];
            messageArr[0] = orderExpress.getOrder_number();
            messageArr[1] = "您的顺丰订单下单成功(大网)！寄件人是：" + sf.get("j_contact");
            String form_id = orderObject.getString("form_id");
            messageService.sendWXTemplateMessage(Integer.parseInt((String) orderObject.get("sender_user_id")),
                    messageArr, "", form_id, WX_template_id_1);
        }

        Order orderData = orderMapper.selectOrderDetailByOrderId(order.getId());
        responseObject.put("order", orderData);

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


    /// TODO: 插入地址簿  要去重
    // 通用地址簿插入utils
    public void insertAddressBookUtils(
            String address_type, String address_book_type, int user_id_sender, int user_id_ship, String name, String phone,
            String province, String city, String area, String address, String supplementary_info,
            String create_time, double longitude, double latitude) {

        com.sftc.web.model.Address addressParam = new com.sftc.web.model.Address(
                user_id_ship, name, phone,
                province, city, area, address, supplementary_info,
                longitude, latitude, create_time
        );

        // 查找重复信息
        List<AddressBook> addressBookList = addressBookMapper.selectAddressForRemoveDuplicate(user_id_sender,
                address_type, address_book_type, name, phone,
                province, city, area, address, supplementary_info);

        if (addressBookList.size() == 0) {// 0代表无重复信息
            //执行插入操作
            addressMapper.addAddress(addressParam);
            AddressBook addressBook = new AddressBook(user_id_sender, addressParam.getId(), 0, 0, address_type, address_book_type, create_time);
            addressBookMapper.insert(addressBook);
        }
        // addressBookList的size如果大于0 代表已经有相同地址
        // 不做插入处理
    }

    /// 普通同城下单使用的 一键添加2个地址簿 1个历史地址
    private void tempInsertAddressBookAndAddressHistory(
            String create_time, JSONObject orderOBJ
            , JSONObject sourceAddressOBJ, JSONObject sourceOBJ
            , JSONObject targetAddressOBJ, JSONObject targetOBJ) {
        // 插入地址簿 寄件人
        insertAddressBookUtils("address_book", "sender",
                orderOBJ.getInt("sender_user_id"),
                orderOBJ.getInt("sender_user_id"),
                sourceAddressOBJ.getString("receiver"),
                sourceAddressOBJ.getString("mobile"),
                sourceAddressOBJ.getString("province"),
                sourceAddressOBJ.getString("city"),
                sourceAddressOBJ.getString("region"),
                sourceAddressOBJ.getString("street"),
                sourceAddressOBJ.getString("supplementary_info"),
                create_time,
                (Double) sourceOBJ.getJSONObject("coordinate").get("longitude"),
                (Double) sourceOBJ.getJSONObject("coordinate").get("latitude")
        );
        // 插入地址簿 收件人
        insertAddressBookUtils("address_book", "ship",
                orderOBJ.getInt("sender_user_id"),// 这里的地址是属于寄件人的
                orderOBJ.getInt("sender_user_id"),// 这里的地址是属于寄件人的
                targetAddressOBJ.getString("receiver"),
                targetAddressOBJ.getString("mobile"),
                targetAddressOBJ.getString("province"),
                targetAddressOBJ.getString("city"),
                targetAddressOBJ.getString("region"),
                targetAddressOBJ.getString("street"),
                targetAddressOBJ.getString("supplementary_info"),
                create_time,
                (Double) targetOBJ.getJSONObject("coordinate").get("longitude"),
                (Double) targetOBJ.getJSONObject("coordinate").get("latitude")
        );
        // 插入历史地址
        insertAddressBookUtils("address_history", "address_history",
                orderOBJ.getInt("sender_user_id"),// 这里的地址是属于寄件人的
                0,// 这里的地址是属于寄件人的
                targetAddressOBJ.getString("receiver"),
                targetAddressOBJ.getString("mobile"),
                targetAddressOBJ.getString("province"),
                targetAddressOBJ.getString("city"),
                targetAddressOBJ.getString("region"),
                targetAddressOBJ.getString("street"),
                targetAddressOBJ.getString("supplementary_info"),
                create_time,
                (Double) targetOBJ.getJSONObject("coordinate").get("longitude"),
                (Double) targetOBJ.getJSONObject("coordinate").get("latitude")
        );
    }

    /// 普通大网下单使用的 一键添加2个地址簿 1个历史地址
    private void nationInsertAddressBookAndAddressHistory(int user_id_sender, JSONObject sf, String create_time) {
        // 插入地址簿 寄件人
        insertAddressBookUtils("address_book", "sender",
                user_id_sender,
                user_id_sender,
                sf.getString("j_contact"),
                sf.getString("j_mobile"),
                sf.getString("j_province"),
                sf.getString("j_city"),
                sf.getString("j_county"),
                sf.getString("j_address"),
                sf.getString("j_supplementary_info"),
                create_time,
                0,
                0
        );
        // 插入地址簿 收件人
        insertAddressBookUtils("address_book", "ship",
                user_id_sender,// 这里的地址是属于寄件人的
                user_id_sender,// 这里的地址是属于寄件人的
                sf.getString("d_contact"),
                sf.getString("d_mobile"),
                sf.getString("d_province"),
                sf.getString("d_city"),
                sf.getString("d_county"),
                sf.getString("d_address"),
                sf.getString("d_supplementary_info"),
                create_time,
                0,
                0
        );
        // 插入历史地址
        insertAddressBookUtils("address_history", "address_history",
                user_id_sender,
                0,// 历史地址都是自己写的
                sf.getString("d_contact"),
                sf.getString("d_mobile"),
                sf.getString("d_province"),
                sf.getString("d_city"),
                sf.getString("d_county"),
                sf.getString("d_address"),
                sf.getString("d_supplementary_info"),
                create_time,
                0,
                0
        );
    }

    private void setupAddress2(Order order, OrderExpress oe) {
        // 插入地址簿 寄件人
        insertAddressBookUtils("address_book", "sender",
                order.getSender_user_id(),
                order.getSender_user_id(),// 地址是收件人的
                order.getSender_name(),
                order.getSender_mobile(),
                order.getSender_province(),
                order.getSender_city(),
                order.getSender_area(),
                order.getSender_addr(),
                order.getSupplementary_info(),
                order.getCreate_time(), order.getLongitude(),
                order.getLatitude()
        );
        // 插入地址簿 收件人
        insertAddressBookUtils("address_book", "ship",
                order.getSender_user_id(),
                oe.getShip_user_id(),//地址是寄件人的
                oe.getShip_name(),
                oe.getShip_mobile(),
                oe.getShip_province(),
                oe.getShip_city(),
                oe.getShip_area(),
                oe.getShip_addr(),
                oe.getSupplementary_info(),
                oe.getCreate_time(),
                oe.getLongitude(),
                oe.getLatitude());
        // 插入历史地址
        insertAddressBookUtils("address_history", "address_history",
                order.getSender_user_id(),
                oe.getShip_user_id(),
                order.getSender_name(),
                order.getSender_mobile(),
                order.getSender_province(),
                order.getSender_city(),
                order.getSender_area(),
                order.getSender_addr(),
                order.getSupplementary_info(),
                order.getCreate_time(), order.getLongitude(),
                order.getLatitude()
        );
    }
}

