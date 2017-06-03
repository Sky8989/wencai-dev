package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.model.Paging;
import com.sftc.web.model.UserContactLabel;
import com.sftc.web.model.reqeustParam.UserContactParam;
import com.sftc.web.service.UserContactLabelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description: 好友接口
 * @date 2017/5/21
 * @Time 下午10:21
 */
@Controller
@RequestMapping("friend")
public class UserContactController extends AbstractBasicController {

    @Resource
    private UserContactLabelService userContactLabelService;

    @RequestMapping(value = "/list", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse allFriend(@RequestBody Paging paging) throws Exception {
        return userContactService.getFriendList(paging);
    }

    @RequestMapping(value = "/contacts", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse getContactInfo(@RequestBody UserContactParam userContactParam) throws Exception {
        return userContactService.getContactInfo(userContactParam);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse friendDetail(@RequestBody UserContactParam userContactParam) throws Exception {
        return userContactService.getFriendDetail(userContactParam);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse addFriend(@RequestBody UserContactParam userContactParam) throws Exception {
        return userContactService.addFriend(userContactParam);
    }

    @RequestMapping(value = "/label/add", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse addLabelFriend(@RequestBody UserContactLabel userContactLabel) throws Exception {
        return userContactLabelService.addLabelForFriend(userContactLabel);
    }

    @RequestMapping(value = "/label/delete", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse deleteLabelFriend(HttpServletRequest request) throws Exception {
        return userContactLabelService.deleteLabelForFriend(new APIRequest(request));
    }

    @RequestMapping(value = "/label", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse getFriendLabelList(HttpServletRequest request) throws Exception {
        return userContactLabelService.getFriendLabelList(new APIRequest(request));
    }
}
