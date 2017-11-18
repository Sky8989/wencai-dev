package com.sftc.web.controller.app;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.vo.swaggerResponse.CommonQuestionListVO;
import com.sftc.web.service.CommonQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Api(description = "常见问题相关接口")
@RequestMapping("question")
public class CommonQuestionController {

    @Resource
    private CommonQuestionService commonQuestionService;

    @ApiOperation(value = "常见问题", httpMethod = "GET",response = CommonQuestionListVO.class)
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 401,message = "The query fails"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getCommonQuestion() throws Exception {
        return commonQuestionService.getCommonQuestion();
    }
}
