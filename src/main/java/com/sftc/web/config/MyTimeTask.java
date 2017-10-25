package com.sftc.web.config;

import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.service.impl.logic.app.OrderCancelLogic;
import com.sftc.web.service.impl.logic.app.OrderCommitLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by xf on 2017/10/23.
 */
public class MyTimeTask extends TimerTask implements InitializingBean{

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderCommitLogic orderCommitLogic;
    private ServletContext context = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MyTimeTask() {
        super();
    }

    public MyTimeTask(ServletContext context) {
        this.context = context;
    }
    @Override
    public void run() {
        logger.info("开始提交大网预约单");
        try {
            this.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("大网预约单提交完毕");

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<String> orderIds = orderMapper.selectNationReserveOrders();
        long currentTimeMillis = System.currentTimeMillis();
        for (String order_id : orderIds) {
            orderCommitLogic.nationOrderReserveCommit(order_id, currentTimeMillis);
        }
    }
}
