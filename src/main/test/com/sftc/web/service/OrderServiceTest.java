package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.web.model.OrderExpress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description:
 * @date 2017/5/15
 * @Time 上午2:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-dao.xml"})
public class OrderServiceTest {

    @Resource
    private OrderService orderService;

    APIRequest request;

    @Before
    public void setUp() throws Exception {
        request = new APIRequest();
    }

    @Test
    public void placeOrder() throws Exception {

    }

    @Test
    public void payOrder() throws Exception {

    }



    @Test
    public void friendFillOrder() throws Exception {

    }

}