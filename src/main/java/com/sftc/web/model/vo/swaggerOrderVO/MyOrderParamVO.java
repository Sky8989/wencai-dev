package com.sftc.web.model.vo.swaggerOrderVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "我的订单列表请求包装类")
public class MyOrderParamVO {

    @ApiModelProperty(name = "id",value = "用户id",example = "10093",hidden = true)
    private int id;
    @ApiModelProperty(name = "token",value = "用户access_token",example = "EyMivbd44I124lcddrBG")
    private String token;
    @ApiModelProperty(name = "pageNum",value = "第几页",example = "1")
    private int pageNum;
    @ApiModelProperty(name = "pageSize",value = "每页几条",example = "10")
    private int pageSize;
    @ApiModelProperty(name = "keyword",value = "关键字")
    private String keyword;
    @ApiModelProperty(name = "keyword_state",value = "关键字状态")
    private String keyword_state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getKeyword_state() {return keyword_state;}

    public void setKeyword_state(String keyword_state) {this.keyword_state = keyword_state;}
}
