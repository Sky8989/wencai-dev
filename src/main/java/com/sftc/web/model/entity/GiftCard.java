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
    @ApiModelProperty("主键")
    @Setter @Getter
    private int id;

    @ApiModelProperty("创建时间")
    @Setter @Getter
    private String create_time;

    @ApiModelProperty("名称")
    @Setter @Getter
    private String name;

    @ApiModelProperty("图片")
    @Setter @Getter
    private String icon;

    @ApiModelProperty("类型")
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
