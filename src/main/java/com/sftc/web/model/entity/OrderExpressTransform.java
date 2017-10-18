package com.sftc.web.model.entity;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 兜底记录表，记录从同城单转下到大网的订单信息
 */
@Entity
@Table(name = "sftc_order_express_transform")
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
     * 大网uuid（转换后）
     */
    private String nation_uuid;

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

    public String getNation_uuid() {
        return nation_uuid;
    }

    public void setNation_uuid(String nation_uuid) {
        this.nation_uuid = nation_uuid;
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

    public OrderExpressTransform(HttpServletRequest request) {

        if (request.getParameter("express_id") != null && !"".equals(request.getParameter("express_id"))) {
            this.express_id = Integer.parseInt(request.getParameter("express_id"));
        }
        if (request.getParameter("same_uuid") != null && !"".equals(request.getParameter("same_uuid"))) {
            this.same_uuid = request.getParameter("same_uuid");
        }
        if (request.getParameter("nation_uuid") != null && !"".equals(request.getParameter("nation_uuid"))) {
            this.nation_uuid = request.getParameter("nation_uuid");
        }

    }


}
