package com.sftc.web.model.SwaggerRequestVO;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户标签存储")
public class UpdateUsrLabelVO {
    @ApiModelProperty(name = "label_id", value = "标签id", required = true,example = "8")
    private int label_id;
    @ApiModelProperty(name = "labels", value = "标签数组", required = true,example = "[\"系统标签5\",\"2837193\",\"879789\"]")
    private String labels;
    @ApiModelProperty(name = "type", value = "类型", required = true,example = "0")
    private int type;

    public int getLabel_id() {
        return label_id;
    }

    public void setLabel_id(int label_id) {
        this.label_id = label_id;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
