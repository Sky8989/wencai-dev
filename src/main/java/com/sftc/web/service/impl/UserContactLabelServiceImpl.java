package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.UserContactLabelMapper;
import com.sftc.web.model.UserContactLabel;
import com.sftc.web.service.UserContactLabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userContactLabelService")
public class UserContactLabelServiceImpl implements UserContactLabelService {

    @Resource
    private UserContactLabelMapper userContactLabelMapper;
    // 添加好友普通标签
    public APIResponse addLabelForFriend(UserContactLabel userContactLabel) {
        APIStatus status = APIStatus.SUCCESS;
        userContactLabel.setCreate_time(Long.toString(System.currentTimeMillis()));
        userContactLabelMapper.addLabel(userContactLabel);
        return APIUtil.getResponse(status, userContactLabel);
    }
    // 删除好友标签
    public APIResponse deleteLabelForFriend(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        int laberId = Integer.parseInt(request.getParameter("id").toString());
        userContactLabelMapper.deleteFriendLabel(laberId);
        return APIUtil.getResponse(status, laberId);
    }
    // 获取好友标签列表
    public APIResponse selectFriendLabelList(APIRequest request) {
        APIStatus status = APIStatus.SELECT_FAIL;
        int user_contact_id = Integer.parseInt(request.getParameter("user_contact_id").toString());
        List<UserContactLabel> userContactLabelList = userContactLabelMapper.getFriendLabelList(user_contact_id);
        if (userContactLabelList != null && userContactLabelList.size() >= 1 ) {
            return APIUtil.getResponse(APIStatus.SUCCESS, userContactLabelList);
        }else return APIUtil.selectErrorResponse(" no label with that id!",user_contact_id);
    }
}
