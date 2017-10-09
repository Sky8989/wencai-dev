package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.reqeustParam.UserParam;
import com.sftc.web.service.UserService;
import io.swagger.annotations.Api;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@RequestMapping("user")
@Api(description = "用户相关")
@Controller
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse login(@RequestBody UserParam userParam) throws Exception {
        return userService.login(userParam);
    }

    @RequestMapping(value = "/superLogin", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse superLogin(@RequestBody UserParam userParam) throws Exception {
        return userService.superLogin(userParam);
    }

    @RequestMapping(value = "/unbind/common", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse commonUnbind(@RequestBody Object object) throws Exception {
        int user_id = JSONObject.fromObject(object).getInt("user_id");
        return userService.deleteMobile(user_id);
    }

    @RequestMapping(value = "/unbind/newMobile", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse bindNewMobile(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return userService.updateMobile(apiRequest);
    }
}
