package com.sftc.web.service.impl.logic.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.config.InitTimeListener;
import com.sftc.web.dao.mybatis.OrderMapper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.sftc.tools.api.APIStatus.SUCCESS;


@Component
public class OrderTimerLogic {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderCancelLogic orderCancelLogic;

    private long timeOutIntervalForSame; // 定时器时间间隔

    /**
     * 设置同城取消超时订单定时器参数
     */
    public APIResponse setupCancelSameOrderTimer(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());
        if (!requestObject.containsKey("on"))
            return APIUtil.paramErrorResponse("缺少必要参数");

        int is_on = requestObject.getInt("on");
        long period = requestObject.containsKey("period") ? requestObject.getInt("period") * 1000 : 21600000;
        long delay = requestObject.containsKey("delay") ? requestObject.getInt("delay") * 1000 : 0;
        timeOutIntervalForSame = requestObject.containsKey("timeOutInterval") ? requestObject.getInt("timeOutInterval") * 1000 : 43200000; // 默认超时时间12小时
        JSONObject responseObject = new JSONObject();
        if (is_on == 0) {   //定时器开关  1/0
            if (InitTimeListener.cancelSAMEScheduledExecutorService != null) {
                //关闭servletContext中的定时器
                InitTimeListener.cancelSAMEScheduledExecutorService.shutdown();
                InitTimeListener.cancelSAMEScheduledExecutorService = null;
                responseObject.put("message", "关闭【取消超时同城订单】定时器成功");
            } else {
                return APIUtil.submitErrorResponse("定时器已经关闭，请勿重复操作", null);
            }
        } else {
            if (InitTimeListener.cancelSAMEScheduledExecutorService != null) {
                return APIUtil.submitErrorResponse("定时器已经开启，请勿重复操作", null);
            } else {//设置定时器参数的时候再次执行任务
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        logger.info("开始取消同城超时单");
                        List<String> orderIds = orderMapper.selectSameUnCommitOrders();
                        for (String order_id : orderIds) {
                            orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutIntervalForSame);
                        }
                        logger.info("同城超时订单取消完毕");
                        //好友多包裹的订单 超时更新为DANKAL_OVERTIME 而不是CANCELED
                        List<String> orderIds2 = orderMapper.selectMutilExpressOrders();
                        for (String order_id : orderIds2) {
                            orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutIntervalForSame);
                        }
                        logger.info("好友多包裹订单取消完毕");
                    }
                };
                //重新初始化servlet启动时的同城取消超时订单定时器，但是在servlet重新启动时，参数又会变为默认的
                InitTimeListener.cancelSAMEScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
                responseObject.put("message", "开启【取消超时同城订单】定时器成功");
            }
        }

        return APIUtil.getResponse(SUCCESS, responseObject);
    }

}
