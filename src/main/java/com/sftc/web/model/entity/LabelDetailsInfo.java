package com.sftc.web.model.entity;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by CatalpaFlat on 2017/11/6.
 */
@Component
public class LabelDetailsInfo {
    private String[] sysLabels;
    private List<String> userLabels;
    private List<String> userSyslabels;

    public String[] getSysLabels() {
        return sysLabels;
    }

    public void setSysLabels(String[] sysLabels) {
        this.sysLabels = sysLabels;
    }

    public List<String> getUserLabels() {
        return userLabels;
    }

    public void setUserLabels(List<String> userLabels) {
        this.userLabels = userLabels;
    }

    public List<String> getUserSyslabels() {
        return userSyslabels;
    }

    public void setUserSyslabels(List<String> userSyslabels) {
        this.userSyslabels = userSyslabels;
    }
}
