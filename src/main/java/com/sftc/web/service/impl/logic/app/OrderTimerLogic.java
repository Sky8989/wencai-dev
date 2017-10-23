package com.sftc.web.service.impl.logic.app;

import com.sftc.web.config.InitTimeListener;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.service.OrderTimeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class OrderTimerLogic implements OrderTimeService {

    @Resource
    private  OrderMapper orderMapper;
    @Resource
    private  OrderCommitLogic orderCommitLogic;
    @Resource
    private  OrderCancelLogic orderCancelLogic;

    private  ScheduledExecutorService reserveScheduledExecutorService; // 大网预约定时器
    private  ScheduledExecutorService cancelScheduledExecutorService; // 大网取消定时器
    private  ScheduledExecutorService cancelSAMEScheduledExecutorService; // 同城取消定时器
    private  long timeOutInterval; // 大网取消超时时间间隔
    private  long timeOutIntervalForSame; // 大网取消超时时间间隔

    /**
     * 开启大网预约单定时器
     */
    public  void setupReserveNationOrderCommitTimer() {
        int is_on = InitTimeListener.is_on;
        long period = 1800000;
        long delay = 0;

        if(is_on==1){
            if (reserveScheduledExecutorService != null) {
                System.out.println("已开启");
                return;
            }
            try{
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        List<String> orderIds = orderMapper.selectNationReserveOrders();
                        long currentTimeMillis = System.currentTimeMillis();
                        for (String order_id : orderIds) {
                            orderCommitLogic.nationOrderReserveCommit(order_id, currentTimeMillis);
                        }

                    }
                };
                reserveScheduledExecutorService = Executors.newScheduledThreadPool(1);
                reserveScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else { // 关
            if (reserveScheduledExecutorService != null) {
                reserveScheduledExecutorService.shutdown();
                reserveScheduledExecutorService = null;
            }
        }

    }

    /**
     * 设置大网取消超时订单定时器开关
     */
    public void setupCancelNationOrderTimer() {
        int is_on = InitTimeListener.is_on;
        long period = 21600000;
        long delay = 0;
        timeOutInterval = 43200000; // 默认超时时间12小时

        if (is_on == 1) { // 开
            if (cancelScheduledExecutorService != null) {
                return;
            }
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        List<String> orderIds = orderMapper.selectNationUnCommitOrders();
                        for (String order_id : orderIds) {
                            orderCancelLogic.cancelNationUnCommitOrder(order_id, timeOutInterval);
                        }
                        //好友多包裹的订单 超时更新为DANKAL_OVERTIME 而不是CANCELED
                        List<String> orderIds2 = orderMapper.selectMutilExpressOrders();
                        for (String order_id : orderIds2) {
                            orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutInterval);
                        }

                    }
                };
                cancelScheduledExecutorService = Executors.newScheduledThreadPool(1);
                cancelScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);


        } else { // 关
            if (cancelScheduledExecutorService != null) {
                cancelScheduledExecutorService.shutdown();
                cancelScheduledExecutorService = null;
            }
        }

    }

    /**
     * 设置同城取消超时订单定时器开关
     */
    public void setupCancelSameOrderTimer() {
        int is_on = InitTimeListener.is_on;
        long period = 21600000;
        long delay = 0;
        timeOutIntervalForSame = 43200000; // 默认超时时间12小时

        if (is_on == 1) { // 开
            if (cancelSAMEScheduledExecutorService != null) {
                return;
            }
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        List<String> orderIds = orderMapper.selectSameUnCommitOrders();
                        for (String order_id : orderIds) {
                            orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutIntervalForSame);
                        }

                    }
                };
                cancelSAMEScheduledExecutorService = Executors.newScheduledThreadPool(1);
                cancelSAMEScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);


        } else { // 关
            if (cancelSAMEScheduledExecutorService != null) {
                cancelSAMEScheduledExecutorService.shutdown();
                cancelSAMEScheduledExecutorService = null;

            }
        }
    }
    /**
     * 设置临时Token的定时器开关
     */
    public void setupTemporaryToken() {
        int is_on = InitTimeListener.is_on;
        long period = 300000;
        long delay = 0;
        timeOutIntervalForSame = 300000; // 临时Token失效时间5分钟

        if (is_on == 1) { // 开
            if (cancelSAMEScheduledExecutorService != null) {
                return;
            }
            final TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    List<String> orderIds = orderMapper.selectSameUnCommitOrders();
                    for (String order_id : orderIds) {
                        orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutIntervalForSame);
                    }

                }
            };
            cancelSAMEScheduledExecutorService = Executors.newScheduledThreadPool(1);
            cancelSAMEScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);


        } else { // 关
            if (cancelSAMEScheduledExecutorService != null) {
                cancelSAMEScheduledExecutorService.shutdown();
                cancelSAMEScheduledExecutorService = null;

            }
        }
    }
}
