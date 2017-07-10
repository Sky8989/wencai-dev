package com.sftc.tools.api;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class APIResponse {

    private String state;
    private String message;
    private Object result;

    public String getMessage() {
        return message;
    }

    public String getState() {
        return state;
    }

    public Object getResult() {
        return result;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    private static class APIResponseHolder {
        private final static APIResponse INSTANCE = new APIResponse();
    }

    public static APIResponse getInstance() {
        return APIResponseHolder.INSTANCE;
    }

//    private APIResponse() {}

    public APIResponse() {
        super();
    }
}
