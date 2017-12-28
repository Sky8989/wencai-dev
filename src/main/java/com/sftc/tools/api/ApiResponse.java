package com.sftc.tools.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Administrator
 */
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private Integer state;
    @ApiModelProperty(name = "message", value = "信息", example = "success", required = true)
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

    public ApiResponse() {
        super();
    }


}
