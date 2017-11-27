package com.sftc.web.model.entity;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 兜底记录表，记录从同城单转下到大网的订单信息
 */
@Entity
@Table(name = "sftc_order_no_driver")
public class OrderExpressTransform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 快递id
     */
    private int express_id;

    /**
     * 同城uuid（转换前）
     */
    private String same_uuid;

    /**
     * 是否已读
     */
    private int is_read;

    /**
     * 创建时间
     */
    private String create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExpress_id() {
        return express_id;
    }

    public void setExpress_id(int express_id) {
        this.express_id = express_id;
    }

    public String getSame_uuid() {
        return same_uuid;
    }

    public void setSame_uuid(String same_uuid) {
        this.same_uuid = same_uuid;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public OrderExpressTransform() {
    }
}
