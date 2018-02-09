package com.sftc.web.controller.app;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.model.vo.swaggerRequest.DeleteSystemLabelVo;
import com.sftc.web.model.vo.swaggerRequest.SystemLabelVo;
import com.sftc.web.service.SystemLabelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "CMS系统标签")
@RestController
@RequestMapping(value = "systemLabel")
public class SystemLabelController {

	@Resource
	private SystemLabelService systemLabelService;

	/**
	 * 查询系统标签List 并分页
	 */
	@ApiOperation(value = "CMS查询系统标签", httpMethod = "POST")
	@PostMapping(value = "/list")
	public APIResponse getUserAllLabelByUCID(@RequestBody SystemLabelVo systemLabel) {
		  APIRequest apiRequest = new APIRequest();
	        apiRequest.setRequestParam(systemLabel);
		return systemLabelService.getSystemLabelList(apiRequest);
	}

	/**
	 * 新增(修改)系统标签 新增id为0 id不为0时修改
	 */
	@ApiOperation(value = "CMS保存系统标签 新增id为0 id不为0修改", httpMethod = "POST",notes = "新增不用传id，修改时传id")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public APIResponse save(@RequestBody SystemLabel systemLabel) {
		 APIRequest apiRequest = new APIRequest();
	        apiRequest.setRequestParam(systemLabel);
			return systemLabelService.save(apiRequest);
	}

	@ApiOperation(value = "CMS删除系统标签", httpMethod = "DELETE")
	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody APIResponse deleteCommonQuestion(@RequestBody DeleteSystemLabelVo systemLabel) throws Exception {
		APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(systemLabel);
		return systemLabelService.deleteSystemLable(apiRequest);
	}
}
