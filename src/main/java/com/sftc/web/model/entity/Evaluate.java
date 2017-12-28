package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;

/**
*@Author:hxy starmoon1994
*@Description:
*@Date:16:25 2017/6/28
*/
public class Evaluate extends Object {

    @Setter @Getter
    private int id;

    @Setter @Getter
    private String merchant_comments;// 评价内容

    @Setter @Getter
    private String merchant_score;  // 评价分数

    @Setter @Getter
    private String merchant_tags;  // 标签的内容，逗号隔开

    @Setter @Getter
    private int order_id;   // 订单id

    @Setter @Getter
    private String user_uuid; // 评价人的用户id

    @Setter @Getter
    private int orderExpress_id; // 快递的id orderExpress_id

    @Setter @Getter
    private String uuid; // 快递的uuid

    @Setter @Getter
    private String create_time;// 创建时间

    public Evaluate() {
        super();
    }

}
