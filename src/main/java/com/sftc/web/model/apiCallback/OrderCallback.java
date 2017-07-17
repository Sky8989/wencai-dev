package com.sftc.web.model.apiCallback;

import java.util.List;

public class OrderCallback {

    private int id;
    private String sender_name;
    private String sender_addr;
    private String order_type;
    private String region_type;
    private boolean is_gift;
    private boolean is_evaluated;
    private String order_number; // 单包裹的时候，从express里提出来

    private List<OrderCallbackExpress> expressList;

    public class OrderCallbackExpress {
        private String ship_name;
        private String ship_addr;
        private String uuid;
        private String state;
        private String order_number;

        public String getShip_name() {
            return ship_name;
        }

        public void setShip_name(String ship_name) {
            this.ship_name = ship_name;
        }

        public String getShip_addr() {
            return ship_addr;
        }

        public void setShip_addr(String ship_addr) {
            this.ship_addr = ship_addr;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getOrder_number() {
            return order_number;
        }

        public void setOrder_number(String order_number) {
            this.order_number = order_number;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_addr() {
        return sender_addr;
    }

    public void setSender_addr(String sender_addr) {
        this.sender_addr = sender_addr;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getRegion_type() {
        return region_type;
    }

    public void setRegion_type(String region_type) {
        this.region_type = region_type;
    }

    public boolean isIs_gift() {
        return is_gift;
    }

    public void setIs_gift(boolean is_gift) {
        this.is_gift = is_gift;
    }

    public List<OrderCallbackExpress> getExpressList() {
        return expressList;
    }

    public void setExpressList(List<OrderCallbackExpress> expressList) {
        this.expressList = expressList;
    }

    public boolean getIs_evaluated() {
        return is_evaluated;
    }

    public void setIs_evaluated(boolean is_evaluated) {
        this.is_evaluated = is_evaluated;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
}
