package com.sftc.web.model.sfdo;

/**
 * Created by Administrator on 2017/5/22.
 */
public class Package {
    private String type;
    private Integer item_amount;
    private String comments;
    private Double weight;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(Integer item_amount) {
        this.item_amount = item_amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
