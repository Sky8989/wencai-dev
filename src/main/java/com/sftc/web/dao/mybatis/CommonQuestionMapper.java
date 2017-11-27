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
}
