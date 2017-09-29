package com.sftc.tools.task;

import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.model.Token;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


import static com.sftc.tools.common.DateUtils.iSO8601DateWithTimeStamp;
import static com.sftc.tools.sf.SFTokenHelper.COMMON_ACCESSTOKEN;

/**
 * 公共token的定时刷新任务
 * Created by huxingyue on 2017/8/31.
 */
@Component
@Configuration
@EnableAsync
@EnableScheduling
public class TaskForPublicToken {

    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    @Resource
    TokenMapper tokenMapper;

    private final int fixedRate = 24 * 3600 * 1000;

    @Scheduled(initialDelay = 1000, fixedRate = fixedRate)
    public void updatePublicToken() {
        //更新公共token

        Token tokenById = tokenMapper.getTokenById(10028);
        if (tokenById != null) {
            COMMON_ACCESSTOKEN = tokenById.getAccess_token();
            logger.info("the public Token refreshes successfully,the public token is :" + COMMON_ACCESSTOKEN);
            logger.info("the public Token refreshes successfully,currentTime is :" + iSO8601DateWithTimeStamp(System.currentTimeMillis()));
        } else {
            logger.error("the public Token refreshes failed,currentTime is :" + iSO8601DateWithTimeStamp(System.currentTimeMillis()));
        }

    }
}
