package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.UserContactMapper;
import com.sftc.web.model.UserContact;
import com.sftc.web.service.UserContactService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service.impl
 * @Description: 联系人操作接口实现
 * @date 17/4/1
 * @Time 下午9:33
 */
@Service("userContactService")
public class UserContactServiceImpl implements UserContactService {

    @Resource
    private UserContactMapper userContactMapper;

    /*
     * 查询该联系人的所有好友
     */
    public APIResponse findUserFriend(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        String user_id = (String) request.getParameter("user_id");
        List<UserContact> userContactList = null;
        if (user_id != null) {
            try {
                userContactList = userContactMapper.friendList(Integer.parseInt(user_id));
            } catch (Exception e) {
                e.printStackTrace();
                status = APIStatus.USER_CONTACT_FAIL;
            }
        }
        return APIUtil.getResponse(status, userContactList);
    }
}
