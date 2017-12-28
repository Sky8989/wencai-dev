package com.sftc.web.service.impl;

import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.vo.swaggerRequest.DeleteTokenVO;
import com.sftc.web.service.TokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.sftc.tools.api.ApiStatus.SUCCESS;


@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenMapper tokenMapper;

    @Override
    @SuppressWarnings("unused")
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse deleteToken(DeleteTokenVO requestParam) throws Exception {
        String mobile = requestParam.getMobile();
        User user;
        if (StringUtils.isNotBlank(mobile)) {
            user = userMapper.selectUserByPhone(mobile);
            if (user == null) {
                return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "当前手机号还未注册");
            }
            //删除token表中对应的数据
            tokenMapper.deleteTokenByUserId(user.getUuid());
        }
        return ApiUtil.getResponse(SUCCESS, null);
    }
}
