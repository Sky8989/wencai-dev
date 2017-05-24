package com.sftc.web.service.impl;

import com.sftc.tools.api.*;
import com.sftc.web.mapper.*;
import com.sftc.web.model.*;
import com.sftc.web.model.reqeustParam.UserContactParam;
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

    @Resource
    private UserContactLabelMapper userContactLabelMapper;

    @Resource
    private DateRemindMapper dateRemindMapper;

    /*
     * 添加好友
     */
    public APIResponse addFriend(UserContactParam userContactParam) {
        APIStatus status = APIStatus.SUCCESS;
        userContactParam.setCreate_time(Long.toString(System.currentTimeMillis()));
        try {
            userContactMapper.addFriend(userContactParam);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SUBMIT_FAIL;
        }
        return APIUtil.getResponse(status, null);
    }

    /*
     * 根据id查询好友详情
     */
    public APIResponse getFriendDetail(UserContactParam userContactParam) {
        APIStatus status = APIStatus.SUCCESS;
        int id = userContactParam.getId();
        UserContact userContact = userContactMapper.friendDetail(id);
        List<UserContactLabel> userContactLabelList = userContactLabelMapper.getFriendLabelList(id);
        List<DateRemind> dateRemindList = dateRemindMapper.getDateRemindList(id);
        userContact.setUserContactLabelList(userContactLabelList);
        userContact.setDateRemindList(dateRemindList);
        return APIUtil.getResponse(status, userContact);
    }

    /*
     * 获取某个用户的所有好友（带分页）
     */
    public APIResponse getFriendList(Paging paging) {
        APIStatus status = APIStatus.SUCCESS;
        paging.setPageNum(paging.getPageNum() - 1);
        List<UserContact> userContactList = null;
        try {
            userContactList = userContactMapper.friendList(paging);
        } catch (Exception e) {
            e.printStackTrace();
            status = APIStatus.SELECT_FAIL;
        }
        return APIUtil.getResponse(status, userContactList);
    }
}
