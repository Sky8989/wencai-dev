package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.DateRemind;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateRemindMapper {

    //添加日期提醒
    void addDateRemind(DateRemind dateRemind);

    //获取好友日期提醒列表
    List<DateRemind> selectDateRemindList(int user_contact_id);

    //删除日期提醒
    void deleteDateRemind(int id);
}
