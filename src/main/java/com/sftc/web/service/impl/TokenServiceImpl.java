package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.*;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import com.sftc.web.service.TokenService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Transactional
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenMapper tokenMapper;

	@SuppressWarnings("unused")
	public APIResponse deleteToken(APIRequest apiRequest) throws Exception {
		Object requestParam = apiRequest.getRequestParam();
		String mobile = JSONObject.fromObject(requestParam).getString("mobile");
		User user = null;
		if(mobile != null && !mobile.equals("")){
			user = userMapper.selectUserByPhone(mobile);
		}
		if(user == null){
			 return APIUtil.submitErrorResponse("当前手机号还未注册",user);
		}
		//删除token表中对应的数据
		tokenMapper.deleteTokenByUserId(user.getId());
		return  APIUtil.getResponse(SUCCESS, null);
	}
}
