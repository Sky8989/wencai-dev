package com.sftc.web.model.vo.swaggerRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value = "来往记录请求类")
public class UserContactParamVO {

    @ApiModelProperty(name = "access_token",value = "顺丰access_token",required = true,hidden = true)
    private String access_token;
    @ApiModelProperty(name = "user_id",value = "用户id",example = "10093",required = true,hidden = true)
    private int user_id;
    @ApiModelProperty(name = "friend_id",value = "好友id",example = "10085",required = true)
    private int friend_id;
    @ApiModelProperty(name = "pageNum",value = "页码",example = "1",required = true)
    private int pageNum;
    @ApiModelProperty(name = "pageSize",value = "每页条数",example = "10",required = true)
    private int pageSize;
    @ApiModelProperty(name = "pageSize",value = "订单id")
    private String order_id;

    public String getOrder_id() {return order_id;}

    public void setOrder_id(String order_id) {this.order_id = order_id;}

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
