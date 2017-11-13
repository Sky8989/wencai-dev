package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.CommonQuestion;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
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
