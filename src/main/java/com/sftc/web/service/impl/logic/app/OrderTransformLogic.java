package com.sftc.web.service.impl.logic.app;


import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.jpa.OrderExpressTransformDao;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderExpressTransformMapper;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.entity.OrderExpressTransform;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderTransform;
import com.sftc.web.model.vo.swaggerOrderRequest.OrderTransformIsReadVO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.sftc.tools.api.ApiStatus.SUCCESS;

@Component
public class OrderTransformLogic {

    @Resource
    private OrderExpressMapper orderExpressMapper;
    @Resource
    private OrderExpressTransformMapper orderExpressTransformMapper;
    @Resource
    private OrderExpressDao orderExpressDao;
    @Resource
    private OrderExpressTransformDao orderExpressTransformDao;

    /**
     * 根据同城订单的uuid，把原本同城的单下到大网
     */
    public ApiResponse transformOrderFromSameToNation(OrderTransform orderTransform) {

        String uuid = orderTransform.getUuid();

        OrderExpress oe = orderExpressMapper.selectExpressByUuid(uuid);
        if (oe == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "订单不存在");
        }

        // 插入兜底表
        String sameUuid = oe.getUuid();
        String orderNum = oe.getOrder_number();
        OrderExpressTransform oet = new OrderExpressTransform();
        oet.setExpress_id(oe.getId());
        oet.setSame_uuid(sameUuid);
        oet.setIs_read(0);
        oet.setCreate_time(System.currentTimeMillis() + "");
        orderExpressTransformDao.save(oet);

        // 更新express表
        oe.setRoute_state("CANCELED");
        oe.setOrder_number(orderNum);
        orderExpressDao.save(oe);
        return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "快递订单状态异常，好友包裹尚未被领取");
    }

    /**
     * 设置兜底记录已读
     */
    public ApiResponse readExpressTransform(OrderTransformIsReadVO orderTransformIsReadVO) {

        Integer expressTransformId = orderTransformIsReadVO.getExpress_transform_id();
        orderExpressTransformMapper.updateExpressTransformReadStatusById(expressTransformId);

        OrderExpressTransform orderExpressTransform2 = orderExpressTransformMapper.selectExpressTransformByID(expressTransformId);
        if (orderExpressTransform2 == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "兜底记录不存在");
        }

        return ApiUtil.getResponse(SUCCESS, orderExpressTransform2);
    }
}
