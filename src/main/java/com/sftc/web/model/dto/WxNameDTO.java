package com.sftc.web.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class WxNameDTO {

    @Getter
    @Setter
    private String sender_wx_name;

    @Getter
    @Setter
    private String ship_wx_name;
}
