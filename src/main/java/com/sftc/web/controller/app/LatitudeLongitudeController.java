package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.service.LatitudeLongitudeService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Api(description = "经纬度")
@RequestMapping("latitudeLongitude")
public class LatitudeLongitudeController extends BaseController {
    @Resource
    private LatitudeLongitudeService latitudeLongitudeService;

    @RequestMapping(value = "/getRandomPoint", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getRandomPoint(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return latitudeLongitudeService.getLatitudeLongitude(apiRequest);
    }

    @RequestMapping(value = "/setConstant", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setConstant(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return latitudeLongitudeService.setLatitudeLongitudeConstant(apiRequest);
    }
}
