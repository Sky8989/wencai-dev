package com.sftc.web.mapper;

import com.sftc.web.model.Message;

import java.util.List;

/**
 * Created by huxingyue on 2017/6/27.
 * 对 消息通知表 的操作
 */
public interface MessageMapper {
    //    <!-- 新建 收到快递信息/收到好友地址 通知消息-->
    void insertMessage(Message message);

    // <!-- 二次新建通知信息时，对原有信息进行更新，更新 express_id 和is_read 字段-->
    void updateMessageReceiveExpress(Message message);

    // <!-- 二次新建通知信息时，对原有信息进行更新，更新 express_id 和is_read 字段-->
    void updateMessageReceiveAddress(Message message);

    //    <!-- 简单查找 收到快递信息 通知消息 by userid -->
    List<Message> selectMessageReceiveExpress(int userId);

    //    <!-- 简单查找 收到好友地址 通知消息 by userid -->
    List<Message> selectMessageReceiveAddress(int userId);
}
