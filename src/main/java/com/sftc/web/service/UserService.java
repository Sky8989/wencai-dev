package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
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
     * 普通登陆
     *
     * @param userParam
     * @return
     * @throws Exception
     */
    APIResponse login(UserParam userParam) throws Exception;

    /**
     * 超级登陆 自动刷新token
     *
     * @param userParam
     * @return
     * @throws Exception
     */
    APIResponse superLogin(UserParam userParam) throws Exception;

    /**
     * 获取token的公用方法
     *
     * @param id
     * @return
     */
    Token getToken(int id);

    /**
     * 解除绑定操作，原微信号，解除原有手机号
     *
     * @param user_id
     * @return
     * @throws Exception
     */
    APIResponse deleteMobile(int user_id) throws Exception;

    /**
     * 修改手机号码 即重新绑定新手机号
     *
     * @param apiRequest
     * @return
     * @throws Exception
     */
    APIResponse updateMobile(APIRequest apiRequest) throws Exception;

    //10-12日提出的新需求 更新个人信息 作为中控给顺丰验证和更新个人信息
    APIResponse updatePersonMessage(APIRequest apiRequest) throws Exception;

    /**
     * 下面是CMS后台所使用的接口
     */
    APIResponse selectUserListByPage(APIRequest request) throws Exception;
}
