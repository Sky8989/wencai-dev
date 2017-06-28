package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.AddressHistoryMapper;
import com.sftc.web.mapper.UserMapper;
import com.sftc.web.model.Address;
import com.sftc.web.model.AddressHistory;
import com.sftc.web.model.User;
import com.sftc.web.service.AddressHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.api.APIConstant.DK_USER_AVATAR_DEFAULT;
import static com.sftc.tools.api.APIStatus.SUCCESS;

@Service
public class AddressHistoryServiceImpl implements AddressHistoryService {

    @Resource
    private AddressHistoryMapper addressHistoryMapper;

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
        List<AddressHistory> addressHistories = addressHistoryMapper.selectAddressHistoryListByUserId(user_id, (pageNum - 1) * pageSzie, pageSzie);
        for (AddressHistory ah : addressHistories) {
            Address address = ah.getAddress();
            User user = userMapper.selectUserByUserId(address.getUser_id());
            String avatar = (user == null || user.getAvatar() == null) ? DK_USER_AVATAR_DEFAULT : user.getAvatar();
            address.setAvatar(avatar);
        }

        return APIUtil.getResponse(SUCCESS, addressHistories);
    }

    /**
     * 删除历史地址
     */
    public APIResponse deleteAddressHistory(APIRequest request) {
        // Param
        String addressHistoryId = (String) request.getParameter("address_history_id");
        if (addressHistoryId == null || addressHistoryId.equals(""))
            return APIUtil.paramErrorResponse("address_history_id不能为空");
        int address_history_id = Integer.parseInt(addressHistoryId);
        if (address_history_id < 1)
            return APIUtil.paramErrorResponse("address_history_id不正确");

        addressHistoryMapper.deleteAddressHistoryWithId(address_history_id);

        return APIUtil.getResponse(SUCCESS, null);
    }
}
