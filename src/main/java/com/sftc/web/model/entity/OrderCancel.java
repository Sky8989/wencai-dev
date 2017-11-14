package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 取消订单类
 * @date 17/4/1
 * @Time 下午9:00
 */
@Entity
@Table(name = "sftc_order_cancel")
public class OrderCancel extends Object {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter @Getter
    private int id;

    @Setter @Getter
    private String create_time; // 创建时间

    @Setter @Getter
    private String reason;// 取消原因

    @Setter @Getter
    private String question_describe;// 问题描述

    @Setter @Getter
    private String order_id;

    public OrderCancel() {}

    public OrderCancel(HttpServletRequest request) {
        if (request.getParameter("order_id") != null && !"".equals(request.getParameter("order_id")))
        {this.order_id = request.getParameter("order_id");}
    }

}
