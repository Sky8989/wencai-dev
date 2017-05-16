package com.sftc.web.mapper;

import com.sftc.web.model.Merchant;


public interface UserMapper {

    public Merchant selectUserByPhone(String user_phone);

    public Merchant selectUserByOpenid(String open_id);

    public void insertOpenid(String open_id);

    void addMerchant(Merchant merchant);
}
