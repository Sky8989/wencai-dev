package com.sftc.web.model.vo.swaggerRequest;


import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "好友列表请求类")
public class FriendListVO extends BaseVO {
    @ApiModelProperty(name = "user_uuid",value = "用户uuid",example = "10028",required = true,hidden = true)
    private String user_uuid;
    @ApiModelProperty(name = "pageNum",value = "页数",example = "1",required = true)
    private int pageNum;
    @ApiModelProperty(name = "pageSize",value = "每页数量",example = "10",required = true)
    private int pageSize;

    public String getUser_uuid() {
        return user_uuid;
    }

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
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
