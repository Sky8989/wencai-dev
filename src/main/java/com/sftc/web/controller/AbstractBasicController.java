package com.sftc.web.controller;

import com.sftc.web.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller
 * @Description:
 * @date 2017/4/7
 * @Time 上午8:54
 */
@Controller
public abstract class AbstractBasicController {

    @Resource
    protected UserService userService;
}
