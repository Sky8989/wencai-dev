package com.sftc.web.model.sfmodel;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.model.sfmodel
 * @Description:
 * @date 2017/5/27
 * @Time 上午1:24
 */
public class Orders {

    private String uuid;
    private String status;
    private boolean payed;
    //错误信息
    private String attributes;
    // private String INIT = "下单";
    // private String PAYING = "支付中";
    // private String WAIT_HAND_OVER = "待揽件";
    // private String DELIVERING = "派送中";
    // private String FINISHED = "已完成";
    // private String ABNORMAL = "不正常的";
    // private String CANCELED = "取消单";
    // private String WAIT_REFUND = "等待退款";
    // private String REFUNDING = "退款中";
    // private String REFUNDED = "已退款";
    //
    // public String getINIT() {
    //     return INIT;
    // }
    //
    // public String getPAYING() {
    //     return PAYING;
    // }
    //
    // public String getWAIT_HAND_OVER() {
    //     return WAIT_HAND_OVER;
    // }
    //
    // public String getDELIVERING() {
    //     return DELIVERING;
    // }
    //
    // public String getFINISHED() {
    //     return FINISHED;
    // }
    //
    // public String getABNORMAL() {
    //     return ABNORMAL;
    // }
    //
    // public String getCANCELED() {
    //     return CANCELED;
    // }
    //
    // public String getWAIT_REFUND() {
    //     return WAIT_REFUND;
    // }
    //
    // public String getREFUNDING() {
    //     return REFUNDING;
    // }
    //
    // public String getREFUNDED() {
    //     return REFUNDED;
    // }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
