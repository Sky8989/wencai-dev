package com.sftc.web.mapper;

import com.sftc.web.model.Evaluate;

import java.util.List;

/**
*@Author:hxy starmoon1994
*@Description:
*@Date:14:08 2017/6/28
*/
public interface EvaluateMapper {
    // 添加 订单评价信息
    void addEvaluate(Evaluate evaluate);
    // 查找 评价信息 by订单id
    List selectByOrderId(int order_id);
}
