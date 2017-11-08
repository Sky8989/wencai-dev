package com.sftc.web.model.chen;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 用户标签
 * Created by CatalpaFlat on 2017/11/6.
 */
public class Label implements Serializable{
    @ApiModelProperty(value = "用户标签id")
    private String id;
    @ApiModelProperty(value = "标签内容",example = "标签1|标签2|标签3|")
    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
