package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.CommonQuestion;
import com.sftc.web.model.vo.swaggerRequestVO.commonQuestion.DeleteCommonQuestionVO;

public interface CommonQuestionService {

    /**
     * 获取所有常见问题
     */
    APIResponse getCommonQuestion();

    /**
     * CMS 获取礼品卡列表 条件查询+分页
     */
    APIResponse selectList(APIRequest apiRequest) throws Exception;

    //根据id查询常见问题
    APIResponse selectListById(APIRequest apiRequest) throws Exception;

    /**
     * CMS 添加礼品卡信息
     */
    APIResponse addCommonQuestion(CommonQuestion commonQuestion) throws Exception;

    /**
     * CMS 修改礼品卡信息
     */
    APIResponse updateCommonQuestion(CommonQuestion commonQuestion) throws Exception;

    /**
     * CMS 删除礼品卡信息
     * @throws Exception 
     */
    APIResponse deleteCommonQuestion(APIRequest apiRequest) throws Exception;
    

	APIResponse selectListPaging(APIRequest apiRequest);

	APIResponse save(APIRequest apiRequest) throws Exception;

	APIResponse deleteCommonQuestion(int id);





}
