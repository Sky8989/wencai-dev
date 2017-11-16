package com.sftc.web.config;

import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.service.impl.logic.app.OrderCancelLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.TimerTask;

/**
 * 取消同城未提交订单和超时订单定时器任务
 * Created by xf on 2017/10/23.
 */
public class CancelSameTimeTask extends TimerTask implements InitializingBean{

    private ServletContext context = null;
    private WebApplicationContext ctx = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public CancelSameTimeTask() {
        super();
    }

    //获取servletContext和spring Bean
    public CancelSameTimeTask(ServletContext context) {
        this.context = context;
        this.ctx = WebApplicationContextUtils.getWebApplicationContext(context);
    }
    @Override
    public void run() {
        logger.info("开始取消同城超时单");
        try {
            //执行定时器任务
            this.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("同城超时订单取消完毕");

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        long timeOutIntervalForSame = 43200000;    //设置超时时间，默认超过 12 小时

        //获取Spring容器中的Bean
        OrderMapper orderMapper = (OrderMapper) ctx.getBean("orderMapper");
        OrderCancelLogic orderCancelLogic = (OrderCancelLogic) ctx.getBean("orderCancelLogic");

        //抓取同城未提交订单和超时订单
        List<String> orderIds = orderMapper.selectSameUnCommitOrders();
        for (String order_id : orderIds) {
            orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutIntervalForSame);
        }
        //好友多包裹的订单 超时更新为DANKAL_OVERTIME 而不是CANCELED
        List<String> orderIds2 = orderMapper.selectMutilExpressOrders();
        for (String order_id : orderIds2) {
            orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutIntervalForSame);
        }
    }
}
