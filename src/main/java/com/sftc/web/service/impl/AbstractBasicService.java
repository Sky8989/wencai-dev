package com.sftc.web.service.impl;

import com.sftc.tools.api.APIStatus;
import com.sftc.web.mapper.UserMapper;

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

    APIStatus status = APIStatus.SUCCESS;
}
