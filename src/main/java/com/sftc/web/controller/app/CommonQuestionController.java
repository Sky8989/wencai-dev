package com.sftc.web.controller.app;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.CommonQuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("question")
public class CommonQuestionController {

    @Resource
    private CommonQuestionService commonQuestionService;

    @RequestMapping(value = "/common", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getCommonQuestion() throws Exception {
        return commonQuestionService.getCommonQuestion();
    }
}
