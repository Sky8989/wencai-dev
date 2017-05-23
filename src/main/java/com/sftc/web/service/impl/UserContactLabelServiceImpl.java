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

    public APIResponse addLabelForFriend(UserContactLabel userContactLabel) {
        APIStatus status = APIStatus.SUCCESS;
        userContactLabel.setCreate_time(Long.toString(System.currentTimeMillis()));
        try {
            userContactLabelMapper.addLabel(userContactLabel);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    public APIResponse deleteLabelForFriend(APIRequest request) {
        APIStatus status = APIStatus.SUCCESS;
        try {
            userContactLabelMapper.deleteFriendLabel(
                    Integer.parseInt(request.getParameter("id").toString()));
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    public APIResponse getFriendLabelList(APIRequest request) {
        APIStatus status = APIStatus.SELECT_FAIL;
        int id = Integer.parseInt(request.getParameter("user_contact_id").toString());
        List<UserContactLabel> userContactLabelList = userContactLabelMapper.getFriendLabelList(id);
        if (userContactLabelList != null) {
            status = APIStatus.SUCCESS;
        }
        return APIUtil.getResponse(status, userContactLabelList);
    }
}
