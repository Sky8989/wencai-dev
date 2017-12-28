package com.sftc.web.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sftc.web.model.others.Object;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "c_user_contact_label")
public class UserContactLabel extends Object {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter @Getter
    private int id;  //联系人id
    @Setter @Getter
    private int user_contact_id;

    @Setter @Getter
    private String custom_labels;  //标签
    
    @Setter @Getter
    private String system_label_ids;  //系统标签

    @Setter @Getter
    private String create_time; //创建时间
    @Setter @Getter
    private String update_time; //修改时间

    public UserContactLabel(int user_contact_id, String label, String create_time) {
        this.create_time = create_time;
        this.user_contact_id = user_contact_id;
        this.custom_labels = label;
    }

    public UserContactLabel() {}

}
