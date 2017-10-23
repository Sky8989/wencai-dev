package com.sftc.web.config;

import com.sftc.web.service.OrderTimeService;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

/**
 * Created by xf on 2017/10/23.
 */

public class InitTimeListener extends HttpServlet implements ServletContextListener{
    public static int is_on;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
       is_on = 1;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        is_on = 2;
    }
}
