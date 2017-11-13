package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerOrderVO.SFServiceRequestVO;
import com.sftc.web.service.SFServiceAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "时效计价")
@RequestMapping("sf/service")
public class SFServiceAddressController {

    @Resource
    private SFServiceAddressService sfServiceAddressService;

    @ApiOperation(value = "更新顺丰速运服务地址",httpMethod = "PUT")
    @RequestMapping(value = "/address", method = RequestMethod.PUT)
    public @ResponseBody
    APIResponse updateSFServiceAddress(HttpServletRequest request) throws Exception {
        return sfServiceAddressService.updateServiceAddress(new APIRequest(request));
    }

    @ApiOperation(value = "顺丰运费时效查询",httpMethod = "POST",notes = "顺丰运费时效查询，用于【顺丰次日】，【顺丰次晨】，【顺丰即日】的动态获取。\n" +
            "接口的使用请参考：http://www.sf-express.com/cn/sc/dynamic_functions/price/\n" +
            "接口返回的数组元素不一定都是三个，当地区不支持某种配送方式时，则返回的数组中不会出现相应的对象。（返回即支持）\n" +
            "如果返回的数据中“closedTime”这个字段有值时，说明当前已超过该配送方式的最晚收件时间，此时应该变灰或不显示。（支持但超时），为空字符串时可正常收件。")
    @RequestMapping(value = "/rate", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse selectRate(@RequestBody SFServiceRequestVO sfServiceRequestVO) throws Exception {
        APIRequest request = new APIRequest();
        request.setRequestParam(sfServiceRequestVO);
        return sfServiceAddressService.selectDynamicPrice(request);
    }

    @ApiOperation(value = "订单运费时效查询",httpMethod = "GET",notes = "订单运费时效查询，用于【顺丰次日】，【顺丰次晨】，【顺丰即日】的动态获取。\n" +
            "接口的使用请参考：http://www.sf-express.com/cn/sc/dynamic_functions/price/\n" +
            "接口暂时只能用于单包裹的订单进行时效运费查询，多包裹的情况目前不清楚估算规则。\n" +
            "接口返回的数组元素不一定都是三个，当地区不支持某种配送方式时，则返回的数组中不会出现相应的对象。（返回即支持）\n" +
            "如果返回的数据中“closedTime”这个字段有值时，说明当前已超过该配送方式的最晚收件时间，此时应该变灰或不显示。（支持但超时），为空字符串时可正常收件。")
    @RequestMapping(value = "/order/rate", method = RequestMethod.GET)
    @ApiImplicitParam(name = "order_id",value = "订单id",required = true,paramType = "query",defaultValue = "C150943940840398")
    public @ResponseBody
    APIResponse selectOrderRate(HttpServletRequest request) throws Exception {
        return sfServiceAddressService.selectOrderDynamicPrice(new APIRequest(request));
    }
}
