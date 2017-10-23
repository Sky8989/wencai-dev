package com.sftc.web.service.impl.logic.app;

import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import com.sftc.web.model.wechat.WechatUser;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_REGISTER_URL;
import static com.sftc.tools.constant.ThirdPartyConstant.WX_AUTHORIZATION;

@Component
public class OrderTimerLogic {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderCommitLogic orderCommitLogic;
    @Resource
    private OrderCancelLogic orderCancelLogic;
    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private UserMapper userMapper;

    private ScheduledExecutorService reserveScheduledExecutorService; // 大网预约定时器
    private ScheduledExecutorService cancelScheduledExecutorService; // 大网取消定时器
    private ScheduledExecutorService cancelSAMEScheduledExecutorService; // 同城取消定时器
    private ScheduledExecutorService temporaryTokenExecutorService; // 临时Token定时器
    private long timeOutInterval; // 大网取消超时时间间隔
    private long timeOutIntervalForSame; // 大网取消超时时间间隔
    private Gson gson = new Gson();

    /**
     * 设置大网预约单定时器开关
     */
    public APIResponse setupReserveNationOrderCommitTimer(APIRequest request) {

        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());

        int is_on = requestObject.getInt("on");
        long period = requestObject.containsKey("period") ? requestObject.getInt("period") * 1000 : 1800000;
        long delay = requestObject.containsKey("delay") ? requestObject.getInt("delay") * 1000 : 0;

        JSONObject responseObject = new JSONObject();

        if (is_on == 1) {
            if (reserveScheduledExecutorService != null) {
                reserveScheduledExecutorService.shutdown();
                reserveScheduledExecutorService = null;
                responseObject.put("message", "关闭定时器成功");
            } else {
                return APIUtil.submitErrorResponse("定时器已经关闭，请勿重复操作", null);
            }
        } else {
            if (reserveScheduledExecutorService != null) {
                return APIUtil.submitErrorResponse("定时器已经开启，请勿重复操作", null);
            } else {
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        logger.info("开始提交大网预约单");
                        List<String> orderIds = orderMapper.selectNationReserveOrders();
                        long currentTimeMillis = System.currentTimeMillis();
                        for (String order_id : orderIds) {
                            orderCommitLogic.nationOrderReserveCommit(order_id, currentTimeMillis);
                        }
                        logger.info("大网预约单提交完毕");
                    }
                };
                reserveScheduledExecutorService = Executors.newScheduledThreadPool(1);
                reserveScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
                responseObject.put("message", "开启定时器成功");
            }
        }

        return APIUtil.getResponse(SUCCESS, responseObject);
    }

    /**
     * 设置大网取消超时订单定时器开关
     */
    public APIResponse setupCancelNationOrderTimer(APIRequest request) {

        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());

        int is_on = requestObject.getInt("on");
        long period = requestObject.containsKey("period") ? requestObject.getInt("period") * 1000 : 21600000;
        long delay = requestObject.containsKey("delay") ? requestObject.getInt("delay") * 1000 : 0;
        timeOutInterval = requestObject.containsKey("timeOutInterval") ? requestObject.getInt("timeOutInterval") * 1000 : 43200000; // 默认超时时间12小时

        JSONObject responseObject = new JSONObject();
        if (is_on == 1) {
            if (cancelScheduledExecutorService != null) {
                cancelScheduledExecutorService.shutdown();
                cancelScheduledExecutorService = null;
                responseObject.put("message", "关闭【取消超时大网订单】定时器成功");
            } else {
                return APIUtil.submitErrorResponse("定时器已经关闭，请勿重复操作", null);
            }
        } else {
            if (cancelScheduledExecutorService != null) {
                return APIUtil.submitErrorResponse("定时器已经开启，请勿重复操作", null);
            } else {
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        logger.info("开始取消大网超时单");
                        List<String> orderIds = orderMapper.selectNationUnCommitOrders();
                        for (String order_id : orderIds) {
                            orderCancelLogic.cancelNationUnCommitOrder(order_id, timeOutInterval);
                        }
                        //好友多包裹的订单 超时更新为DANKAL_OVERTIME 而不是CANCELED
                        List<String> orderIds2 = orderMapper.selectMutilExpressOrders();
                        for (String order_id : orderIds2) {
                            orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutInterval);
                        }
                        logger.info("大网超时订单取消完毕");
                    }
                };
                cancelScheduledExecutorService = Executors.newScheduledThreadPool(1);
                cancelScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
                responseObject.put("message", "开启【取消超时大网订单】定时器成功");
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

        int is_on = requestObject.getInt("on");
        long period = requestObject.containsKey("period") ? requestObject.getInt("period") * 1000 : 21600000;
        long delay = requestObject.containsKey("delay") ? requestObject.getInt("delay") * 1000 : 0;
        timeOutIntervalForSame = requestObject.containsKey("timeOutInterval") ? requestObject.getInt("timeOutInterval") * 1000 : 43200000; // 默认超时时间12小时

        JSONObject responseObject = new JSONObject();
        if (is_on == 1) {
            if (cancelSAMEScheduledExecutorService != null) {
                cancelSAMEScheduledExecutorService.shutdown();
                cancelSAMEScheduledExecutorService = null;
                responseObject.put("message", "关闭【取消超时同城订单】定时器成功");
            } else {
                return APIUtil.submitErrorResponse("定时器已经关闭，请勿重复操作", null);
            }
        } else {
            if (cancelSAMEScheduledExecutorService != null) {
                return APIUtil.submitErrorResponse("定时器已经开启，请勿重复操作", null);
            } else {
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        logger.info("开始取消同城超时单");
                        List<String> orderIds = orderMapper.selectSameUnCommitOrders();
                        for (String order_id : orderIds) {
                            orderCancelLogic.cancelSameUnCommitOrder(order_id, timeOutIntervalForSame);
                        }
                        logger.info("同城超时订单取消完毕");
                    }
                };
                cancelSAMEScheduledExecutorService = Executors.newScheduledThreadPool(1);
                cancelSAMEScheduledExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
                responseObject.put("message", "开启【取消超时同城订单】定时器成功");
            }
        }

        return APIUtil.getResponse(SUCCESS, responseObject);
    }

    /**
     * 临时Token定时器
     */
    public APIResponse temporaryToken(APIRequest request) {
        JSONObject requestObject = JSONObject.fromObject(request.getRequestParam());

        int is_on = requestObject.getInt("on");
        long period = requestObject.containsKey("period") ? requestObject.getInt("period") * 1000 : 300000;
        long delay = requestObject.containsKey("delay") ? requestObject.getInt("delay") * 1000 : 0;
        timeOutIntervalForSame = requestObject.containsKey("timeOutInterval") ? requestObject.getInt("timeOutInterval") * 1000 : 300000; //5分钟过期

        JSONObject responseObject = new JSONObject();
        if (is_on == 1) {
            if (temporaryTokenExecutorService != null) {
                temporaryTokenExecutorService.shutdown();
                temporaryTokenExecutorService = null;
                responseObject.put("message", "关闭临时Token定时器成功");
            } else {
                return APIUtil.submitErrorResponse("定时器已经关闭，请勿重复操作", null);
            }
        } else {
            if (temporaryTokenExecutorService != null) {
                return APIUtil.submitErrorResponse("定时器已经开启，请勿重复操作", null);
            } else {
                final TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        logger.info("开始获取临时Token");
                        String auth_url = WX_AUTHORIZATION + "041u1Eid1d3bbv0nFyld1FSjid1u1EiW";
                        WechatUser wechatUser = null;
                        try {
                            wechatUser = APIResolve.getWechatJson(auth_url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (wechatUser.getOpenid() != null) {
                            User user = userMapper.selectUserByUserId(2188);
                            user.setOpen_id(wechatUser.getOpenid());
                            user.setSession_key(wechatUser.getSession_key());
                            user.setCreate_time(Long.toString(System.currentTimeMillis()));
                            userMapper.insertOpenid(user);
                            //构建新的token
                            String myToken = makeToken(user.getCreate_time(), user.getOpen_id());
                            Token token = new Token(user.getId(), myToken);
                            tokenMapper.addToken(token);
                        } else {
                            User user = userMapper.selectUserByUserId(2188);
                            user.setOpen_id(wechatUser.getOpenid());
                            user.setSession_key(wechatUser.getSession_key());
                            user.setCreate_time(Long.toString(System.currentTimeMillis()));
                            Token token = tokenMapper.getTokenById(user.getId());
                            if (token == null) {
                                token = new Token(user.getId(), makeToken(user.getCreate_time(), user.getOpen_id()));
                                tokenMapper.addToken(token);
                            } else {
                                String gmt_modified = Long.toString(System.currentTimeMillis());
                                if (Long.parseLong(gmt_modified) > Long.parseLong(token.getGmt_expiry())) {
                                    token.setGmt_expiry(Long.toString(System.currentTimeMillis() + 2592000000L));
                                    String myToken = makeToken(gmt_modified, user.getOpen_id());
                                    token.setLocal_token(myToken);
                                }
                                token.setGmt_modified(gmt_modified);
                                tokenMapper.updateToken(token);
                            }

                        }
                    }

                };
            temporaryTokenExecutorService = Executors.newScheduledThreadPool(1);
            temporaryTokenExecutorService.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
            responseObject.put("message", "开启临时Token定时器成功");
        }
    }

         return APIUtil.getResponse(SUCCESS, responseObject);
    }
    private String makeToken(String str1, String str2) {
        String s = MD5Util.MD5(str1 + str2);
        return s.substring(0, s.length() - 10);
    }
}
