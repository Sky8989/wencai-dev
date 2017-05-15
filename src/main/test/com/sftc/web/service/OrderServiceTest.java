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
    public void friendPlaceOrder() throws Exception {
        request.setAttribute("order_number", UUID.randomUUID().toString());
        Map<Integer, OrderExpress> map = new HashMap<Integer, OrderExpress>();
        OrderExpress orderExpress = new OrderExpress("大包裹", "玩具");
        OrderExpress orderExpress2 = new OrderExpress("中包裹", "文具");
        OrderExpress orderExpress3 = new OrderExpress("小包裹", "衣服");
        map.put(1, orderExpress);
        map.put(2, orderExpress2);
        map.put(3, orderExpress3);
        // APIResponse response = orderService.friendPlaceOrder(request, map);
        // Assert.assertTrue(response.getMessage(), response.getState().equals("00001"));
    }

    @Test
    public void friendFillOrder() throws Exception {

    }

}