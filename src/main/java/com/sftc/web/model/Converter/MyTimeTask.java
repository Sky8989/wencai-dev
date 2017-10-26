package com.sftc.web.model.Converter;

import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.service.impl.logic.app.OrderCommitLogic;


import java.util.List;
import java.util.TimerTask;

/**
 * Created by xf on 2017/10/24.
 */
public class MyTimeTask extends TimerTask{
    private OrderMapper orderMapper;
    private OrderCommitLogic orderCommitLogic;

    public OrderMapper getOrderMapper() {return orderMapper;}

    public void setOrderMapper(OrderMapper orderMapper) {this.orderMapper = orderMapper;}

    public OrderCommitLogic getOrderCommitLogic() {return orderCommitLogic;}

    public void setOrderCommitLogic(OrderCommitLogic orderCommitLogic) {this.orderCommitLogic = orderCommitLogic;}

    public MyTimeTask(OrderMapper orderMapper, OrderCommitLogic orderCommitLogic){
        this.orderMapper = orderMapper;
        this.orderCommitLogic = orderCommitLogic;
    }
    @Override
    public void run() {
        List<String> orderIds = orderMapper.selectNationReserveOrders();
        long currentTimeMillis = System.currentTimeMillis();
        for (String order_id : orderIds) {
            orderCommitLogic.nationOrderReserveCommit(order_id, currentTimeMillis);
        }
    }
}
