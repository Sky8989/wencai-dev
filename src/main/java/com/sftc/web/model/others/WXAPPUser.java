package com.sftc.web.model.others;


import lombok.Getter;
import lombok.Setter;

/**
 * 微信登录返回Json数据模型
 *
 * @author bingo
 */
public class WXAPPUser {

    @Setter @Getter
    private String openid;

    @Setter @Getter
    private String access_token;

    @Setter @Getter
    private String refresh_token;

    @Setter @Getter
    private String scope;

    @Setter @Getter
    private String expires_in;

    @Setter @Getter
    private String unionid;

    @Setter @Getter
    private int errcode;

    @Setter @Getter
    private String errmsg;
}
