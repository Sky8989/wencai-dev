package com.sftc.web.model.SwaggerRequestVO;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户标签存储")
public class UpdateUsrLabelVO {
    @ApiModelProperty(name = "label_id", value = "标签id", required = true,example = "8")
    private int label_id;
    @ApiModelProperty(name = "system_labels", value = "系统标签数组", required = true,example = "[\"系统标签5\",\"2837193\",\"879789\"]")
    private String system_labels;
    @ApiModelProperty(name = "custom_labels", value = "自定义标签json数组", required = true,example = "[{\"name\":\"1234456\",\"is_selected\":\"true\"}]")
    private String custom_labels;
    @ApiModelProperty(name = "type", value = "类型", required = true,example = "0")
    private int type;

    public int getLabel_id() {
        return label_id;
    }

    public void setLabel_id(int label_id) {
        this.label_id = label_id;
    }

    public String getSystem_labels() {
        return system_labels;
    }

    public void setSystem_labels(String system_labels) {
        this.system_labels = system_labels;
    }

    public String getCustom_labels() {
        return custom_labels;
    }

    public void setCustom_labels(String custom_labels) {
        this.custom_labels = custom_labels;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
