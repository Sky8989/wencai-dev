package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequest.AddressBookRequestVO;
import com.sftc.web.model.vo.swaggerRequest.AddressBookUpdateVO;
import com.sftc.web.model.vo.swaggerRequest.AddressBookDeleteVO;
import com.sftc.web.model.vo.swaggerResponse.AddAddressBookRespVO;
import com.sftc.web.model.vo.swaggerResponse.DeleteAddressBookRespVO;
import com.sftc.web.service.AddressBookService;
import io.swagger.annotations.*;
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

    @ApiOperation(value = "添加地址簿",httpMethod = "POST",response = AddAddressBookRespVO.class)
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addAddress(@RequestBody AddressBookRequestVO addressBookRequestVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(addressBookRequestVO);
        return addressBookService.addAddressBook(apiRequest);
    }

    @ApiOperation(value = "删除地址簿",httpMethod = "DELETE",notes = "软删除",response = DeleteAddressBookRespVO.class)
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deleteAddress(@RequestBody AddressBookDeleteVO addressBookDeleteVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(addressBookDeleteVO);
        return addressBookService.deleteAddressBook(apiRequest);
    }

    @ApiOperation(value = "修改地址簿",httpMethod = "PUT")
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse updateAddress(@RequestBody AddressBookUpdateVO addressBookUpdateVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(addressBookUpdateVO);
        return addressBookService.updateAddressBook(apiRequest);
    }


    @ApiOperation(value = "地址簿查找",httpMethod = "GET")
    @ApiImplicitParam(name = "address_book_type",value = "地址簿类型 sender/ship",required = true,paramType = "query",defaultValue = "sender")
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse updateAddress(HttpServletRequest httpServletRequest) throws Exception {
        APIRequest apiRequest = new APIRequest(httpServletRequest);
        return addressBookService.selectAddressBookList(apiRequest);
    }

}
