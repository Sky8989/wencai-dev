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

@Controller
@RequestMapping("friend")
public class UserContactController extends AbstractBasicController {

    @Resource
    private UserContactLabelService userContactLabelService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse allFriend(@RequestBody Paging paging) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(paging);
        return userContactService.getFriendList(request);
    }

    @RequestMapping(value = "/contacts", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getContactInfo(@RequestBody UserContactParam userContactParam) throws Exception {
        return userContactService.getContactInfo(userContactParam);
    }

    @RequestMapping(value = "/star", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getContactInfo(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return userContactService.starFriend(request);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse friendDetail(HttpServletRequest request) throws Exception {
        return userContactService.getFriendDetail(new APIRequest(request));
    }

//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    public @ResponseBody
//    APIResponse addFriend(@RequestBody UserContactParam userContactParam) throws Exception {
//        return userContactService.addFriend(userContactParam);
//    }

    @RequestMapping(value = "/label/add", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addLabelFriend(@RequestBody UserContactLabel userContactLabel) throws Exception {
        return userContactLabelService.addLabelForFriend(userContactLabel);
    }

    @RequestMapping(value = "/label/delete", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse deleteLabelFriend(HttpServletRequest request) throws Exception {
        return userContactLabelService.deleteLabelForFriend(new APIRequest(request));
    }

    @RequestMapping(value = "/label", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getFriendLabelList(HttpServletRequest request) throws Exception {
        return userContactLabelService.selectFriendLabelList(new APIRequest(request));
    }

    @RequestMapping(value = "/notes/update", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse updateNotes(HttpServletRequest request) throws Exception {
        return userContactService.updateNotesAndPicture(new APIRequest(request));
    }

}
