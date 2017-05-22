package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.AddressMapper;
import com.sftc.web.model.Address;
import com.sftc.web.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    public APIResponse consigneeAddress(APIRequest request) {
        APIStatus status = APIStatus.SELECT_FAIL;
        String id = request.getParameter("user_id").toString();
        List<Address> addressList = new ArrayList<Address>();
        if (id != null) {
            try {
                addressList = addressMapper.addressDetail(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            status = APIStatus.SUCCESS;
        }
        return APIUtil.getResponse(status, addressList);
    }

    public APIResponse editAddress(Address address) {
        APIStatus status = APIStatus.SUCCESS;
        try {
            addressMapper.editeAddress(address);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    public APIResponse deleteAddress(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String id = request.getParameter("id").toString();
        if (id != null) {
            try {
                addressMapper.deleteAddress(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
                status = APIStatus.SUBMIT_FAIL;
            }
        }
        return APIUtil.getResponse(status, null);
    }
}
