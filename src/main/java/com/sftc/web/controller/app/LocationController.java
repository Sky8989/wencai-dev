package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.SwaggerRequestVO.CoordinateVO;
import com.sftc.web.model.SwaggerRequestVO.LLSetConstantVO;
import com.sftc.web.service.LatitudeLongitudeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

@Controller
@Api(description = "经纬度")
@RequestMapping("location")
public class LocationController extends BaseController {
    @Resource
    private LatitudeLongitudeService latitudeLongitudeService;

    @ApiOperation(value = "获取随机经纬度",httpMethod = "POST")
    @RequestMapping(value = "/random", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse getRandomPoint(@RequestBody CoordinateVO coordinateVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(coordinateVO);
        return latitudeLongitudeService.getLatitudeLongitude(apiRequest);
    }

    @ApiOperation(value = "获取经纬度接口的相关参数",httpMethod = "POST")
    @RequestMapping(value = "/constant", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setConstant(@RequestBody LLSetConstantVO llSetConstantVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(llSetConstantVO);
        return latitudeLongitudeService.setLatitudeLongitudeConstant(apiRequest);
    }
}
