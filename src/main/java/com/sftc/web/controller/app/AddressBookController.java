package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "地址簿相关接口")
@RequestMapping("addressBook")
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    @ApiOperation(value = "006添加地址簿", httpMethod = "POST")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addAddress(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return addressBookService.addAddressBook(apiRequest);
    }

    @ApiOperation(value = "007删除地址簿", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressBook_id", value = "地址簿id", required = true, paramType = "path")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse deleteAddress(HttpServletRequest httpServletRequest) throws Exception {
        APIRequest apiRequest = new APIRequest(httpServletRequest);
        return addressBookService.deleteAddressBook(apiRequest);
    }

    @ApiOperation(value = "008修改地址簿", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址簿id", required = true),
            @ApiImplicitParam(name = "address", value = "地址实体", required = true, dataType = "com.sftc.web.model.entity.Address"),
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse updateAddress(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return addressBookService.updateAddressBook(apiRequest);
    }

    @ApiOperation(value = "009地址簿查找", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", value = "用户id", required = true, paramType = "query", defaultValue = "10028"),
            @ApiImplicitParam(name = "address_book_type", value = "地址簿类型 sender/ship", required = true, paramType = "query", defaultValue = "sender"),
    })
    @RequestMapping(value = "/selectList", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse updateAddress(HttpServletRequest httpServletRequest) throws Exception {
        APIRequest apiRequest = new APIRequest(httpServletRequest);
        return addressBookService.selectAddressBookList(apiRequest);
    }

}
