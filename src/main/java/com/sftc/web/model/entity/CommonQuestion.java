package com.sftc.web.model.entity;

import com.sftc.web.model.others.Object;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

@Entity
@Table(name = "sftc_common_question")
@ApiModel(value = "常见问题 ")
public class CommonQuestion extends Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name="主键")
    @Setter @Getter
    private int id;

    @ApiModelProperty(name="时间",hidden=true)
    @Setter @Getter
    private String create_time;

    @ApiModelProperty(name="标题",example = "问题标题",required = true)
    @Setter @Getter
    private String title;

    @ApiModelProperty(name="内容",example = "问题答案",required = true)
    @Setter @Getter
    private String content;

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
