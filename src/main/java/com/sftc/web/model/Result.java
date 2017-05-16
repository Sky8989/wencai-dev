package com.sftc.web.model;

/**
 * Created by Administrator on 2017/5/16.
 */
public class Result {

    private Error error;
    private Merchant merchant;
    private Token token;


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
