package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.AddressMapper;
import com.sftc.web.model.Address;
import com.sftc.web.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 地址操作接口实现
 * @date 17/4/1
 * @Time 下午9:32
 */
@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    public APIResponse addAddress(Address address) {
        APIStatus status = APIStatus.SUCCESS;
        address.setCreate_time(Long.toString(System.currentTimeMillis()));
        try {
            addressMapper.addAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }
}
