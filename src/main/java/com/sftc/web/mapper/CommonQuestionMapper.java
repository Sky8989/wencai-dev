package com.sftc.web.mapper;

import com.sftc.web.model.CommonQuestion;

import java.util.List;

public interface CommonQuestionMapper {

    //常见问题列表
    List<CommonQuestion> getCommonQuestion();
}
