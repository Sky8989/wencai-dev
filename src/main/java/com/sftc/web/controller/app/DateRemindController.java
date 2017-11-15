package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.DateRemind;
import com.sftc.web.service.DateRemindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@ApiIgnore
@Api(description = "纪念日")
@RequestMapping("date")
public class DateRemindController {

    @Resource
    private DateRemindService dateRemindService;

    @ApiOperation(value = "添加好友日期提醒",httpMethod = "POST")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addDateRemind(@RequestBody DateRemind dateRemind) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(dateRemind);
        return dateRemindService.addFriendDateRemind(apiRequest);
    }

    @ApiOperation(value = "删除好友日期提醒",httpMethod = "DELETE")
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deleteDateRemind(HttpServletRequest request) throws Exception {
        return dateRemindService.deleteFriendDateRemind(new APIRequest(request));
    }

    @ApiOperation(value = "获取好友日期提醒",httpMethod = "GET")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse selectDateRemind(HttpServletRequest request) throws Exception {
        return dateRemindService.selectFriendDateRemind(new APIRequest(request));
    }
}
