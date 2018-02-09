package com.sftc.web.controller.cms;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.CommonQuestion;
import com.sftc.web.service.CommonQuestionService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by huxingyue on 2017/7/11.
 * 在CMS中对常见问题进行操作的controller
 */
@RequestMapping("cms/commonQuestion")
@Controller
public class CMSCommonQuestionController {

    @Resource
    private CommonQuestionService commonQuestionService;

    /**
     * 获取所有常见问题信息列表  分页+条件查询
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse giftCardList(HttpServletRequest httpServletRequest) throws Exception {
        return commonQuestionService.selectList(new APIRequest(httpServletRequest));
    }

    //根据id查询常见问题
    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse selectListById(HttpServletRequest httpServletRequest) throws Exception {
        return commonQuestionService.selectListById(new APIRequest(httpServletRequest));
    }

    /**
     * 添加常见问题信息
     *
     * @param commonQuestion
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse addGiftCard(@RequestBody CommonQuestion commonQuestion) throws Exception {
        return commonQuestionService.addCommonQuestion(commonQuestion);
    }

    /**
     * 修改常见问题信息
     *
     * @param commonQuestion
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse updateCommonQuestion(@RequestBody CommonQuestion commonQuestion) throws Exception {
        return commonQuestionService.updateCommonQuestion(commonQuestion);
    }

    /**
     * 删除常见问题信息
     *
     * @param object
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse deleteGiftCard(@RequestBody Object object) throws Exception {
        int id = JSONObject.fromObject(object).containsKey("id") ? JSONObject.fromObject(object).getInt("id") : 0;
        return commonQuestionService.deleteCommonQuestion(id);
    }
}
