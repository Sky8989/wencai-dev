package com.sftc.web.model.SwaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户标签")
public class UserLabelVO {
   @ApiModelProperty(value = "用户好友关系id",example = "156",required = true)
   private int user_contact_id;

    public int getUser_contact_id() {
        return user_contact_id;
    }

    public void setUser_contact_id(int user_contact_id) {
        this.user_contact_id = user_contact_id;
    }
}
