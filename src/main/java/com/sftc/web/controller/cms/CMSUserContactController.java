package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.UserContactService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by huxingyue on 2017/7/5.
 */

@Controller
@RequestMapping("cms/userContact")
public class CMSUserContactController {
    @Resource
    private UserContactService userContactService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse UserList(HttpServletRequest request) throws Exception{
        // 分页查询
        return userContactService.selectUserContactListByPage(new APIRequest(request));
    }
}
