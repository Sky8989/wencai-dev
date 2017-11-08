package com.sftc.tools.common;

import com.sftc.tools.api.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 控制层的相关工具
 * Created by bingo on 2017/8/9.
 */
public class ControllerHelper {

    /**
     * 对APIResponse进行封装 使httpcode一一对应
     *
     * @param apiResponse 统一返回值
     * @return ResponseEntity
     */
    public static ResponseEntity<APIResponse> responseEntityBuilder(APIResponse apiResponse) {

        switch (apiResponse.getState()) {
            case 200:
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            case 500:
                return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            case 400:
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            case 401:
                return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
            case 402:
                return new ResponseEntity<>(apiResponse, HttpStatus.PAYMENT_REQUIRED);
            default:
                return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
