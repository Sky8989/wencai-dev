package com.sftc.web.mapper;

import com.sftc.web.model.Label;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */
public interface LabelMapper {
    //添加标签
    void addLabel(Label label);
    //获取好友标签列表
    List<Label> getFriendLabelList(int user_contact_id);
    //删除好友标签
    void deleteFriendLabel(int id);
}
