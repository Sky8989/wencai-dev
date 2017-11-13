package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.Evaluate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluateMapper {

    // 添加 订单评价信息
    void addEvaluate(Evaluate evaluate);

    // 查找 评价信息 by订单id
    List<Evaluate> selectByUuid(@Param("uuid") String uuid);

    // 下面是cms系统用到的mapper
    List<Evaluate> selectByPage(Evaluate evaluate);
}
