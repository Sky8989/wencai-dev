package com.sftc.web.config;

import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.service.impl.logic.app.OrderCancelLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 取消同城未提交订单和超时订单定时器任务
 * Created by xf on 2017/10/23.
 */
@Component
public class CancelSameTimeTask {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderCancelLogic orderCancelLogic;

    /**
     * 获取servletContext和spring Bean
     */
    public CancelSameTimeTask() {
    }

    @Scheduled(fixedRate = 21600000)
    public void afterPropertiesSet() throws Exception {
        logger.info("开始取消同城超时单");
        //设置超时时间，默认超过 12 小时
        long timeOutIntervalForSame = 43200000;


        //抓取同城未提交订单和超时订单
        List<String> orderIds = orderMapper.selectSameUnCommitOrders();
        for (String orderId : orderIds) {
            orderCancelLogic.cancelUnCommitOrder(orderId, timeOutIntervalForSame);
        }
        //好友多包裹的订单 超时更新为OVERTIME 而不是CANCELED
        List<String> orderIds2 = orderMapper.selectMutilExpressOrders();
        for (String orderId : orderIds2) {
            orderCancelLogic.cancelUnCommitOrder(orderId, timeOutIntervalForSame);
        }
        logger.info("同城超时订单取消完毕");
    }
}
