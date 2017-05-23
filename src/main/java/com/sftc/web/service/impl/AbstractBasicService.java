package com.sftc.web.service.impl;

import com.sftc.web.mapper.TokenMapper;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.Token;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service.impl
 * @Description: 公有service类
 * @date 2017/4/7
 * @Time 上午9:13
 */
public abstract class AbstractBasicService {

    @Resource
    protected UserMapper userMapper;
    @Resource
    protected TokenMapper tokenMapper;

}
