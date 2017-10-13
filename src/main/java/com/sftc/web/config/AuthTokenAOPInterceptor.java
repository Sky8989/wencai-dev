package com.sftc.web.config;

import com.google.gson.Gson;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.model.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class AuthTokenAOPInterceptor {
    @Resource
    private TokenMapper tokenMapper;

    public void before() throws Throwable {
        ServletRequestAttributes res = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = res.getRequest();
        HttpServletResponse response = res.getResponse();
        String token = request.getHeader("token");
        if (token != null && !token.equals("")) {
            APIResponse error = authTokenCheck(token);//校验用户
            if (error != null) {
                error(response, error);
            }
        } else {
            error(response, APIUtil.selectErrorResponse("token验证失败", null));
        }
    }

    private void error(HttpServletResponse response, APIResponse error) {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        OutputStreamWriter ow = null;
        try {
            ServletOutputStream out = response.getOutputStream();
            ow = new OutputStreamWriter(out, "UTF-8");
            ow.write(new Gson().toJson(error));
        } catch (IOException e) {
            e.fillInStackTrace();
        } finally {
            try {
                ow.flush();
                ow.close();
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }
    }

    private APIResponse authTokenCheck(String token) throws Exception {
        User user = tokenMapper.tokenInterceptor(token);
        if (user != null) {
            return null;
        } else {
            return APIUtil.selectErrorResponse("token验证失败", null);
        }
    }
}

