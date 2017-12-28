package com.sftc.web.controller;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.entity.Compensate;
import com.sftc.web.model.vo.swaggerRequest.CoordinateVO;
import com.sftc.web.model.vo.swaggerRequest.PriceExplainVO;
import com.sftc.web.model.vo.swaggerRequest.SFAPIRequestVO;
import com.sftc.web.model.vo.swaggerRequest.SFAccessTokenRequestVO;
import com.sftc.web.model.vo.swaggerResponse.CommonQuestionListVO;
import com.sftc.web.model.vo.swaggerResponse.RandomLocationRespVO;
import com.sftc.web.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.ws.rs.HttpMethod;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@Api(description = "设置")
@RequestMapping("index")
public class IndexController{

    @Resource
    private IndexService indexService;

    @ApiIgnore
    @ApiOperation(value = "设置顺丰专送API环境", httpMethod = "POST",notes = "设置项目对接的顺丰同城的API环境，项目部署后默认是dev环境")
    @RequestMapping(value = "/environment", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse setupEnvironment(@RequestBody SFAPIRequestVO body) throws Exception {
        return indexService.setupEnvironment(body);
    }

    @ApiIgnore
    @ApiOperation(value = "设置顺丰专送公共token", httpMethod = "POST")
    @RequestMapping(value = "/common/token", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse setupCommonToken(@RequestBody SFAccessTokenRequestVO body) throws Exception {
        return indexService.setupCommonToken(body);
    }

    @ApiOperation(value = "获取城市列表", httpMethod = HttpMethod.GET)
    @RequestMapping(value = "/cities")
    @ResponseBody
    public ApiResponse getCityExpressList() {
        return indexService.getCityExpressList();
    }

    @ApiOperation(value = "根据城市获取服务电话", httpMethod = "POST")
    @RequestMapping(value = "/hotline")
    public @ResponseBody
    ApiResponse getServicePhoneByCity(@RequestBody PriceExplainVO body) throws Exception {
        return indexService.getServicePhoneByCity(body);
    }

    @ApiOperation(value = "根据城市获取价格说明", httpMethod = "POST")
    @RequestMapping(value = "/price_desc")
    public @ResponseBody
    ApiResponse getPriceExplainByCity(@RequestBody PriceExplainVO body) throws Exception {
        return indexService.getPriceExplainByCity(body);
    }

    @ApiOperation(value = "获取所有常见问题", httpMethod = "GET",response = CommonQuestionListVO.class)
    @RequestMapping(value = "/questions",method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse getCommonQuestion() throws Exception {
        return indexService.getCommonQuestion();
    }

    @ApiOperation(value = "获取赔偿规则说明", httpMethod = "GET",response = Compensate.class)
    @RequestMapping(value = "/compensate",method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse getCompensate() throws Exception {
        return indexService.getCompensate();
    }

    @ApiOperation(value = "获取所有礼品卡",httpMethod = "GET")
    @RequestMapping(value = "/cards",method = RequestMethod.GET)
    public @ResponseBody
    ApiResponse getGiftCardList() throws Exception {
        return indexService.getGiftCardList();
    }

    @ApiOperation(value = "获取随机经纬度",httpMethod = "POST",notes = "提供功能：更改随机获取经纬度所使用的常量，控制生成数据的数量和范围值\n" +
            "注意事项：原则上最大数量的值要大于最小数量要，范围值的单位是千米",response = RandomLocationRespVO.class)
    @RequestMapping(value = "/coordinates", method = RequestMethod.POST)
    public @ResponseBody
    ApiResponse getRandomPoint(@RequestBody CoordinateVO body) throws Exception {
        return indexService.getLatitudeLongitude(body);
    }

}
