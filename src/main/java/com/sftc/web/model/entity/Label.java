package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
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
    @Setter @Getter
    private int id;

    @ApiModelProperty(value = "系统标签id", example = "标签1|标签2|标签3|")
    @Setter @Getter
    private String system_label_ids;

    @ApiModelProperty(value = "自定义标签", example = " [{\"name\" : \"1234\",\"is_selected\" : true}]")
    @Setter @Getter
    private String custom_labels;

    @Id
    @ApiModelProperty(value = "好友联系表id", example = "156")
    @Setter @Getter
    private int user_contact_id;
}
