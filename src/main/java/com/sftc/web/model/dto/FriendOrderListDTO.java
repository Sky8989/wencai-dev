package com.sftc.web.model.dto;

import java.util.List;

public class FriendOrderListDTO {
    private String id;
    private String sender_avatar;
    private String sender_name;
    private int sender_user_id;
    private String region_type;
    private String object_type; // 物品类型
    private String word_message;// 文字寄语
    private String image;       // 包裹图片
    private String create_time;
    private boolean is_gift;
    private boolean is_evaluated;
    private String pay_method;//支付类型


    private List<OrderFriendCallbackExpress> expressList;

    public class OrderFriendCallbackExpress {
        private int id;
        private int user_contact_id;
        private String ship_name;
        private String ship_avatar;
        private int ship_user_id;
        private String uuid;
        private String state;
        private String attributes;
        //为C端小程序的物品类型，
        private String package_type;
        // 包裹的类型
        private String object_type;
        //当物品类型在packageType罗列的类型之外时填写
        private String package_comments;
        private String reserve_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShip_name() {
            return ship_name;
        }

        public void setShip_name(String ship_name) {
            this.ship_name = ship_name;
        }

        public String getShip_avatar() {
            return ship_avatar;
        }

        public void setShip_avatar(String ship_avatar) {
            this.ship_avatar = ship_avatar;
        }

        public int getShip_user_id() {
            return ship_user_id;
        }

        public void setShip_user_id(int ship_user_id) {
            this.ship_user_id = ship_user_id;
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

        public String getAttributes() {
            return attributes;
        }

        public void setAttributes(String attributes) {
            this.attributes = attributes;
        }

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

        public int getUser_contact_id() {return user_contact_id;}

        public void setUser_contact_id(int user_contact_id) {this.user_contact_id = user_contact_id;}
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_avatar() {
        return sender_avatar;
    }

    public void setSender_avatar(String sender_avatar) {
        this.sender_avatar = sender_avatar;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public int getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(int sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public String getRegion_type() {
        return region_type;
    }

    public void setRegion_type(String region_type) {
        this.region_type = region_type;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public String getWord_message() {
        return word_message;
    }

    public void setWord_message(String word_message) {
        this.word_message = word_message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public boolean isIs_gift() {
        return is_gift;
    }

    public void setIs_gift(boolean is_gift) {
        this.is_gift = is_gift;
    }

    public boolean isIs_evaluated() {
        return is_evaluated;
    }

    public void setIs_evaluated(boolean is_evaluated) {
        this.is_evaluated = is_evaluated;
    }

    public List<OrderFriendCallbackExpress> getExpressList() {
        return expressList;
    }

    public void setExpressList(List<OrderFriendCallbackExpress> expressList) {
        this.expressList = expressList;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }
}
