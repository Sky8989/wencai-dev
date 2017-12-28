package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "c_order_cancel")
public class OrderCancel extends Object {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private int id;

    @Setter
    @Getter
    private String create_time; // 创建时间

    @Setter
    @Getter
    private String reason; // 取消原因

    @Setter
    @Getter
    private String question_describe; // 问题描述

    @Setter
    @Getter
    private int express_id; // 快递id

    public OrderCancel() {
    }

}
