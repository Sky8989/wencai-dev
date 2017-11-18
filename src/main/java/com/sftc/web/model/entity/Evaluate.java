package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

/**
*@Author:hxy starmoon1994
*@Description:
*@Date:16:25 2017/6/28
*/
@ApiModel(value = "订单评价信息包装类")
public class Evaluate extends Object {

    @Setter @Getter
    @ApiModelProperty(name = "id",value = "评价id",example = "64",dataType = "int")
    private int id;

    @Setter @Getter
    @ApiModelProperty(name = "merchant_comments",value = "评价内容",example = "一般，需改善")
    private String merchant_comments;// 评价内容

    @Setter @Getter
    @ApiModelProperty(name = "merchant_score",value = "评价分数",example = "3")
    private String merchant_score;  // 评价分数

    @Setter @Getter
    @ApiModelProperty(name = "merchant_tags",value = "标签的内容，逗号隔开",example = "态度不好,穿着不整齐")
    private String merchant_tags;  // 标签的内容，逗号隔开

    @Setter @Getter
    private int order_id;   // 订单id

    @Setter @Getter
    @ApiModelProperty(name = "user_id",value = "评价人的用户id",example = "10093",dataType = "long")
    private long user_id; // 评价人的用户id

    @Setter @Getter
    @ApiModelProperty(name = "orderExpress_id",value = "快递的id",example = "1",dataType = "int")
    private int orderExpress_id; // 快递的id orderExpress_id

    @Setter @Getter
    @ApiModelProperty(name = "uuid",value = "快递的uuid",example = "2c9a85895d8f0962015d90246c8c0a4f")
    private String uuid; // 快递的uuid

    @Setter @Getter
    @ApiModelProperty(name = "create_time",value = "创建时间",example = "1501365451482")
    private String create_time;// 创建时间

    public Evaluate() {
        super();
    }

    public Evaluate(HttpServletRequest request) {
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
            this.id = Integer.parseInt(request.getParameter("id"));
        }
        if (request.getParameter("uuid") != null && !"".equals(request.getParameter("uuid"))) {
            this.uuid = request.getParameter("uuid");
        }
        if (request.getParameter("user_id") != null && !"".equals(request.getParameter("user_id"))) {
            this.user_id = Integer.parseInt(request.getParameter("user_id"));
        }
    }
}
