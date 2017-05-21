package com.sftc.web.model.sfmodel;;

import com.sftc.web.model.User;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */
public class Request {
    private String region;
    private Source source;
    private Target target;
    private User merchant;
    private Attributes attributes;
    private Quote quote;
    private List<Package> packages;
    private String product_type;
    private String pay_type;
    public String getRegion() {
        return region;
    }

    public User getMerchant() {
        return merchant;
    }

    public void setMerchant(User merchant) {
        this.merchant = merchant;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }



    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }
}
