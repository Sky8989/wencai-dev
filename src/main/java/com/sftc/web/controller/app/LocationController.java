package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.vo.swaggerRequest.CoordinateVO;
import com.sftc.web.model.vo.swaggerRequest.LLSetConstantVO;
import com.sftc.web.model.vo.swaggerResponse.RandomLocationRespVO;
import com.sftc.web.service.LatitudeLongitudeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "获取随机经纬度",httpMethod = "POST",notes = "提供功能：更改随机获取经纬度所使用的常量，控制生成数据的数量和范围值\n" +
            "注意事项：原则上最大数量的值要大于最小数量要，范围值的单位是千米",response = RandomLocationRespVO.class)
    @RequestMapping(value = "/random", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    public @ResponseBody
    APIResponse getRandomPoint(@RequestBody CoordinateVO coordinateVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(coordinateVO);
        return latitudeLongitudeService.getLatitudeLongitude(apiRequest);
    }

    @ApiIgnore
    @ApiOperation(value = "获取经纬度接口的相关参数",httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 401,message = "The query fails"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    @RequestMapping(value = "/constant", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse setConstant(@RequestBody LLSetConstantVO llSetConstantVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(llSetConstantVO);
        return latitudeLongitudeService.setLatitudeLongitudeConstant(apiRequest);
    }
}
