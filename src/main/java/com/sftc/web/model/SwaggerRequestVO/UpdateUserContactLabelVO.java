package com.sftc.web.model.SwaggerRequestVO;


import com.sftc.web.model.dto.LabelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "用户标签存储")
public class UpdateUserContactLabelVO {

    @ApiModelProperty(value = "用户好友关系id", example = "156", required = true)
    private int user_contact_id;

    @ApiModelProperty(name = "system_labels", value = "系统标签id数组", required = true)
    private List<Integer> system_labels;

    @ApiModelProperty(name = "custom_labels", value = "自定义标签", required = true)
    private List<LabelDTO> custom_labels;

    public int getUser_contact_id() {
        return user_contact_id;
    }

    public void setUser_contact_id(int user_contact_id) {
        this.user_contact_id = user_contact_id;
    }

    public List<Integer> getSystem_labels() {
        return system_labels;
    }

    public void setSystem_labels(List<Integer> system_labels) {
        this.system_labels = system_labels;
    }

    public List<LabelDTO> getCustom_labels() {
        return custom_labels;
    }

    public void setCustom_labels(List<LabelDTO> custom_labels) {
        this.custom_labels = custom_labels;
    }
}
