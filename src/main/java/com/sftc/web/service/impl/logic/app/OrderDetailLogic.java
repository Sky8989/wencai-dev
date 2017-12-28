package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.ApiGetUtil;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.tools.sf.SfExpressHelper;
import com.sftc.tools.utils.AddressDistanceUtil;
import com.sftc.tools.utils.OrderStateSyncUtil;
import com.sftc.web.dao.mybatis.*;
import com.sftc.web.enumeration.express.PackageType;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.dto.PackageMessageDTO;
import com.sftc.web.model.entity.GiftCard;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.entity.OrderExpressTransform;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.vo.swaggerRequest.SourceDistanceVO;
import com.sftc.web.model.vo.swaggerRequest.TargetDistanceVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.SfConstant.SF_CONSTANTS_URL;
import static com.sftc.tools.sf.SfTokenHelper.COMMON_ACCESSTOKEN;

@Component
public class OrderDetailLogic {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private IndexMapper indexMapper;
    @Resource
    private OrderExpressTransformMapper orderExpressTransformMapper;

    private static final String CONSTANTS_STR = "BASICDATA";
    private static final String SF_CONSTANT = "constant";
    private static final String SF_VALUE = "value";
    private static final String PACKAGE_TYPE = "PACKAGE_TYPE";

    /**
     * 订单详情接口
     */
    public ApiResponse selectOrderDetail(String orderId) {

        OrderDTO orderDTO1 = orderMapper.selectOrderDetailByOrderId(orderId);
        if (orderDTO1 == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }

        User user = userMapper.getUuidAndtoken(orderId);
        String accessToken = user.getToken().getAccess_token();
        if (StringUtils.isBlank(accessToken)) {
            accessToken = COMMON_ACCESSTOKEN;
        }
        List<OrderExpress> orderExpressList = orderExpressMapper.findAllOrderExpressByOrderId(orderId);
        ApiResponse apiResponse = new OrderStateSyncUtil().syncOrderState(orderExpressList, accessToken, orderMapper, orderExpressMapper);
        if (apiResponse != null) {
            return apiResponse;
        }

        OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(orderId);
        JSONObject respObject = new JSONObject();
        if (orderDTO == null) {
            return ApiUtil.getResponse(SUCCESS, null);
        }
        List<OrderExpressDTO> orderExpress = orderDTO.getOrderExpressList();

        //int一：记录有收件人的包裹
        int i = 0;
        //List<OrderExpressDTO>二：记录空包裹数组下标
        List<OrderExpressDTO> emptyOrderExpress = new ArrayList<>();
        for (OrderExpressDTO orderExpressDTO : orderExpress) {
            if (StringUtils.isNotBlank(orderExpressDTO.getShip_user_uuid())) {
                User receiver = userMapper.selectUserByUserUUId(orderExpressDTO.getShip_user_uuid());
                if (receiver != null && receiver.getAvatar() != null) {
                    // 扩展收件人头像
                    orderExpressDTO.setShip_avatar(receiver.getAvatar());
                }
            }
            //判断单号和路由
            //记录
            if (StringUtils.isNotBlank(orderExpressDTO.getOrder_number()) && "CANCELED".equals(orderExpressDTO.getRoute_state())) {
                i++;
            } else {
                //排除有收件人，路由不是CANCELED
                if (StringUtils.isBlank(orderExpressDTO.getOrder_number()) && !"CANCELED".equals(orderExpressDTO.getRoute_state())) {
                    emptyOrderExpress.add(orderExpressDTO);
                }
            }
        }
        //1.判断收件人包裹>0  遍历修改
        if (i > 0) {
            for (OrderExpressDTO orderExpressDTO : emptyOrderExpress) {
                orderExpressMapper.updateRouteState("CANCELED", orderExpressDTO.getUuid());
            }
        }
        //获取包裹信息
        ApiResponse errorResponse = setPackageType(orderDTO);
        if (errorResponse != null) {
            return errorResponse;
        }
        // order
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        String resultJson = new Gson().toJson(orderDTO);
        Map<String, Object> orderMap = g.fromJson(resultJson, new TypeToken<Map<String, Object>>() {
        }.getType());
        User sender = userMapper.selectUserByUserUUId(orderDTO.getSender_user_uuid());
        orderMap.put("sender_avatar", sender.getAvatar());

        // giftCard
        GiftCard giftCard = indexMapper.selectGiftCardById(orderDTO.getGift_card_id());
        respObject.put("order", orderMap);
        respObject.put("giftCard", giftCard);

        return ApiUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 订单快递详情
     */
    public ApiResponse selectExpressDetail(String uuid) {

        if (StringUtils.isBlank(uuid)) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "uuid不能为空");
        }

        OrderDTO order = orderMapper.selectOrderDetailByUuid(uuid);
        if (order == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }

        // 同城订单需要access_token
        User user = userMapper.getUuidAndtoken(order.getId());
        String accessToken = user.getToken().getAccess_token();
        if (StringUtils.isBlank(accessToken)) {
            accessToken = COMMON_ACCESSTOKEN;
        }
        JSONObject respObject = SfExpressHelper.getExpressDetail(uuid, accessToken);

        List<OrderExpress> orderExpressList = orderExpressMapper.findAllOrderExpressByOrderId(order.getId());
        //同步路由状态
        ApiResponse apiResponse = new OrderStateSyncUtil().syncOrderState(orderExpressList, accessToken, orderMapper, orderExpressMapper);
        if (apiResponse != null) {
            return apiResponse;
        }
        order = orderMapper.selectOrderDetailByUuid(uuid);
        //获取包裹信息
        ApiResponse errorResponse = setPackageType(order);
        if (errorResponse != null) {
            return errorResponse;
        }
        OrderExpressTransform orderExpressTransform = orderExpressTransformMapper.selectExpressTransformByUUID(uuid);
        respObject.put("transform", orderExpressTransform);
        respObject.put("order", order);
  /*--------------------------------------------------------------- 增加快递百分比字段 ------------------------------------------------------------------------------*/
        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        //筛选派送中的快递
        if (CustomConstant.DELIVERING.equals(orderExpress.getRoute_state())) {
            Double totalDistance; //总距离 寄件人与收件人距离
            Double deliveringDistance; //当前距离 小哥与收件人的距离
            Float distancePercent; //派送百分比 当前距离/总距离
            JSONObject request = respObject.getJSONObject("request");

            //vehicle 为小哥位置信息
            if (request.containsKey(CustomConstant.VEHICLE)) {
                Map<String, Object> map = AddressDistanceUtil.getCoordinate(request.getJSONObject("source").getJSONObject("coordinate"),
                        request.getJSONObject("target").getJSONObject("coordinate"));
                try {
                    totalDistance = AddressDistanceUtil.getAddressDistance((SourceDistanceVO) map.get("sourceDistance"),
                            (TargetDistanceVO) map.get("targetDistance"));
                    JSONObject vehicle = request.getJSONObject("vehicle");
                    Map<String, Object> deliverMap = AddressDistanceUtil.getCoordinate(request.getJSONObject("target").getJSONObject("coordinate"),
                            vehicle.getJSONObject("coordinate"));
                    deliveringDistance = AddressDistanceUtil.getAddressDistance((SourceDistanceVO) deliverMap.get("sourceDistance"),
                            (TargetDistanceVO) deliverMap.get("targetDistance"));
                    Double validPercent = 0.0000;
                    if (deliveringDistance.equals(validPercent) || totalDistance.equals(validPercent)) {
                        return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "距离计算错误");
                    } else {
                        //计算保存后返回  保留 4 位小数点
                        Double percent = 1 - (deliveringDistance / totalDistance);
                        distancePercent = Float.parseFloat(percent.toString());
                        BigDecimal distanceDecimal = new BigDecimal(distancePercent);
                        Float percentResp = distanceDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
                        if(percentResp < 0){
                            percentResp = 0f;
                        }
                        respObject.getJSONObject("order").put("deliver_progress",percentResp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return ApiUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 纯走B端的同城订单详情查询，封装使用公共token
     */
    public ApiResponse selectSameExpressDetail(String uuid) {

        if (StringUtils.isBlank(uuid)) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "uuid不能为空");
        }
        JSONObject respObject = SfExpressHelper.getExpressDetail(uuid, COMMON_ACCESSTOKEN);
        if (respObject.containsKey(CustomConstant.ERROR)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "查询失败", respObject.getJSONObject(CustomConstant.ERROR));
        }
        return ApiUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 同步基础数据，返回包裹信息
     */

    private ApiResponse setPackageType(OrderDTO order) {
        List<OrderExpressDTO> orderExpressList = order.getOrderExpressList();
        String accessToken = COMMON_ACCESSTOKEN;
        if (order.getLatitude() == 0 || order.getLongitude() == 0) {
            Map<String, String> map = new HashMap<>(1, 1);
            map.put("reason", "寄件人经纬度参数缺失");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "寄件人经纬度参数缺失", map);
        }
        //sf请求
        String constantsUrl = SF_CONSTANTS_URL + CONSTANTS_STR + "?latitude="
                + order.getLatitude() + "&longitude=" + order.getLongitude();
        HttpGet get = new HttpGet(constantsUrl);
        get.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = ApiGetUtil.get(get);
        JSONObject jsonObject = JSONObject.fromObject(res);
        if (jsonObject.get(CustomConstant.ERRORS) != null || jsonObject.get(CustomConstant.ERROR) != null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "获取订单基础数据失败", jsonObject);
        }
        //没有物品类型
        if (!jsonObject.getJSONObject(SF_CONSTANT).getJSONObject(SF_VALUE).containsKey(PACKAGE_TYPE)) {
            return null;
        }

        JSONArray packageTypeArr = jsonObject.getJSONObject("constant").getJSONObject("value").getJSONArray("PACKAGE_TYPE");
        //获取订单基础数据
        for (OrderExpressDTO orderExpressDTO : orderExpressList) {
            PackageMessageDTO packageMessage = new PackageMessageDTO();
            for (int i = 0; i < packageTypeArr.size(); i++) {
                JSONObject packageTypeOBJ = packageTypeArr.getJSONObject(i);
                String packageCode = packageTypeOBJ.getString("code");
                //获取物品类型与快递相同的数组
                if (packageCode != null && packageCode.equals(orderExpressDTO.getObject_type())) {
                    JSONArray weightArr = packageTypeOBJ.getJSONArray("weight_segment");
                    for (int j = 0; j < weightArr.size(); j++) {
                        JSONObject weightOBJ = weightArr.getJSONObject(j);
                        //根据包裹大小类型添加包裹信息    0/1/2/3 4 5 --对应-- 小/中/大/超大
                        packageMessage.setName(weightOBJ.getString("name"));
                        packageMessage.setWeight(weightOBJ.getString("weight"));
                        if (j == 0 && orderExpressDTO.getPackage_type().equals(PackageType.SMALl_PACKAGE.getKey())) {
                            packageMessage.setType(PackageType.SMALl_PACKAGE.getKey());
                            break;
                        }
                        if (j == 1 && orderExpressDTO.getPackage_type().equals(PackageType.CENTRN_PACKAGE.getKey())) {
                            packageMessage.setType(PackageType.CENTRN_PACKAGE.getKey());
                            break;
                        }
                        if (j == 2 && orderExpressDTO.getPackage_type().equals(PackageType.BIG_PACKAGE.getKey())) {
                            packageMessage.setType(PackageType.BIG_PACKAGE.getKey());
                            break;
                        }
                        if (j == 3 || j == 4 || j == 5) {
                            if (orderExpressDTO.getPackage_type().equals(PackageType.HUGE_PACKAGE.getKey())) {
                                packageMessage.setType(PackageType.HUGE_PACKAGE.getKey());
                            }
                            break;
                        }
                    }
                }
            }
            orderExpressDTO.setPackageMessage(packageMessage);
        }
        return null;
    }
}
