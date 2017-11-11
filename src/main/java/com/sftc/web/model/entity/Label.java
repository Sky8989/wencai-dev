package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel(value = "用户标签")
@Entity
@Table(name = "sftc_user_contact_label")
public class Label implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "用户标签id")
    private int id;

    @ApiModelProperty(value = "系统标签id", example = "标签1|标签2|标签3|")
    private String system_label_ids;

    @ApiModelProperty(value = "自定义标签", example = " [{\"name\" : \"1234\",\"is_selected\" : true}]")
    private String custom_labels;

    @Id
    @ApiModelProperty(value = "好友联系表id", example = "156")
    private int user_contact_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSystem_label_ids() {
        return system_label_ids;
    }

    public void setSystem_label_ids(String system_label_ids) {
        this.system_label_ids = system_label_ids;
    }

    public String getCustom_labels() {
        return custom_labels;
    }

    public void setCustom_labels(String custom_labels) {
        this.custom_labels = custom_labels;
    }

    public int getUser_contact_id() {
        return user_contact_id;
    }

    public void setUser_contact_id(int user_contact_id) {
        this.user_contact_id = user_contact_id;
    }
}
