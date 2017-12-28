package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.sf.SfTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.tools.utils.OrderStateSyncUtil;
import com.sftc.web.dao.mybatis.*;
import com.sftc.web.model.dto.FriendRecordDTO;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.entity.UserContact;
import com.sftc.web.model.entity.UserContactLabel;
import com.sftc.web.model.vo.swaggerRequest.FriendListVO;
import com.sftc.web.model.vo.swaggerRequest.FriendStarVO;
import com.sftc.web.model.vo.swaggerRequest.UserContactParamVO;
import com.sftc.web.service.UserContactService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.api.ApiStatus.SUCCESS;

@Service("userContactService")
public class UserContactServiceImpl implements UserContactService {

    @Resource
    private UserContactMapper userContactMapper;

    @Resource
    private UserContactLabelMapper userContactLabelMapper;

    @Resource
    private OrderExpressMapper orderExpressMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 根据id查询好友详情
     */
    @Override
    public ApiResponse getFriendDetail(String friendUUId) {

        if (StringUtils.isBlank(friendUUId)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "friend_uuid不能为空");
        }

        String userUUID = TokenUtils.getInstance().getUserUUID();

        if (StringUtils.isBlank(userUUID)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "user_uuid无效");
        }

        UserContact userContact = userContactMapper.friendDetail(userUUID, friendUUId);

        if (userContact == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "非好友关系");
        }

        List<UserContactLabel> userContactLabelList = userContactLabelMapper.getFriendLabelList(userContact.getId());
        userContact.setUserContactLabelList(userContactLabelList);
        return ApiUtil.getResponse(SUCCESS, userContact);
    }

    /**
     * 获取用户的好友列表
     */
    @Override
    public ApiResponse getFriendList(FriendListVO friendListVO) {
        String userUUId = TokenUtils.getInstance().getUserUUID();
        // verify params
        if (friendListVO.getPageNum() < 1 || friendListVO.getPageSize() < 1) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "分页参数无效");
        }
        friendListVO.setUser_uuid(userUUId);
        friendListVO.setPageNum((friendListVO.getPageNum() - 1) * friendListVO.getPageSize());
        List<UserContact> userContactList;
        try {
            userContactList = userContactMapper.friendList(friendListVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiUtil.logicErrorResponse(e.getLocalizedMessage(), e);
        }

        return ApiUtil.getResponse(SUCCESS, userContactList);
    }

    /**
     * 来往记录
     */
    @Override
    public ApiResponse getContactInfo(UserContactParamVO userContactParamVO) {
        String userUUId = TokenUtils.getInstance().getUserUUID();
        // handle param
        if (userContactParamVO.getAccess_token() == null || userContactParamVO.getAccess_token().length() == 0) {
            //传入公共token
            userContactParamVO.setAccess_token(SfTokenHelper.COMMON_ACCESSTOKEN);
        } else if (StringUtils.isBlank(userUUId)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "用户id不能为空");
        } else if (StringUtils.isBlank(userContactParamVO.getFriend_uuid())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "好友id不能为空");
        } else if (userUUId.equals(userContactParamVO.getFriend_uuid())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "I believe that you can't fuck yourself !");
        } else if (userContactParamVO.getPageNum() < 1 || userContactParamVO.getPageSize() < 1) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "分页参数无效");
        }
        userContactParamVO.setUser_uuid(userUUId);
        PageHelper.startPage(userContactParamVO.getPageNum(), userContactParamVO.getPageSize());
        List<OrderExpress> orderExpressList = orderExpressMapper.selectExpressForContactInfo(userContactParamVO.getUser_uuid(),
                userContactParamVO.getFriend_uuid());
        //同步快递路由信息
        ApiResponse apiResponse = new OrderStateSyncUtil().syncOrderState(orderExpressList, userContactParamVO.getAccess_token(),
                orderMapper, orderExpressMapper);
        if (apiResponse != null) {
            return apiResponse;
        }

        //修改为物理分页参数
        // pageNum -> startIndex
        userContactParamVO.setPageNum((userContactParamVO.getPageNum() - 1) * userContactParamVO.getPageSize());
        // callback
        List<FriendRecordDTO> friendRecordDTOS = userContactMapper.selectCirclesContact(userContactParamVO);
        for (FriendRecordDTO friendRecordDTO : friendRecordDTOS) {
            User sender = userMapper.selectUserByUserUUId(friendRecordDTO.getSender_user_uuid());
            User receiver = userMapper.selectUserByUserUUId(friendRecordDTO.getShip_user_uuid());
            if (sender != null) {
                friendRecordDTO.setSender_icon(sender.getAvatar());
                friendRecordDTO.setSender_wechatname(sender.getName());
            }
            if (receiver != null) {
                friendRecordDTO.setShip_icon(receiver.getAvatar());
                friendRecordDTO.setShip_wechatname(receiver.getName());
            }
        }
        return ApiUtil.getResponse(SUCCESS, friendRecordDTOS);
    }

    /**
     * 星标好友
     */
    @Override
    public ApiResponse starFriend(FriendStarVO friendStarVO) {
        // Param
        String userUUID = TokenUtils.getInstance().getUserUUID();
        String friendUUId = friendStarVO.getFriend_uuid();
        int isStar = friendStarVO.getIs_star();
        if (StringUtils.isBlank(userUUID)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "参数user_uuid无效");
        }
        if (StringUtils.isBlank(friendUUId)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "参数friend_uuid无效");
        }
        if (isStar != 0 && isStar != 1) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "参数is_star无效");
        }

        UserContact userContact = userContactMapper.friendDetail(userUUID, friendUUId);
        if (userContact == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "非好友关系");
        }

        // Result
        try {
            userContactMapper.starFriend(userUUID, friendUUId, isStar);
            userContact = userContactMapper.friendDetail(userUUID, friendUUId);
        } catch (Exception e) {
            return ApiUtil.logicErrorResponse(e.getLocalizedMessage(), e);
        }

        return ApiUtil.getResponse(SUCCESS, userContact);
    }

}
