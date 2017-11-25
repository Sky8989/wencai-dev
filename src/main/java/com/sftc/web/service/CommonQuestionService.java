package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.entity.CommonQuestion;

public interface CommonQuestionService {

    /**
     * 获取所有常见问题
     */
    APIResponse getCommonQuestion();

    /**
     * CMS 获取常见问题列表 条件查询+分页
     */
    APIResponse selectList(APIRequest apiRequest) throws Exception;

    /**
     * 根据id查询常见问题
     * @param apiRequest
     * @return
     * @throws Exception
     */
    APIResponse selectListById(APIRequest apiRequest) throws Exception;

    /**
     * CMS 添加常见问题
     */
    APIResponse addCommonQuestion(CommonQuestion commonQuestion) throws Exception;

    /**
     * CMS 修改常见问题
     */
    APIResponse updateCommonQuestion(CommonQuestion commonQuestion) throws Exception;

    /**
     * CMS 删除常见问题
     * @throws Exception 
     */
    APIResponse deleteCommonQuestion(APIRequest apiRequest) throws Exception;
    

	APIResponse selectListPaging(APIRequest apiRequest);

	APIResponse save(APIRequest apiRequest) throws Exception;

	APIResponse deleteCommonQuestion(int id);





}
