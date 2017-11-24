package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.CityExpressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;

/**
 * 同城专送控制层
 *
 * @author ： CatalpaFlat
 * @date ：Create in 10:18 2017/11/24
 */
@RestController
@RequestMapping(value = "cityExpress")
@Api(description = "同城专送")
public class CityExpressController {
    @Resource
    private CityExpressService cityExpressService;

    @ApiOperation(value = "获取同城专送列表", httpMethod = HttpMethod.GET)
    @GetMapping(value = "getCitys")
    public APIResponse getCityExpressList(HttpServletRequest request) {
        return cityExpressService.getCityExpressList(new APIRequest(request));
    }

}
