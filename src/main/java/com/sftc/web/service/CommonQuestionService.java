package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.model.CommonQuestion;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description: 常见问题操作接口
 * @date 2017/4/25
 * @Time 上午10:58
 */
public interface CommonQuestionService {

    /**
     * 获取所有常见问题
     *
     * @return
     */
    APIResponse getCommonQuestion();

    /**
     * CMS 系统 获取礼品卡列表 条件查询+分页
     *
     * @param apiRequest
     * @return
     * @throws Exception
     */
    APIResponse selectList(APIRequest apiRequest) throws Exception;

    //根据id查询常见问题
    APIResponse selectListById(APIRequest apiRequest) throws Exception;

    /**
     * CMS 系统 添加礼品卡信息
     *
     * @param commonQuestion
     * @return
     * @throws Exception
     */
    APIResponse addCommonQuestion(CommonQuestion commonQuestion) throws Exception;

    /**
     * CMS 系统 修改礼品卡信息
     *
     * @param commonQuestion
     * @return
     * @throws Exception
     */
    APIResponse updateCommonQuestion(CommonQuestion commonQuestion) throws Exception;

    /**
     * CMS 系统 删除礼品卡信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    APIResponse deleteCommonQuestion(int id) throws Exception;
}
