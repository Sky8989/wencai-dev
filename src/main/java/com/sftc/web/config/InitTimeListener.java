package com.sftc.web.config;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

/**
 * Created by xf on 2017/10/23.
 */

public class InitTimeListener extends ContextLoader implements ServletContextListener{
   public InitTimeListener(){}
    private java.util.Timer timer = null;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        long delay = 0;
        long period = 1800000;
        timer = new java.util.Timer(true);
        sce.getServletContext().log("定时器已启动");
        timer.schedule(new MyTimeTask(sce.getServletContext()),delay,period);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        timer.cancel();
        sce.getServletContext().log("定时器销毁");
    }
}
