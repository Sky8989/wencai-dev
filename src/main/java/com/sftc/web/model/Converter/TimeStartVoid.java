package com.sftc.web.model.Converter;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.model.OrderNationTime;
import com.sftc.web.service.impl.logic.app.OrderCommitLogic;
import com.sftc.web.service.impl.logic.app.OrderTimerLogic;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.sftc.tools.constant.SFConstant.SF_CREATEORDER_URL;
import static com.sftc.tools.constant.SFConstant.SF_REQUEST_URL;


/**
 * Created by xf on 2017/10/23.
 */
public class TimeStartVoid implements InitializingBean{
    private Gson gson = new Gson();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
        HttpPost post = new HttpPost();
        post = new HttpPost("https://sftc.dankal.cn/sftc/order/reserve/setup");
        post.addHeader("token", "ffa1d5557565e283caba67");
        OrderNationTime orderNationTime = OrderTimeFactory.setOrderNationTimeOn();
        String requestStr = gson.toJson(orderNationTime);
        String resultStr = APIPostUtil.post(requestStr, post);
        logger.info("定时器开启");
    }
}
