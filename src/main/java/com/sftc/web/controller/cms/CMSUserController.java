package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.vo.swaggerRequestVO.UserParam;
import com.sftc.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("cms/user")
@Controller
public class CMSUserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestBody UserParam userParam) throws Exception {
        return null;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse UserList(HttpServletRequest request) throws Exception {
        // 分页查询
        return userService.selectUserListByPage(new APIRequest(request));
    }
}
