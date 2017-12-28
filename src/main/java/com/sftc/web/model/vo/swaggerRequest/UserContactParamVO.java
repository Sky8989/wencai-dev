package com.sftc.web.model.vo.swaggerRequest;

import com.sftc.web.model.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "来往记录请求类")
public class UserContactParamVO extends BaseVO {

    @ApiModelProperty(name = "access_token",value = "顺丰access_token",required = true,hidden = true)
    private String access_token;
    @ApiModelProperty(name = "user_uuid",value = "用户uuid",example = "10093",required = true,hidden = true)
    private String user_uuid;
    @ApiModelProperty(name = "friend_uuid",value = "好友uuid",example = "2c9a85895fddad58015fdde9f78d005a",required = true)
    private String friend_uuid;
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

    public String getUser_uuid() {return user_uuid;}

    public void setUser_uuid(String user_uuid) {
        this.user_uuid = user_uuid;
    }

    public String getFriend_uuid() {
        return friend_uuid;
    }

    public void setFriend_uuid(String friend_uuid) {
        this.friend_uuid = friend_uuid;
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
