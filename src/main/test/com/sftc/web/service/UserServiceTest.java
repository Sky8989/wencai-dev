package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-dao.xml"})
public class UserServiceTest {

    @Resource
    private UserService userService;

    APIRequest request;

    @Before
    public void setUp() throws Exception {
        request = new APIRequest();
    }

    @Test
    public void wechatLogin() throws Exception {

        request.setAttribute("js_code", "003N9DQB0KIF6e21BUPB0mstQB0N9DQL");
        APIResponse response = userService.login(request);
        Assert.assertTrue(response.getMessage(), response.getState().equals("40029"));
    }

    @Test
    public void login() throws Exception {

        request.setAttribute("user_phone", "skm");
        request.setAttribute("user_password", "123");
        APIResponse response = userService.login(request);
        Assert.assertTrue(response.getMessage(), response.getState().equals("00001"));

        request.setAttribute("user_phone", "skm");
        request.setAttribute("user_password", "12");
        response = userService.login(request);
        Assert.assertTrue(response.getMessage(), response.getState().equals("00002"));

        request.setAttribute("user_phone", "sk");
        request.setAttribute("user_password", "222");
        response = userService.login(request);
        Assert.assertTrue(response.getMessage(), response.getState().equals("00003"));
    }
}