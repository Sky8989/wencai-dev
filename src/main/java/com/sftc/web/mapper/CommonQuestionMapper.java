package com.sftc.web.mapper;

import com.sftc.web.model.CommonQuestion;

import java.util.List;

public interface CommonQuestionMapper {

    //常见问题列表
    List<CommonQuestion> getCommonQuestion();

    /**
     * CMS 查找常用问题  分页加条件加模糊
     *
     * @param commonQuestion
     * @return
     */
    List<CommonQuestion> selectByPage(CommonQuestion commonQuestion);

    /**
     * CMS 系统 添加常用问题
     *
     * @param commonQuestion
     */
    void insertCommonQuestion(CommonQuestion commonQuestion);

    /**
     * CMS 系统 修改常用问题
     *
     * @param commonQuestion
     */
    void updateCommonQuestion(CommonQuestion commonQuestion);

    /**
     * CMS 系统 删除常用问题
     *
     * @param id
     */
    void deleteCommonQuestion(int id);
}
