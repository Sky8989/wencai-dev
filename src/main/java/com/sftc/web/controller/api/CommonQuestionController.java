package com.sftc.web.controller.api;

import com.sftc.tools.api.APIResponse;
import com.sftc.web.service.CommonQuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description: 常见问题接口
 * @date 2017/5/22
 * @Time 下午8:50
 */
@Controller
@RequestMapping("question")
public class CommonQuestionController {

    @Resource
    private CommonQuestionService commonQuestionService;

    @RequestMapping(value = "/common", method = RequestMethod.GET, headers = "api-version=1")
    public @ResponseBody
    APIResponse getCommonQuestion() throws Exception {
        return commonQuestionService.getCommonQuestion();
    }
}
