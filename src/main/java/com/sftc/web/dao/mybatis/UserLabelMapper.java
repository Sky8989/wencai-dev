package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.Label;
import com.sftc.web.model.entity.SystemLabel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLabelMapper {

    Label queryUserAllLabelByUID(@Param(value = "user_contact_id") int user_contact_id);

    void insertLabelByUCID(
            @Param(value = "user_contact_id") int user_contact_id,
            @Param(value = "system_label_ids") String system_label_ids,
            @Param(value = "custom_labels") String custom_labels
    );

    void updateLabelByUCID(
            @Param(value = "user_contact_id") int user_contact_id,
            @Param(value = "system_label_ids") String system_label_ids,
            @Param(value = "custom_labels") String custom_labels
    );

    List<SystemLabel> querySystemLabels();
}
