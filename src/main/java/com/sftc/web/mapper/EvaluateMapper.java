package com.sftc.web.mapper;

import com.sftc.web.model.Evaluate;

import java.util.List;


public interface EvaluateMapper {
    // 添加 订单评价信息
    void addEvaluate(Evaluate evaluate);
    // 查找 评价信息 by订单id
    List<Evaluate> selectByUuid(String uuid);
    //     <!--下面是cms系统用到的mapper-->
    List<Evaluate> selectByPage(Evaluate evaluate);
}
