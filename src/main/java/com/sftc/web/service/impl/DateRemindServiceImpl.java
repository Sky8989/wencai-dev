package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.dao.mybatis.DateRemindMapper;
import com.sftc.web.model.DateRemind;
import com.sftc.web.service.DateRemindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("dateRemindService")
public class DateRemindServiceImpl implements DateRemindService {

    @Resource
    private DateRemindMapper dateRemindMapper;

    // 添加 好友日期提醒
    public APIResponse addFriendDateRemind(APIRequest apiRequest) {
        DateRemind dateRemind = (DateRemind) apiRequest.getRequestParam();
        APIStatus state = APIStatus.SUCCESS;
        dateRemind.setCreate_time(Long.toString(System.currentTimeMillis()));
        dateRemindMapper.addDateRemind(dateRemind);
        return APIUtil.getResponse(state, dateRemind);
    }

    // 删除 好友日期提醒
    public APIResponse deleteFriendDateRemind(APIRequest request) {
        int id = Integer.parseInt(request.getParameter("id").toString());
        dateRemindMapper.deleteDateRemind(id);
        return APIUtil.getResponse(APIStatus.SUCCESS, id);
    }

    // 获取 好友日期提醒 列表
    public APIResponse selectFriendDateRemind(APIRequest request) {
        int user_contact_id = Integer.parseInt(request.getParameter("user_contact_id").toString());
        List<DateRemind> dateRemindList = dateRemindMapper.selectDateRemindList(user_contact_id);
        if (dateRemindList != null && dateRemindList.size() >= 1) {
            return APIUtil.getResponse(APIStatus.SUCCESS, dateRemindList);
        } else return APIUtil.selectErrorResponse(" no DateRemind with that id!", user_contact_id);
    }
}
