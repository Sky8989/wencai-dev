package com.sftc.web.mapper;

import com.sftc.web.model.CommonQuestion;

import java.util.List;

public interface CommonQuestionMapper {

    /**
     * 常见问题列表
     */
    List<CommonQuestion> getCommonQuestion();

    /**
     * CMS 查找常用问题  分页加条件加模糊
     */
    List<CommonQuestion> selectByPage(CommonQuestion commonQuestion);

    //根据id查询常见问题
    CommonQuestion selectListById(CommonQuestion commonQuestion);

    /**
     * CMS 系统 添加常用问题
     */
    void insertCommonQuestion(CommonQuestion commonQuestion);

    /**
     * CMS 系统 修改常用问题
     */
    void updateCommonQuestion(CommonQuestion commonQuestion);

    /**
     * CMS 系统 删除常用问题
     */
    void deleteCommonQuestion(int id);
}
