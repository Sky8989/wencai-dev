package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerRequest.AddressHistoryDeleteVO;
import com.sftc.web.model.vo.swaggerResponse.AddressHistoryListVO;
import com.sftc.web.model.vo.swaggerResponse.DeleteAddressBookRespVO;
import com.sftc.web.service.AddressHistoryService;
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
@Api(description = "历史地址相关文档")
@RequestMapping("address/history")
public class AddressHistoryController {

    @Resource
    private AddressHistoryService addressHistoryService;

    @ApiOperation(value = "查询历史地址",httpMethod = "GET",response = AddressHistoryListVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页码",paramType = "query",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "每页数量",paramType = "query",defaultValue = "10"),
    })
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse selectAddressHistory(HttpServletRequest request) throws Exception {
        return addressHistoryService.selectAddressHistory(new APIRequest(request));
    }

    @ApiOperation(value = "删除历史地址",httpMethod = "DELETE",notes = "删除的是地址簿中类型为address_history的地址簿 软删除",response = DeleteAddressBookRespVO.class)
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deleteAddress(@RequestBody AddressHistoryDeleteVO addressHistoryDeleteVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(addressHistoryDeleteVO);
        return addressHistoryService.deleteAddressHistory(request);
    }
}
