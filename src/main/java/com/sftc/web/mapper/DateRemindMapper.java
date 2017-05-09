package com.sftc.web.mapper;

import com.sftc.web.model.DateRemind;

import java.util.List;

public interface DateRemindMapper {
    //添加日期提醒
    void addDateRemind(DateRemind dateRemind);
    //获取日期提醒列表
    List<DateRemind> getDateRemindList(DateRemind dateRemind);
    //删除日期提醒
    void deleteDateRemind(int id);
}
