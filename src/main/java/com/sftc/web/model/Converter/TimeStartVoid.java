package com.sftc.web.model.Converter;

import com.sftc.tools.api.APIRequest;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.OrderNationTime;
import com.sftc.web.service.impl.logic.app.OrderCommitLogic;
import com.sftc.web.service.impl.logic.app.OrderTimerLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by xf on 2017/10/23.
 */
public class TimeStartVoid implements InitializingBean,ServletContextAware {
    @Resource
    private OrderTimerLogic orderTimerLogic;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderCommitLogic orderCommitLogic;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Timer timer = null;
    public void afterPropertiesSet() throws Exception {
        logger.info("定时器开启");
        APIRequest request = new APIRequest();
        OrderNationTime orderNationTime = OrderTimeFactory.setOrderNationTimeOn();
        request.setRequestParam(orderNationTime);
        orderTimerLogic.setupReserveNationOrderCommitTimer(request);
        timer = new Timer(true);
        long period = 1800000;
        long delay = 0;
        timer.schedule(new MyTimeTask(orderMapper,orderCommitLogic),delay,period);
        logger.info("定时器开启");
    }

    @Override
    public void setServletContext(ServletContext servletContext) {

    }
}
