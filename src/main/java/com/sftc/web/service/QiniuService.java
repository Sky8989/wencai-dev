package com.sftc.web.service;

import java.util.Map;

public interface QiniuService {

    /**
     * 返回七牛云的uptoken
     */
    Map<String, String> returnUptoken();

    String uploadImageWithBase64(String base64Img);
}
