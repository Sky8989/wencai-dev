package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.AddressBookDeleteVO;
import com.sftc.web.model.vo.swaggerRequest.AddressBookRequestVO;
import com.sftc.web.model.vo.swaggerRequest.AddressBookUpdateVO;
import com.sftc.web.model.vo.swaggerResponse.*;
import com.sftc.web.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@Api(description = "地址簿相关接口")
@RequestMapping("addressBook")
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    @ApiOperation(value = "添加地址簿",httpMethod = "POST",response = AddAddressBookRespVO.class)
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse addAddress(@RequestBody AddressBookRequestVO body) throws Exception {
        return addressBookService.addAddressBook(body);
    }

    @ApiOperation(value = "删除地址簿",httpMethod = "DELETE",notes = "软删除",response = DeleteAddressBookRespVO.class)
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    ApiResponse deleteAddress(@RequestBody AddressBookDeleteVO body) throws Exception {
        return addressBookService.deleteAddressBook(body);
    }

    @ApiOperation(value = "修改地址簿",httpMethod = "PUT",response = ResponseMessageVO.class)
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody
    ApiResponse updateAddress(@RequestBody AddressBookUpdateVO body) throws Exception {
        return addressBookService.updateAddressBook(body);
    }


    @ApiOperation(value = "地址簿查找",httpMethod = "GET",response = GetAddressBookRespVO.class)
    @ApiImplicitParam(name = "address_book_type",value = "地址簿类型 sender/ship",required = true,paramType = "query",defaultValue = "sender")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse updateAddress(@RequestParam(value="address_book_type") String addressBookType) throws Exception {
        return addressBookService.selectAddressBookList(addressBookType);
    }

    @ApiOperation(value = "查询历史地址",httpMethod = "GET",response = AddressHistoryListVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页码",paramType = "query",dataType ="Integer" ,defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "每页数量",paramType = "query",dataType ="Integer" ,defaultValue = "10"),
    })
    @RequestMapping(value="/history",method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse selectAddressHistory(@RequestParam Integer pageNum, @RequestParam Integer pageSize) throws Exception {
        return addressBookService.selectAddressHistory(pageNum,pageSize);
    }

}
