package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.service.TokenService;
import com.sftc.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("cms/token")
@Controller
public class CMSTokenController extends AbstractBasicController {

    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse UserList(HttpServletRequest request) throws Exception {
        // 分页查询
        return tokenService.getTokenList(new APIRequest(request));
    }
}
