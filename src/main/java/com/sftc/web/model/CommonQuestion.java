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
public class CommonQuestion {

    private int id;
    // 等级规则
    private String level_rule;
    // 如何修改订单
    private String how_update_order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel_rule() {
        return level_rule;
    }

    public void setLevel_rule(String level_rule) {
        this.level_rule = level_rule;
    }

    public String getHow_update_order() {
        return how_update_order;
    }

    public void setHow_update_order(String how_update_order) {
        this.how_update_order = how_update_order;
    }
}
