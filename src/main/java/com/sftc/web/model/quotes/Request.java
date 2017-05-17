package com.sftc.web.model.quotes;

import com.sftc.web.model.Order;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */
public class Request {
    private Source source;
    private List<Package> packages;
    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }
}
