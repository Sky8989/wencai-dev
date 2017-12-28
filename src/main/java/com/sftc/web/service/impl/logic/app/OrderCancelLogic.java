package com.sftc.web.service.impl.logic.app;


import com.sftc.tools.api.ApiPostUtil;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiStatus;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.OrderCancelDao;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.mybatis.OrderCancelMapper;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.OrderCancel;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.vo.swaggerOrderRequest.ExpressCancelVO;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderCancelVO;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.constant.SfConstant.SF_REQUEST_URL;
import static com.sftc.tools.sf.SfTokenHelper.COMMON_ACCESSTOKEN;

/**
 * @author bingo
 * @date 2017/12/07
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class OrderCancelLogic {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private OrderCancelMapper orderCancelMapper;
    @Resource
    private OrderExpressDao orderExpressDao;
    @Resource
    private OrderCancelDao orderCancelDao;

    //////////////////// Public Method ////////////////////

    /**
     * 取消订单
     */
    public ApiResponse cancelOrder(OrderCancelVO orderCancelVO) {

        String orderId = orderCancelVO.getOrder_id();

        List<OrderExpress> orderExpressList = orderExpressMapper.findAllOrderExpressByOrderId(orderId);
        if (orderExpressList == null || orderExpressList.size() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(),"订单不存在");
        }

        String accessToken = TokenUtils.getInstance().getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            accessToken = COMMON_ACCESSTOKEN;
        }

        try {
            // 取消订单中的所有包裹
            for (OrderExpress orderExpress : orderExpressList) {
                cancelOrderByUuidAndAccessToken(orderExpress, accessToken);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }

        return ApiUtil.getResponse(ApiStatus.SUCCESS, null);
    }

    /**
     * 根据uuid取消订单
     */
    public ApiResponse cancelOrderByUuid(ExpressCancelVO expressCancelVO) {

        String uuid = expressCancelVO.getUuid();

        OrderExpress orderExpress = orderExpressMapper.selectExpressByUuid(uuid);
        if (orderExpress == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }

        String accessToken = TokenUtils.getInstance().getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            accessToken = COMMON_ACCESSTOKEN;
        }

        try {
            // 取消单个包裹
            cancelOrderByUuidAndAccessToken(orderExpress, accessToken);
        } catch (Exception e) {
            e.fillInStackTrace();
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(),e.getMessage());
        }

        return ApiUtil.getResponse(ApiStatus.SUCCESS, null);
    }

    /**
     * 取消同城超时订单
     */
    public void cancelUnCommitOrder(String orderId, long timeOutInterval) {
        OrderDTO order = orderMapper.selectOrderDetailByOrderId(orderId);
        // 判断是否超时
        if (Long.parseLong(order.getCreate_time()) + timeOutInterval < System.currentTimeMillis()) {
            // order表标记为已取消
            orderMapper.updateCancelOrderById(orderId);
            List<OrderExpress> orderExpressList = orderExpressMapper.findAllOrderExpressByOrderId(orderId);
            for (OrderExpress orderExpress : orderExpressList) {
                // 更新数据库
                orderExpress.setRoute_state("OVERTIME");
                orderExpress.setPay_state("WAIT_PAY");
                orderExpressDao.save(orderExpress);
                // 添加订单取消记录
                OrderCancel orderCancel = new OrderCancel();
                orderCancel.setExpress_id(orderExpress.getId());
                orderCancel.setReason("超时");
                orderCancel.setQuestion_describe("软取消");
                orderCancel.setCreate_time(Long.toString(System.currentTimeMillis()));
                orderCancelDao.save(orderCancel);
            }
        }
    }

    //////////////////// Private Method ////////////////////

    /**
     * 根据uuid和access_token取消订单
     *
     * @param orderExpress 快递
     * @param accessToken  sf token
     */
    private void cancelOrderByUuidAndAccessToken(OrderExpress orderExpress, String accessToken) throws Exception {
        if (orderExpress == null) {
            throw new Exception("订单不存在");
        }
        String uuid = orderExpress.getUuid();
        if (StringUtils.isBlank(uuid)) {
            throw new Exception("快递uuid为空");
        }
        // 取消订单 判断订单如果已经提交到顺丰，需要先请求顺丰接口取消
        boolean isDidCommitToSF = StringUtils.isNotBlank(orderExpress.getOrder_number());
        if (isDidCommitToSF) {
            String str = "{\"event\":{\"type\":\"CANCEL\",\"source\":\"MERCHANT\"}}";
            String myUrl = SF_REQUEST_URL + "/" + uuid + "/events";
            HttpPost post = new HttpPost(myUrl);
            post.addHeader("PushEnvelope-Device-Token", accessToken);
            String res = ApiPostUtil.post(str, post);
            JSONObject resJSONObject = JSONObject.fromObject(res);
            // 失败
            if (resJSONObject.containsKey(CustomConstant.ERROR)) {
                String errorMessage;
                try {
                    errorMessage = resJSONObject.getJSONObject(CustomConstant.ERROR).getString("message");
                } catch (Exception e) {
                    errorMessage = "取消订单失败";
                }
                throw new Exception(errorMessage);
            }
        }
        String cancelStr = "CANCELED";
        String payStr = "WAIT_PAY";

        //判断是否已经取消过了
        if (!cancelStr.equals(orderExpress.getRoute_state())) {
            // 更新数据库
            orderExpress.setRoute_state(cancelStr);
            orderExpress.setPay_state(payStr);
            orderExpressDao.save(orderExpress);
            orderMapper.updateCancelOrderById(orderExpress.getOrder_id());
        }
        //判断是否取消
        List<Integer> cacheId = orderCancelMapper.selectCanceleOrderByExpressId(orderExpress.getId());
        if (cacheId.size() < 1) {
            // 添加订单取消记录
            OrderCancel orderCancel = new OrderCancel();
            orderCancel.setExpress_id(orderExpress.getId());
            orderCancel.setReason("用户取消");
            orderCancel.setQuestion_describe(isDidCommitToSF ? "取消" : "软取消");
            orderCancel.setCreate_time(Long.toString(System.currentTimeMillis()));
            orderCancelDao.save(orderCancel);
        }
    }

}
