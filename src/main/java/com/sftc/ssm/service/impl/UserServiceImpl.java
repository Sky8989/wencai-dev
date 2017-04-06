package com.sftc.ssm.service.impl;

import com.sftc.ssm.mapper.UserMapper;
import com.sftc.ssm.model.Address;
import com.sftc.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 用户操作接口实现
 * @date 17/4/1
 * @Time 下午9:34
 */
public class UserServiceImpl implements UserService {

    @Autowired

    private UserMapper userMapper;



    public void addAddress(Address address) {
        userMapper.addAddress(address);
        System.out.print(address.getId());
    }
}
