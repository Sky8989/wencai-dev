package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.dao.jpa.OrderDao;
import com.sftc.web.dao.jpa.OrderExpressDao;
import com.sftc.web.dao.jpa.OrderExpressTransformDao;
import com.sftc.web.dao.mybatis.OrderExpressMapper;
import com.sftc.web.dao.mybatis.OrderExpressTransformMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.entity.OrderExpressTransform;
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
    @Resource
    private OrderExpressDao orderExpressDao;
    @Resource
    private OrderExpressTransformDao orderExpressTransformDao;

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

                // 插入兜底表
                String same_uuid = oe.getUuid();
                String nation_uuid = oe.getOrder_number();
                OrderExpressTransform oet = new OrderExpressTransform();
                oet.setExpress_id(oe.getId());
                oet.setSame_uuid(same_uuid);
                oet.setIs_read(0);
                oet.setCreate_time(System.currentTimeMillis() + "");
                orderExpressTransformDao.save(oet);

                // 更新express表
                oe.setRoute_state("CANCELED");
                oe.setOrder_number(nation_uuid);
                orderExpressDao.save(oe);

            return APIUtil.submitErrorResponse("快递订单状态异常，好友包裹尚未被领取", null);
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

        OrderExpressTransform orderExpressTransform2 = orderExpressTransformMapper.selectExpressTransformByID(express_transform_id);
        if (orderExpressTransform2 == null)
            return APIUtil.submitErrorResponse("兜底记录不存在", null);

        return APIUtil.getResponse(SUCCESS, orderExpressTransform2);
    }
}
