package com.sftc.web.config;

import com.sftc.web.dao.mybatis.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.TimerTask;

/**
 * 提交大网预约订单定时器任务
 * Created by xf on 2017/10/23.
 */
public class ReserveNationTimeTask extends TimerTask implements InitializingBean {

    private ServletContext context = null;
    private WebApplicationContext ctx = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ReserveNationTimeTask() {
        super();
    }

    //获取servletContext和spring Bean
    public ReserveNationTimeTask(ServletContext context) {
        this.context = context;
        this.ctx = WebApplicationContextUtils.getWebApplicationContext(context);
    }

    @Override
    public void run() {
        logger.info("开始提交大网预约单");
        try {
            //执行定时器任务
            this.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("大网预约单提交完毕");

    }

    //提交大网预约单定时器任务
    @Override
    public void afterPropertiesSet() throws Exception {
        //获取Spring容器中的Bean
        OrderMapper orderMapper = (OrderMapper) ctx.getBean("orderMapper");
        OrderCommitLogic orderCommitLogic = (OrderCommitLogic) ctx.getBean("orderCommitLogic");
        //抓取大网预约单
        List<String> orderIds = orderMapper.selectNationReserveOrders();
        long currentTimeMillis = System.currentTimeMillis();
        for (String order_id : orderIds) {
            orderCommitLogic.nationOrderReserveCommit(order_id, currentTimeMillis);
        }
    }
}
