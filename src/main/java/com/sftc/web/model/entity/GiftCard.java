package com.sftc.web.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import com.sftc.web.model.others.Object;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
@Entity
@ApiModel(value = "礼品卡(id为0时新增礼品卡，id不为0时修改礼品卡)")
@Table(name = "sftc_gift_card")
public class GiftCard extends Object {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "主键")
    @Setter @Getter
    private int id;

    @ApiModelProperty(value = "创建时间",hidden=true)
    @Setter @Getter
    private String create_time;

    @ApiModelProperty(value = "名称",example = "礼品卡名称")
    @Setter @Getter
    private String name;

    @ApiModelProperty(value = "图片",example = "暂存 上传图片时的Base64值")
    @Setter @Getter
    private String icon;

    @ApiModelProperty(value = "类型",example = "礼品卡 类型名")
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
