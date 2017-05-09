package com.sftc.web.controller;

import com.sftc.web.service.OrderService;
import com.sftc.web.service.QiniuService;
import com.sftc.web.service.UserService;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller
 * @Description: 常用公有类
 * @date 2017/4/7
 * @Time 上午8:54
 */
public abstract class AbstractBasicController {

    @Resource
    protected UserService userService;

    @Resource
    protected QiniuService qiniuService;
    @Resource
    protected OrderService orderService;
}
