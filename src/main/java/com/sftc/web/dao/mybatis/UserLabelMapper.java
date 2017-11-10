package com.sftc.web.dao.mybatis;

import com.sftc.web.model.chen.Label;
import com.sftc.web.model.entity.SystemLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserLabelMapper {

    Label queryUserAlllabelByUID(@Param(value = "user_contact_id") int user_contact_id);

    int updateLabelByID(@Param(value = "label_id") int label_id,@Param(value = "system_label_ids") String system_label_ids, @Param(value = "custom_labels") String custom_labels);

    void insertLabelByid(@Param(value = "user_contact_id") int user_contact_id, @Param(value = "system_labels")  String system_labels, @Param(value = "custom_labels") String custom_labels);

    List<SystemLabel> querySystemLable();
}
