package com.sftc.web.model.reqeustParam;

public class MyOrderParam {

    private int id;
    private String token;
    private int pageNum;
    private int pageSize;
    private String keyword;
    //模糊查询的state关键字
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
