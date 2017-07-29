package com.sftc.web.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by huxingyue on 2017/7/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring**.xml"})
public class OrderServiceImplTest {

    OrderServiceImpl orderService = new OrderServiceImpl();
    @Test
    public void test1(){
    }

}