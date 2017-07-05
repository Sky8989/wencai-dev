package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.EvaluateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by huxingyue on 2017/7/4.
 */
@Controller
@RequestMapping("cms/evaluate")
public class CMSEvaluateController {

    @Resource
    EvaluateService evaluateService;
    /**
     * 获取所有评价信息列表  分页+条件查询
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse evaluateList(HttpServletRequest request) throws Exception{
        return evaluateService.selectEvaluateListByPage(request);
    }
}
