package com.sftc.ssm.service;

import com.sftc.ssm.model.Address;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceTest extends TestCase {

    private ApplicationContext applicationContext;

    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-*.xml");
    }

    public void testAddAddress() throws Exception {
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.addAddress(new Address());

    }
}