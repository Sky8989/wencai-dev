package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.DateRemind;
import com.sftc.web.service.DateRemindService;
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
 * @Description: 日期提醒接口
 * @date 2017/5/22
 * @Time 下午10:02
 */
@Controller
@RequestMapping("date")
public class DateRemindController {

    @Resource
    private DateRemindService dateRemindService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, headers = "api-version=1")
    public @ResponseBody
    APIResponse addDateRemind(@RequestBody DateRemind dateRemind) throws Exception {
        return dateRemindService.addFriendDateRemind(dateRemind);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse deleteDateRemind(HttpServletRequest request) throws Exception {
        return dateRemindService.deleteFriendDateRemind(new APIRequest(request));
    }
}
