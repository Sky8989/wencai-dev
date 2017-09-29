package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.CommonQuestionDao;
import com.sftc.web.dao.mybatis.CommonQuestionMapper;
import com.sftc.web.model.CommonQuestion;
import com.sftc.web.service.CommonQuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service("commonQuestionService")
public class CommonQuestionServiceImpl implements CommonQuestionService {

    @Resource
    private CommonQuestionMapper commonQuestionMapper;

    @Resource
    private CommonQuestionDao commonQuestionDao;

    public APIResponse getCommonQuestion() {
        APIStatus status = APIStatus.SELECT_FAIL;
        List<CommonQuestion> commonQuestionList = commonQuestionMapper.getCommonQuestion();
        if (commonQuestionList != null) status = APIStatus.SUCCESS;
        return APIUtil.getResponse(status, commonQuestionList);
    }


    /**
     * CMS 获取常见问题列表 条件查询+分页
     */
    public APIResponse selectList(APIRequest apiRequest) throws Exception {
        APIStatus status = APIStatus.SUCCESS;
        // 此处封装了 User的构造方法
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        CommonQuestion commonQuestion = new CommonQuestion(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        PageHelper.startPage(pageNumKey, pageSizeKey);
        List<CommonQuestion> commonQuestionList = commonQuestionMapper.selectByPage(commonQuestion);
        if (commonQuestionList.size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(status, commonQuestionList);
        }
    }

    // 根据id查询常见问题
    public APIResponse selectListById(APIRequest request) throws Exception {
        HttpServletRequest httpServletRequest = request.getRequest();
        // 此处封装了 User的构造方法
        CommonQuestion commonQuestion = new CommonQuestion(httpServletRequest);
        CommonQuestion commonQuestion1 = commonQuestionMapper.selectListById(commonQuestion);
        if (commonQuestion1 == null) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(APIStatus.SUCCESS, commonQuestion1);
        }
    }

    /**
     * CMS 添加常见问题信息
     */
    public APIResponse addCommonQuestion(CommonQuestion commonQuestion) throws Exception {
        commonQuestion.setCreate_time(Long.toString(System.currentTimeMillis()));
        commonQuestionDao.save(commonQuestion);

        return APIUtil.getResponse(APIStatus.SUCCESS, commonQuestion);
    }

    /**
     * CMS 修改礼品卡信息
     */
    public APIResponse updateCommonQuestion(CommonQuestion commonQuestion) throws Exception {
        commonQuestionMapper.updateCommonQuestion(commonQuestion);
        return APIUtil.getResponse(APIStatus.SUCCESS, commonQuestion);
    }

    /**
     * CMS 删除礼品卡信息
     */
    public APIResponse deleteCommonQuestion(int id) throws Exception {
        commonQuestionMapper.deleteCommonQuestion(id);
        return APIUtil.getResponse(APIStatus.SUCCESS, id);
    }
}
