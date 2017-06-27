package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.DateRemindMapper;
import com.sftc.web.model.DateRemind;
import com.sftc.web.service.DateRemindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("dateRemindService")
public class DateRemindServiceImpl implements DateRemindService {

    @Resource
    private DateRemindMapper dateRemindMapper;

    public APIResponse addFriendDateRemind(DateRemind dateRemind) {
        APIStatus state = APIStatus.SUCCESS;
        dateRemind.setCreate_time(Long.toString(System.currentTimeMillis()));
        try {
            dateRemindMapper.addDateRemind(dateRemind);
        } catch (Exception e) {
            e.printStackTrace();
            state = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(state, null);
    }

    public APIResponse deleteFriendDateRemind(APIRequest request) {
        APIStatus state = APIStatus.SUCCESS;
        try {
            dateRemindMapper.deleteDateRemind(
                    Integer.parseInt(request.getParameter("id").toString()));
        } catch (Exception e) {
            e.printStackTrace();
            state = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(state, null);
    }
}
