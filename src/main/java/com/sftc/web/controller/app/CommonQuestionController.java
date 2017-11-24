package com.sftc.web.controller.app;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.CommonQuestion;
import com.sftc.web.model.vo.swaggerRequest.commonQuestion.CommonQuestionVo;
import com.sftc.web.service.CommonQuestionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(description = "常见问题相关接口")
@RequestMapping("question")
public class CommonQuestionController {

	@Resource
	private CommonQuestionService commonQuestionService;

	@ApiOperation(value = "常见问题", httpMethod = "GET")
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody APIResponse getCommonQuestion() throws Exception {
		return commonQuestionService.getCommonQuestion();
	}

	@ApiOperation(value = "新增(修改)常见问题", httpMethod = "POST",notes = "新增不用传id,修改时传id")
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody APIResponse addCommonQuestion(@RequestBody CommonQuestion commonQuestion) throws Exception {
				return commonQuestionService.save(commonQuestion); 

	}

	@ApiOperation(value = "删除常见问题", httpMethod = "DELETE")
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody APIResponse deleteCommonQuestion(@RequestBody int id) throws Exception {
		return commonQuestionService.deleteCommonQuestion(id);
	}

	@ApiOperation(value = "查询常见问题List", httpMethod = "POST")
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public @ResponseBody APIResponse findCommonQuestion(@RequestBody CommonQuestionVo commonQuestionVo)
			throws Exception {
		APIRequest apiRequest = new APIRequest();
		apiRequest.setRequestParam(commonQuestionVo);
		return commonQuestionService.selectListPaging(apiRequest);
	}

}
