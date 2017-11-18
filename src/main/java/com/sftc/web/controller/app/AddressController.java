package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequestVO.DistanceRequestVO;
import com.sftc.web.model.entity.Address;
import com.sftc.web.model.vo.swaggerResponse.AddressDistanceRespVO;
import com.sftc.web.model.vo.swaggerResponse.GeocoderAddressRespVO;
import com.sftc.web.service.AddressService;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "地址相关文档")
@RequestMapping("address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @ApiIgnore
    @ApiOperation(value = "地址添加",httpMethod = "POST")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addAddress(@RequestBody Address address) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(address);
        return addressService.addAddress(request);
    }

    @ApiIgnore
    @ApiOperation(value = "我的收件人地址",httpMethod = "GET")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse consigneeAddress(HttpServletRequest request) throws Exception {
        return addressService.consigneeAddress(new APIRequest(request));
    }

    @ApiIgnore
    @ApiOperation(value = "修改收件人地址",httpMethod = "PUT")
    @RequestMapping(method = RequestMethod.PUT ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    APIResponse editAddress(@RequestBody Address address) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(address);
        return addressService.editAddress(request);
    }

    @ApiOperation(value = "地址解析",httpMethod = "GET",notes = "地址解析接口，地址转坐标",response = GeocoderAddressRespVO.class)
    @ApiImplicitParam(name = "address",value = "详细地址",paramType = "query",defaultValue = "深圳龙岗区花样年龙城广场")
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 401,message = "The query fails"),
            @ApiResponse(code = 402,message = "The submit fails"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    @RequestMapping(value = "/geocoder", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse geocoderAddress(HttpServletRequest request) throws Exception {
        return addressService.geocoderAddress(new APIRequest(request));
    }

    @ApiOperation(value = "地址距离计算",httpMethod = "POST",response = AddressDistanceRespVO.class)
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 401,message = "The query fails"),
            @ApiResponse(code = 402,message = "The submit fails"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    @RequestMapping(value = "/distance", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse distanceAddress(@RequestBody DistanceRequestVO distanceRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(distanceRequestVO);
        return addressService.getAddressDistance(request);
    }
}
