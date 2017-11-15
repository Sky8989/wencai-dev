package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;

public class UserContactLabel extends Object {

    @Setter @Getter
    private int id;  //联系人id
    @Setter @Getter
    private int user_contact_id;

    @Setter @Getter
    private String label;  //标签

    @Setter @Getter
    private String create_time; //创建时间

    public UserContactLabel(int user_contact_id, String label, String create_time) {
        this.create_time = create_time;
        this.user_contact_id = user_contact_id;
        this.label = label;
    }

    public UserContactLabel() {}

}
