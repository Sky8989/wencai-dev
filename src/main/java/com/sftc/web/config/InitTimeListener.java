//package com.sftc.web.config;
//
//import com.sftc.web.service.OrderTimeService;
//
//import javax.annotation.Resource;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.http.HttpServlet;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by xf on 2017/10/23.
// */
//
//public class InitTimeListener extends HttpServlet implements ServletContextListener{
//   public InitTimeListener(){}
//    private java.util.Timer timer = null;
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        long date = 1800000;
//        long period = 0;
//        timer = new java.util.Timer(true);
//        timer.schedule(new MyTimeTask(sce.getServletContext()), date, period);
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce)
//    {
//
//    }
//}
