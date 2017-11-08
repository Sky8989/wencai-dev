package com.sftc.web.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

@Entity
@Table(name = "sftc_common_question")
@ApiModel(value = "常见问题")
public class CommonQuestion extends Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    private int id;

    @ApiModelProperty("创建时间")
    private String create_time;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommonQuestion() {
    }

    /**
     * 基于HttpServletRequest作为参数的构造方法 用于cms
     * 后期便于应用扩展工厂模式 将此参数抽出
     */
    public CommonQuestion(HttpServletRequest request) {
        if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))) {
            this.id = Integer.parseInt(request.getParameter("id"));
        }
        if (request.getParameter("title") != null && !"".equals(request.getParameter("title"))) {
            this.title = request.getParameter("title");
        }
    }
}
