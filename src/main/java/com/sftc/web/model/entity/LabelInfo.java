package com.sftc.web.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;


@Component
@ApiModel(value = "用户标签")
public class LabelInfo {
    @ApiModelProperty(value = "用户标签id")
    private String label_id;
    @ApiModelProperty(value = "标签内容", example = "标签1|标签2|标签3|")
    private String[] labels;

    public String getLabel_id() {
        return label_id;
    }

    public void setLabel_id(String label_id) {
        this.label_id = label_id;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }
}
