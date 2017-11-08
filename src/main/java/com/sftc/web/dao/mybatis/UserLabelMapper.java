package com.sftc.web.dao.mybatis;

import com.sftc.web.model.chen.Label;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserLabelMapper {

    Label queryUserAlllabelByUID(@Param(value = "user_contact_id") int user_contact_id);

    int updateLabelByID(@Param(value = "label_id") int label_id, @Param(value = "label") String label);

    void insertLabelByid(@Param(value = "user_contact_id") int user_contact_id, @Param(value = "label")  String label);
}
