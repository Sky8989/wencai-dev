package com.sftc.web.model.vo.swaggerRequestVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by xf on 2017/10/23.
 */
@ApiModel(value = "好友标星请求包装类")
public class FriendStarVO {
    @ApiModelProperty(name = "user_id",value = "用户id",example = "10093",required = true,hidden = true)
    private String user_id;
    @ApiModelProperty(name = "friend_id",value = "好友id",example = "10028",required = true)
    private String friend_id;
    @ApiModelProperty(name = "is_star",value = "标星",example = "1",required = true)
    private String is_star;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getIs_star() {
        return is_star;
    }

    public void setIs_star(String is_star) {
        this.is_star = is_star;
    }
}
