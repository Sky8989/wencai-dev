package com.sftc.web.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class LabelDTO {

    @Getter
    @Setter
    @ApiModelProperty(name = "name", value = "标签名", required = true)
    private String name;

    @Getter
    @Setter
    @ApiModelProperty(name = "selected", value = "是否选中", required = true)
    private boolean selected;
}
