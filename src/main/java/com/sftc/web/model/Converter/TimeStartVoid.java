package com.sftc.web.model.Converter;

import com.sftc.tools.api.APIRequest;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.OrderNationTime;
import com.sftc.web.service.impl.logic.app.OrderCommitLogic;
import com.sftc.web.service.impl.logic.app.OrderTimerLogic;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by xf on 2017/10/23.
 */
public class TimeStartVoid implements InitializingBean,ServletContextListener {
    @Resource
    private OrderTimerLogic orderTimerLogic;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderCommitLogic orderCommitLogic;
    private Timer timer = null;
    public void afterPropertiesSet() throws Exception {
        System.out.println("aaa");
        APIRequest request = new APIRequest();
        OrderNationTime orderNationTime = OrderTimeFactory.setOrderNationTimeOn();
        request.setRequestParam(orderNationTime);
        orderTimerLogic.setupReserveNationOrderCommitTimer(request);
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        timer = new Timer(true);
        sce.getServletContext().log("定时器已启动");
        long period = 1800000;
        long delay = 0;
        timer.schedule(new MyTimeTask(orderMapper,orderCommitLogic),delay,period);
        sce.getServletContext().log("定时器已启动");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
