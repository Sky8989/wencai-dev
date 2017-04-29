package com.sftc.web.model;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model
 * @Description: 快递员类
 * @date 2017/4/25
 * @Time 上午10:32
 */
public class Courier {

    private int id;
    // 姓名
    private String name;
    // 电话
    private String phone;
    // 评分
    private double score;
    // 送单数
    private int sender_count;
    // 评价数
    private int evaluate_count;
    // 快递员编号
    private String courier_number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getSender_count() {
        return sender_count;
    }

    public void setSender_count(int sender_count) {
        this.sender_count = sender_count;
    }

    public int getEvaluate_count() {
        return evaluate_count;
    }

    public void setEvaluate_count(int evaluate_count) {
        this.evaluate_count = evaluate_count;
    }

    public String getCourier_number() {
        return courier_number;
    }

    public void setCourier_number(String courier_number) {
        this.courier_number = courier_number;
    }
}
