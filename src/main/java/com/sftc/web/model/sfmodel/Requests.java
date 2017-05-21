package com.sftc.web.model.sfmodel;


import com.sftc.web.model.*;
import com.sftc.web.model.Error;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2017/5/19.
 */
public class Requests {
    private JSONObject jsonObject;
    private Object object;
    private Order order;
    private OrderExpress orderExpress;
    private int id;
    private Token token;
    private com.sftc.web.model.Error error;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public com.sftc.web.model.Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderExpress getOrderExpress() {
        return orderExpress;
    }

    public void setOrderExpress(OrderExpress orderExpress) {
        this.orderExpress = orderExpress;
    }



    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }



    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
