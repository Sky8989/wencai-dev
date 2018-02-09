package com.sftc.web.model.vo.swaggerRequest;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(value = "好友列表请求类")
public class FriendListVO {
    @ApiModelProperty(name = "user_id",value = "用户id",example = "10028",required = true,hidden = true)
    private int user_id;
    @ApiModelProperty(name = "pageNum",value = "页数",example = "1",required = true)
    private int pageNum;
    @ApiModelProperty(name = "pageSize",value = "每页数量",example = "10",required = true)
    private int pageSize;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {this.user_id = user_id;}

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
