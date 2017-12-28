package com.sftc.web.controller;


import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.web.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseController {

    @Resource
    protected QiniuService qiniuService;

    @Resource
    protected MessageService messageService;

    @Resource
    protected UserContactService userContactService;


    /**
     * 校验字符
     */
    protected ApiResponse validRequestParams(BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            List<String> lists = new ArrayList<>();
            for (ObjectError objectError : allErrors) {
                lists.add(objectError.getDefaultMessage());
            }
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(),"parameter empty",lists);
        }
        return null;
    }


}
