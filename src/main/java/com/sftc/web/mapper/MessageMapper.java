package com.sftc.web.mapper;

import com.sftc.web.model.Message;

import java.util.List;

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

    // <!-- 更新 消息is_read状态-->
    void updateIsRead(int id);

    /**
     * 查询未读消息
     */
    List<Message> selectUnReadMessageList(int user_id);
}
