package com.sftc.web.config;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 启动项目开启定时器的监听器
 * Created by xf on 2017/10/23.
 */

public class InitTimeListener extends ContextLoader implements ServletContextListener{
   public InitTimeListener(){}
//    private java.util.Timer timer = null;
    public static  ScheduledExecutorService reserveScheduledExecutorService;   //提交大网预约订单定时器
    public static  ScheduledExecutorService cancelScheduledExecutorService;    //取消大网未提交订单和超时订单定时器
    public static  ScheduledExecutorService cancelSAMEScheduledExecutorService;   //取消同城未提交订单和超时订单定时器
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        long delay = 0;
        long period = 1800000;   //定时器执行周期 半小时
        long period2 = 21600000;   //定时器执行周期 6 小时
//        timer = new java.util.Timer(true);
        sce.getServletContext().log("定时器已启动");
//        timer.schedule(new ReserveNationTimeTask(sce.getServletContext()),delay,period);
        //初始化大网预约订单定时器
        reserveScheduledExecutorService = Executors.newScheduledThreadPool(1);
        reserveScheduledExecutorService.scheduleAtFixedRate(new ReserveNationTimeTask(sce.getServletContext()), delay, period, TimeUnit.MILLISECONDS);
       //初始化取消大网未提交订单和超时订单定时器
        cancelScheduledExecutorService = Executors.newScheduledThreadPool(1);
        cancelScheduledExecutorService.scheduleAtFixedRate(new CancelNationTimeTask(sce.getServletContext()), delay, period2, TimeUnit.MILLISECONDS);
        //初始化取消同城未提交订单和超时订单定时器
        cancelSAMEScheduledExecutorService = Executors.newScheduledThreadPool(1);
        cancelSAMEScheduledExecutorService.scheduleAtFixedRate(new CancelSameTimeTask(sce.getServletContext()), delay, period2, TimeUnit.MILLISECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
//        reserveScheduledExecutorService.shutdown();
        sce.getServletContext().log("定时器销毁");
    }
}
