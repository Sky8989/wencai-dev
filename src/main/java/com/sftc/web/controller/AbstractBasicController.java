package com.sftc.web.controller;


import com.sftc.web.service.*;

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
    protected QiniuService qiniuService;

    @Resource
    protected GiftCardService giftCardService;

    @Resource
    protected OrderCancelService orderCancelService;

    @Resource
    protected MessageService messageService;

    @Resource
    protected AddressService addressService;

    @Resource
    protected UserContactService userContactService;

}
