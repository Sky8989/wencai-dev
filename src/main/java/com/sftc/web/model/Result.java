package com.sftc.web.model;

import com.sftc.tools.api.APIStatus;
import com.sftc.web.model.quotes.Request;
import net.sf.json.JSONArray;

import java.util.List;
import java.util.Map;

public class Result {

    private Error error;

    private User merchant;

    private Token token;
    private Object message;
    private Request request;
    private List<String> errors;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }


    public User getMerchant() {
        return merchant;
    }

    public void setMerchant(User merchant) {
        this.merchant = merchant;


    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public APIStatus validateMessage(){
        APIStatus status=null;
        JSONArray jsonObject = JSONArray.fromObject(this.errors);
        Map m =  (Map)jsonObject.get(0);
        if (m.get("type").equals("VALIDATION_ERROR")){
            status = APIStatus.VALIDATION_ERROR;
        }
        return status;
    }
}

