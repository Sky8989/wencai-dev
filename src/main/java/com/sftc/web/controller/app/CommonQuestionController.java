package com.sftc.web.controller.app;

import javax.annotation.Resource;

import com.sftc.web.model.vo.swaggerResponse.CommonQuestionListVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.CommonQuestion;
import com.sftc.web.model.vo.swaggerRequest.CommonQuestionVO;
import com.sftc.web.model.vo.swaggerRequest.DeleteCommonQuestionVO;
import com.sftc.web.service.CommonQuestionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(description = "常见问题相关接口")
@RequestMapping("question")
public class CommonQuestionController {

	@Resource
	private CommonQuestionService commonQuestionService;

	@ApiOperation(value = "获取所有常见问题", httpMethod = "GET",response = CommonQuestionListVO.class)
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody APIResponse getCommonQuestion() throws Exception {
		return commonQuestionService.getCommonQuestion();
	}
}
