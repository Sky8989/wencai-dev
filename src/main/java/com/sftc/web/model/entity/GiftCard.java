package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model
 * @Description: 礼品卡类
 * @date 2017/4/25
 * @Time 上午10:45
 */
@ApiModel(value = "礼品卡")
public class GiftCard extends Object {
    @ApiModelProperty(value = "主键",example = "17",dataType = "int")
    @Setter @Getter
    private int id;

    @ApiModelProperty(value = "创建时间",example = "1497797226111")
    @Setter @Getter
    private String create_time;

    @ApiModelProperty(value = "名称",example = "祝我们合作愉快")
    @Setter @Getter
    private String name;

    @ApiModelProperty(value = "图片",example = "https://sf.dankal.cn/card14.png")
    @Setter @Getter
    private String icon;

    @ApiModelProperty(value = "类型",example = "节目必备")
    @Setter @Getter
    private String type;

    public GiftCard() {
    }

    public GiftCard(HttpServletRequest request) {
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
            this.id = Integer.parseInt(request.getParameter("id"));
        }
        if (request.getParameter("name") != null && !"".equals(request.getParameter("name"))) {
            this.name = request.getParameter("name");
        }
        if (request.getParameter("icon") != null && !"".equals(request.getParameter("icon"))) {
            this.icon = request.getParameter("icon");
        }
        if (request.getParameter("type") != null && !"".equals(request.getParameter("type"))) {
            this.type = request.getParameter("type");
        }
    }
}
