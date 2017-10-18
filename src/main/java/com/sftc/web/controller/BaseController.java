package com.sftc.web.controller;


import com.sftc.web.service.*;

import javax.annotation.Resource;

public abstract class BaseController {

    @Resource
    protected QiniuService qiniuService;

    @Resource
    protected GiftCardService giftCardService;

    @Resource
    protected MessageService messageService;

    @Resource
    protected UserContactService userContactService;

}
