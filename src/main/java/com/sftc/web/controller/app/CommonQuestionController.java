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
import com.sftc.web.model.vo.swaggerRequestVO.commonQuestion.CommonQuestionVO;
import com.sftc.web.model.vo.swaggerRequestVO.commonQuestion.DeleteCommonQuestionVO;
import com.sftc.web.service.CommonQuestionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(description = "CMS常见问题相关接口")
@RequestMapping("question")
public class CommonQuestionController {

	@Resource
	private CommonQuestionService commonQuestionService;

	@ApiOperation(value = "CMS查询所有常见问题", httpMethod = "GET")
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody APIResponse getCommonQuestion() throws Exception {
		return commonQuestionService.getCommonQuestion();
	}

	@ApiOperation(value = "CMS保存常见问题 id为0新增，id不为0修改", httpMethod = "POST")
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody APIResponse save(@RequestBody CommonQuestion commonQuestion) throws Exception {
		  APIRequest apiRequest = new APIRequest();
	        apiRequest.setRequestParam(commonQuestion);
		return commonQuestionService.save(apiRequest); 

	}

	@ApiOperation(value = "CMS删除常见问题", httpMethod = "DELETE")
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody APIResponse deleteCommonQuestion(@RequestBody DeleteCommonQuestionVO commonQuestion) throws Exception {
		APIRequest apiRequest = new APIRequest();
		apiRequest.setRequestParam(commonQuestion);
		return commonQuestionService.deleteCommonQuestion(apiRequest);
	}

	@ApiOperation(value = "CMS指定查询常见问题 并分页", httpMethod = "POST")
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public @ResponseBody APIResponse findCommonQuestion(@RequestBody CommonQuestionVO commonQuestionVo)
			throws Exception {
		APIRequest apiRequest = new APIRequest();
		apiRequest.setRequestParam(commonQuestionVo);
		return commonQuestionService.selectListPaging(apiRequest);
	}

}
