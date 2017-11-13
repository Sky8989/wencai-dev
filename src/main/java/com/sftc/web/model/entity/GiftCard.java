package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
    private int id;
    @ApiModelProperty("创建时间")
    private String create_time;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("图片")
    private String icon;
    @ApiModelProperty("类型")
    private String type;

    public GiftCard() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
