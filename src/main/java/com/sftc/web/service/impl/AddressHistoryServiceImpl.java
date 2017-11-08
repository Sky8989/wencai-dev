package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.AddressBookDao;
import com.sftc.web.dao.jpa.AddressHistoryDao;
import com.sftc.web.dao.mybatis.AddressBookMapper;
import com.sftc.web.dao.mybatis.AddressHistoryMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.Converter.AddressFactory;
import com.sftc.web.model.dto.AddressBookDTO;
import com.sftc.web.model.User;
import com.sftc.web.model.dto.AddressDTO;
import com.sftc.web.model.entity.Address;
import com.sftc.web.model.entity.AddressBook;
import com.sftc.web.model.entity.AddressHistory;
import com.sftc.web.service.AddressHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.DKConstant.DK_USER_AVATAR_DEFAULT;

@Service
public class AddressHistoryServiceImpl implements AddressHistoryService {

    @Resource
    private AddressHistoryMapper addressHistoryMapper;
    @Resource
    private AddressBookMapper addressBookMapper;
    @Resource
    private AddressBookDao addressBookDao;
    @Resource
    private AddressHistoryDao addressHistoryDao;

    @Resource
    private UserMapper userMapper;

    /**
     * 查询历史地址
     */
    public APIResponse selectAddressHistory(APIRequest request) {
        // Param
        String userId = (String) request.getParameter("user_id");
        String page = (String) request.getParameter("pageNum");
        String size = (String) request.getParameter("pageSize");
        if (userId == null || userId.equals("")) return APIUtil.paramErrorResponse("user_id不能为空");
        if (page == null || page.equals("")) return APIUtil.paramErrorResponse("pageNum不能为空");
        if (size == null || size.equals("")) return APIUtil.paramErrorResponse("pageSize不能为空");
        int user_id = Integer.parseInt(userId);
        int pageNum = Integer.parseInt(page);
        int pageSzie = Integer.parseInt(size);
        if (user_id < 1) return APIUtil.paramErrorResponse("user_id不正确");
        if (pageNum < 1 || pageSzie < 1) return APIUtil.paramErrorResponse("分页参数无效");

        // Handle avatar
        List<AddressBookDTO> addressBookDTOList = addressBookMapper.selectAddressHistoryListByUserId(user_id, (pageNum - 1) * pageSzie, pageSzie);
        for (AddressBookDTO ab : addressBookDTOList) {
            Address address = ab.getAddress();
            User user = userMapper.selectUserByUserId(address.getUser_id());
            AddressDTO addressDTO = AddressFactory.entityToDto(address);
            String avatar = (user == null || user.getAvatar() == null) ? DK_USER_AVATAR_DEFAULT : user.getAvatar();
            addressDTO.setAvatar(avatar);
            // handle wechatname by hxy
            String wechatname = (user == null || user.getName() == null) ? "default_name" : user.getName();
            ab.setShip_wechatname(wechatname);
            ab.setAddressDTO(addressDTO);
        }

        return APIUtil.getResponse(SUCCESS, addressBookDTOList);
    }

    /**
     * 删除历史地址
     */
    public APIResponse deleteAddressHistory(APIRequest request) {
        // Param
        String addressHistoryId = (String) request.getParameter("address_history_id");
        if (addressHistoryId == null || addressHistoryId.equals(""))
            return APIUtil.paramErrorResponse("address_history_id不能为空");
        long address_history_id = Long.parseLong(addressHistoryId);
        if (address_history_id < 1)
            return APIUtil.paramErrorResponse("address_history_id不正确");

//        addressBookMapper.updateIsDeleteStatusByPrimaryKey(address_history_id, 1);
        AddressBook addressBook = addressBookDao.findOne(address_history_id);
        addressBook.setIs_delete(1);
        addressBookDao.save(addressBook);
        return APIUtil.getResponse(SUCCESS, null);
    }
}
