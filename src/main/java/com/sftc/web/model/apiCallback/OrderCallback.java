package com.sftc.web.model.apiCallback;

import net.sf.json.JSONObject;

import java.util.List;

public class OrderCallback {

    private int id;
    private String sender_name;
    private String sender_addr;
    private String order_type;
    private String region_type;
    private boolean is_gift;
    private boolean is_evaluated;
    private String pay_method;
    private String order_number; // 单包裹的时候，从express里提出来

    private List<OrderCallbackExpress> expressList;

    public class OrderCallbackExpress {
        private String ship_name;
        private String ship_addr;
        private String uuid;
        private String state;
        private String attributes;
        private String order_number;
        //为C端小程序的物品类型，
        private String package_type;
        // 包裹的类型
        private String object_type;
        //当物品类型在packageType罗列的类型之外时填写
        private String package_comments;
        private String reserve_time;

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

        public String getAttributes() {return attributes;}

        public void setAttributes(String attributes) {this.attributes = attributes;}

        public String getPackage_type() {return package_type;}

        public void setPackage_type(String package_type) {this.package_type = package_type;}

        public String getObject_type() {return object_type;}

        public void setObject_type(String object_type) {this.object_type = object_type;}

        public String getPackage_comments() {return package_comments;}

        public void setPackage_comments(String package_comments) {this.package_comments = package_comments;}

        public String getReserve_time() {
            return reserve_time;
        }

        public void setReserve_time(String reserve_time) {
            this.reserve_time = reserve_time;
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

    public boolean isIs_evaluated() {
        return is_evaluated;
    }

    public void setIs_evaluated(boolean is_evaluated) {
        this.is_evaluated = is_evaluated;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
}
