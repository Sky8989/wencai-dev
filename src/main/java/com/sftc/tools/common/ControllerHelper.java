package com.sftc.tools.common;

import com.sftc.tools.api.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * controller层的相关工具
 * Created by huxingyue on 2017/8/9.
 */
public class ControllerHelper {
    /**
     * 对APIResponse进行封装 使httpcode一一对应
     *
     * @param apiResponse 统一返回值
     * @return  ResponseEntity
     */
    public static ResponseEntity<APIResponse> responseEntityBuilder(APIResponse apiResponse) {
        ResponseEntity<APIResponse> responseEntity;
        if (apiResponse.getState() == 200) {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
            return responseEntity;
        } else if (apiResponse.getState() == 500) {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        } else if (apiResponse.getState() == 400) {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.BAD_REQUEST);
            return responseEntity;
        } else if (apiResponse.getState() == 401) {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.UNAUTHORIZED);
            return responseEntity;
        } else if (apiResponse.getState() == 402) {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.PAYMENT_REQUIRED);
            return responseEntity;
        } else {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
            return responseEntity;
        }
    }

}
