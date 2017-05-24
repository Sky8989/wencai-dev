package com.sftc.web.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model
 * @Description: 常见问题类
 * @date 2017/4/25
 * @Time 上午10:46
 */
public class CommonQuestion extends Object {

    private int id;

    // 创建时间
    private String create_time;
    // 标题
    private String title;
    // 内容
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
}
