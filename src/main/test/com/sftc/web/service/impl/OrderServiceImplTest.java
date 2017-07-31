package com.sftc.web.service.impl;

import com.sftc.tools.common.DateUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

import static org.apache.commons.lang.time.DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT;
import static org.junit.Assert.*;

/**
 * Created by huxingyue on 2017/7/15.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:spring/spring**.xml"})
public class OrderServiceImplTest {

    OrderServiceImpl orderService = new OrderServiceImpl();
    @Test
    public void test1(){
        String reserve_time = "1501506000517";
        Locale locale =  Locale.CHINA;
        String format = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd'T'HH:mm:ssZZ", locale);
        System.out.println("1-  -"+format);
        String format2 = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale);
        System.out.println("2-  -"+format2);
    }
}