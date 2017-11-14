package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 *
 * @version 1.0
 * @Package com.sftc.ssm.pojo
 * @Description: 意见反馈类
 * @date 17/4/1
 * @Time 下午9:00
 */
public class OpinionFeedback extends Object {

    @Setter @Getter
    private int id;

    @Setter @Getter
    private String create_time;// 创建时间

    @Setter @Getter
    private String feedback;// 意见反馈

    @Setter @Getter
    private String gmt_create;// 反馈时间

    @Setter @Getter
    private User merchant; // 反馈意见的用户

    @Setter @Getter
    private long user_id;

    public OpinionFeedback() {super();}
}
