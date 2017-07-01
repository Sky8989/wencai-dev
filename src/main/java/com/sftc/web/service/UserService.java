package com.sftc.web.service;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import com.sftc.web.model.reqeustParam.UserParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.service
 * @Description: 用户操作接口
 * @date 17/4/1
 * @Time 下午9:32
 */
public interface UserService {

    /**
     * 登录
     * @param user
     * @return
     */
    APIResponse login(UserParam userParam) throws Exception;
    Token getToken(int id);

    ModelAndView selectUserList(HttpServletRequest request);
}
