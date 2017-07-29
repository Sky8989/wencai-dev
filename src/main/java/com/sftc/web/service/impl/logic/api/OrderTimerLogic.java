package com.sftc.web.service.impl.logic.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.OrderMapper;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Component
public class OrderTimerLogic {

    private Logger logger = Logger.getLogger(this.getClass());

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderCommitLogic orderCommitLogic;
    @Resource
    private OrderCancelLogic orderCancelLogic;

    private ScheduledExecutorService reserveScheduledExecutorService; // 大网预约定时器
    private ScheduledExecutorService cancelScheduledExecutorService; // 大网取消定时器
    private ScheduledExecutorService cancelSAMEScheduledExecutorService; // 同城取消定时器
    private long timeOutInterval; // 大网取消超时时间间隔
    private long timeOutIntervalForSame; // 大网取消超时时间间隔

    /**
     * 设置大网预约单定时器开关
     */
    public APIResponse setupReserveNationOrderCommitTimer(APIRequest request) {

        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        if (!requestObject.containsKey("on"))
            return APIUtil.paramErrorResponse("缺少必要参数");

        int is_on = ((Double) requestObject.get("on")).intValue();
        long period = requestObject.containsKey("period") ? requestObject.getInt("period") * 1000 : 1800000;
        long delay = requestObject.containsKey("delay") ? requestObject.getInt("delay") * 1000 : 0;

        JSONObject responseObject = new JSONObject();

        if (is_on == 1) { // 开
            if (reserveScheduledExecutorService != null) {
                return APIUtil.submitErrorResponse("定时器已经开启，请勿重复操作", null);
            } else {
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        logger.info("开始提交大网预约单");
                        List<Integer> orderIds = orderMapper.selectNationReserveOrders();
                        for (int order_id : orderIds) {
                            orderCommitLogic.nationOrderReserveCommit(order_id);
                        }
                        logger.info("大网预约单提交完毕");
                    }
                };
                reserveScheduledExecutorService = Executors.newScheduledThreadPool(1);
                reserveScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
                responseObject.put("message", "开启定时器成功");
            }

        } else { // 关
            if (reserveScheduledExecutorService != null) {
                reserveScheduledExecutorService.shutdown();
                reserveScheduledExecutorService = null;
                responseObject.put("message", "关闭定时器成功");
            } else {
                return APIUtil.submitErrorResponse("定时器已经关闭，请勿重复操作", null);
            }
        }

        return APIUtil.getResponse(SUCCESS, responseObject);
    }

    /**
     * 设置大网取消超时订单定时器开关
     */
    public APIResponse setupCancelNationOrderTimer(APIRequest request) {

        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        if (!requestObject.containsKey("on"))
            return APIUtil.paramErrorResponse("缺少必要参数");

        int is_on = ((Double) requestObject.get("on")).intValue();
        long period = requestObject.containsKey("period") ? requestObject.getInt("period") * 1000 : 21600000;
        long delay = requestObject.containsKey("delay") ? requestObject.getInt("delay") * 1000 : 0;
        timeOutInterval = requestObject.containsKey("timeOutInterval") ? requestObject.getInt("timeOutInterval") * 1000 : 43200000; // 默认超时时间12小时

        JSONObject responseObject = new JSONObject();

        if (is_on == 1) { // 开
            if (cancelScheduledExecutorService != null) {
                return APIUtil.submitErrorResponse("定时器已经开启，请勿重复操作", null);
            } else {
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        logger.info("开始取消大网超时单");
                        List<Integer> orderIds = orderMapper.selectNationUnCommitOrders();
                        for (int order_id : orderIds) {
                            orderCancelLogic.cancelNationUnCommitOrder(order_id, timeOutInterval);
                        }
                        logger.info("大网超时订单取消完毕");
                    }
                };
                cancelScheduledExecutorService = Executors.newScheduledThreadPool(1);
                cancelScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
                responseObject.put("message", "开启【取消超时大网订单】定时器成功");
            }

        } else { // 关
            if (cancelScheduledExecutorService != null) {
                cancelScheduledExecutorService.shutdown();
                cancelScheduledExecutorService = null;
                responseObject.put("message", "关闭【取消超时大网订单】定时器成功");
            } else {
                return APIUtil.submitErrorResponse("定时器已经关闭，请勿重复操作", null);
            }
        }

        return APIUtil.getResponse(SUCCESS, responseObject);
    }

    /**
     * 设置同城取消超时订单定时器开关
     */
    public APIResponse setupCancelSameOrderTimer(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        if (!requestObject.containsKey("on"))
            return APIUtil.paramErrorResponse("缺少必要参数");

        int is_on = ((Double) requestObject.get("on")).intValue();
        long period = requestObject.containsKey("period") ? requestObject.getInt("period") * 1000 : 21600000;
        long delay = requestObject.containsKey("delay") ? requestObject.getInt("delay") * 1000 : 0;
        timeOutIntervalForSame = requestObject.containsKey("timeOutInterval") ? requestObject.getInt("timeOutInterval") * 1000 : 43200000; // 默认超时时间12小时

        JSONObject responseObject = new JSONObject();

        if (is_on == 1) { // 开
            if (cancelSAMEScheduledExecutorService != null) {
                return APIUtil.submitErrorResponse("定时器已经开启，请勿重复操作", null);
            } else {
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        logger.info("开始取消同城超时单");
                        List<Integer> orderIds = orderMapper.selectSameUnCommitOrders();
                        for (int order_id : orderIds) {
                            orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutIntervalForSame);
                        }
                        logger.info("同城超时订单取消完毕");
                    }
                };
                cancelSAMEScheduledExecutorService = Executors.newScheduledThreadPool(1);
                cancelSAMEScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
                responseObject.put("message", "开启【取消超时同城订单】定时器成功");
            }
        } else { // 关
            if (cancelSAMEScheduledExecutorService != null) {
                cancelSAMEScheduledExecutorService.shutdown();
                cancelSAMEScheduledExecutorService = null;
                responseObject.put("message", "关闭【取消超时同城订单】定时器成功");
            } else {
                return APIUtil.submitErrorResponse("定时器已经关闭，请勿重复操作", null);
            }
        }
        return APIUtil.getResponse(SUCCESS, responseObject);
    }
}
