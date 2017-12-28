package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.entity.Address;
import com.sftc.web.model.vo.swaggerRequest.DistanceRequestVO;
import com.sftc.web.model.vo.swaggerResponse.AddressDistanceRespVO;
import com.sftc.web.model.vo.swaggerResponse.GeocoderAddressRespVO;
import com.sftc.web.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@Api(description = "地址相关文档")
@RequestMapping("address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @ApiOperation(value = "地址添加",httpMethod = "POST")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse addAddress(@RequestBody Address body) throws Exception {
        return addressService.addAddress(body);
    }

    @ApiOperation(value = "我的收件人地址",httpMethod = "GET")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse consigneeAddress() throws Exception {
        return addressService.consigneeAddress();
    }

    @ApiOperation(value = "修改收件人地址",httpMethod = "PUT")
    @RequestMapping(method = RequestMethod.PUT ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ApiResponse editAddress(@RequestBody Address body) throws Exception {
        return addressService.editAddress(body);
    }

    @ApiOperation(value = "地址解析",httpMethod = "GET",notes = "地址解析接口，地址转坐标",response = GeocoderAddressRespVO.class)
    @ApiImplicitParam(name = "address",value = "详细地址",paramType = "query",defaultValue = "深圳龙岗区花样年龙城广场",required = true)
    @RequestMapping(value = "/geocoder", method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse geocoderAddress(@RequestParam String address) throws Exception {
        return addressService.geocoderAddress(address);
    }

    @ApiOperation(value = "地址距离计算",httpMethod = "POST",response = AddressDistanceRespVO.class)
    @RequestMapping(value = "/distance", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse distanceAddress(@RequestBody DistanceRequestVO body) throws Exception {
        return addressService.getAddressDistance(body);
    }
}
