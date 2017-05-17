package com.sftc.web.model;

import com.sftc.tools.api.APIStatus;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2017/5/16.
 */
public class Error {
    private String type;
    private String reason;
    private String message;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public APIStatus validate(){
        APIStatus status=null;
        if (this.type.equals("SMS_VERIFY_CODE_ERROR")){
           status = APIStatus.REGISTER_FAIL;
        }else {
            status = APIStatus.REGISTER_SUCCESS;
        }
        return status;
    }
    public APIStatus validateToken(){
        APIStatus status=null;
        if(this.type.equals("ERROR")){
            status = APIStatus.ERROR;
        }else {
            status = APIStatus.TOKEN_SUCCESS;
        }
        return status;
    }
    public APIStatus login(){
        APIStatus status=null;
        if(this.type.equals("AUTHORIZATION_FAIL")){
            status = APIStatus.AUTHORIZATION_FAIL;
        }if(this.type.equals("LOGIN_ERROR")){
            status = APIStatus.LOGIN_ERROR;
        }
        return status;
    }
}
