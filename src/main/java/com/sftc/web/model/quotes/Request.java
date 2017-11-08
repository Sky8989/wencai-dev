package com.sftc.web.model.quotes;

import java.util.List;

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
