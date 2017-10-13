package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.Address;
import com.sftc.web.service.AddressHistoryService;
import com.sftc.web.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "地址操作")
@RequestMapping("address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @Resource
    private AddressHistoryService addressHistoryService;

    @ApiOperation(value = "地址添加",httpMethod = "POST")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addAddress(@RequestBody Address address) throws Exception {
        return addressService.addAddress(address);
    }

    @ApiOperation(value = "我的收件人地址",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id",value = "用户id",required = true,paramType = "query",defaultValue = "10028")
    })
    @RequestMapping(value = "/consignee", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse consigneeAddress(HttpServletRequest request) throws Exception {
        return addressService.consigneeAddress(new APIRequest(request));
    }

    @ApiOperation(value = "修改收件人地址",httpMethod = "POST")
    @RequestMapping(value = "/edit", method = RequestMethod.POST ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    APIResponse editAddress(@RequestBody Address address) throws Exception {
        return addressService.editAddress(address);
    }

    @ApiOperation(value = "地址解析",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address",value = "详细地址",paramType = "query",defaultValue = "深圳龙岗区花样年龙城广场")
    })
    @RequestMapping(value = "/geocoder", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse geocoderAddress(HttpServletRequest request) throws Exception {
        return addressService.geocoderAddress(new APIRequest(request));
    }

    @ApiOperation(value = "地址距离计算",httpMethod = "POST")
    @RequestMapping(value = "/distance", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse distanceAddress(@RequestBody Object object) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(object);
        return addressService.getAddressDistance(request);
    }

    @ApiOperation(value = "查询历史地址",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id",value = "用户id",paramType = "query",defaultValue = "10028"),
            @ApiImplicitParam(name = "pageNum",value = "页码",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "每页数量",paramType = "query",defaultValue = "10"),
    })
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse selectAddressHistory(HttpServletRequest request) throws Exception {
        return addressHistoryService.selectAddressHistory(new APIRequest(request));
    }

    @ApiOperation(value = "删除历史地址(删除的是地址簿中类型为address_history的地址簿)  软删除 ",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address_history_id",value = "历史地址id",paramType = "query",defaultValue = "924")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse deleteAddress(HttpServletRequest request) throws Exception {
        return addressHistoryService.deleteAddressHistory(new APIRequest(request));
    }
}
