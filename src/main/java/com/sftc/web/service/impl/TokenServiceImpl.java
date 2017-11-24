package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import com.sftc.web.service.TokenService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_GET_TOKEN;
import static com.sftc.tools.constant.SFConstant.SF_LOGIN;
@Transactional
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenMapper tokenMapper;

    /**
     * @param apiRequest
     * @return
     * @throws Exception
     */
    public APIResponse getTokenList(APIRequest apiRequest) throws Exception {
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        // 此处封装了 User的构造方法
        Token token = new Token(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        //  使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey).doSelectPageInfo(() -> tokenMapper.getTokenList(token));
        //  处理结果
        if (pageInfo.getList().size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(SUCCESS, pageInfo);
        }
    }

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
