package com.sftc.web.controller.app;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.SystemLabel;
import com.sftc.web.model.vo.swaggerRequestVO.systemLabel.SystemLabelVo;
import com.sftc.web.service.SystemLabelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "系统标签")
@RestController
@RequestMapping(value = "systemLabel")
public class SystemLabelController {

    @Resource
    private SystemLabelService systemLabelService;

    /**
     * 查询系统标签List 并分页
     */
    @ApiOperation(value = "查询系统标签", httpMethod = "POST")
    @PostMapping(value = "/findSystemLabelList")
    public APIResponse getUserAllLabelByUCID(@RequestBody SystemLabelVo systemLabel) {
        return systemLabelService.getSystemLabelList(systemLabel);
    }
    /**
     * 新增(修改)系统标签  新增id为0  id不为0时修改
     */
    @ApiOperation(value = "新增(修改)系统标签", httpMethod = "POST")
    @RequestMapping(value = "addAndUpdate",method = RequestMethod.POST)
    public APIResponse updateUsrLabelByLID(@RequestBody SystemLabel systemLabel) {
    	if(systemLabel != null && systemLabel.getId() != 0){
    		return systemLabelService.updateSystemLabel(systemLabel);
    	}else{
    		return systemLabelService.addSystemLabel(systemLabel);
    	}
    }
    
    @ApiOperation(value = "删除常见问题", httpMethod = "DELETE")
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deleteCommonQuestion(@RequestBody int id) throws Exception {
    		return systemLabelService.deleteSystemLable(id);
    	}
}
