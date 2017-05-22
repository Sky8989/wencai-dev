package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.reqeustParam.UserContactParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description:
 * @date 2017/5/21
 * @Time 下午10:21
 */
@Controller
@RequestMapping("friend")
public class UserContactController extends AbstractBasicController {

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse allFriend(HttpServletRequest request) throws Exception {
        return userContactService.findUserFriend(new APIRequest(request));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse addFriend(@RequestBody UserContactParam userContactParam) throws Exception {
        return userContactService.addFriend(userContactParam);
    }
}
