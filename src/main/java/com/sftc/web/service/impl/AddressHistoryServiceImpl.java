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
        if (userId == null || userId.equals(""))
            return APIUtil.paramErrorResponse("user_id不能为空");
        int user_id = Integer.parseInt(userId);
        if (user_id < 1)
            return APIUtil.paramErrorResponse("user_id不正确");

        // Handle avatar
        List<AddressHistory> addressHistories = addressHistoryMapper.selectAddressHistoryListByUserId(user_id);
        for (AddressHistory ah: addressHistories) {
            Address address = ah.getAddress();
            User user = userMapper.selectUserByUserId(address.getUser_id());
            String avatar = user.getAvatar() == null ? DK_USER_AVATAR_DEFAULT : user.getAvatar();
            address.setAvatar(avatar);
        }

        return APIUtil.getResponse(SUCCESS, addressHistories);
    }
}
