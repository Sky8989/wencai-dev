package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;

public interface QiniuService {

    /**
     * 返回七牛云的uptoken
     */
    ApiResponse returnUptoken();

    String uploadImageWithBase64(String base64Img);
}
