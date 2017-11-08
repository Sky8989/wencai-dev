package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.DateRemind;
import com.sftc.web.service.DateRemindService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "纪念日")
@RequestMapping("date")
public class DateRemindController {

    @Resource
    private DateRemindService dateRemindService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addDateRemind(@RequestBody DateRemind dateRemind) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(dateRemind);
        return dateRemindService.addFriendDateRemind(apiRequest);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse deleteDateRemind(HttpServletRequest request) throws Exception {
        return dateRemindService.deleteFriendDateRemind(new APIRequest(request));
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse selectDateRemind(HttpServletRequest request) throws Exception {
        return dateRemindService.selectFriendDateRemind(new APIRequest(request));
    }
}
