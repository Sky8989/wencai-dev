package com.sftc.tools.api;

import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class APIResponse {

    @ApiModelProperty(name = "state",value = "状态",example = "200",dataType = "int",required = true)
    private Integer state;
    @ApiModelProperty(name = "message",value = "信息",example = "success",required = true)
    private String message;
    private Object result;
    private Object error;

    public String getMessage() {
        return message;
    }

    public Integer getState() {
        return state;
    }

    public Object getResult() {
        return result;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public APIResponse() {
        super();
    }


}
