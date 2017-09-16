package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.service.LatitudeLongitudeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("latitudeLongitude")
public class LatitudeLongitudeController extends AbstractBasicController {
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
