package com.sftc.web.service;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description: 七牛文件上传操作接口
 * @date 2017/4/19
 * @Time 下午3:36
 */
public interface QiniuService {

    /**
     * 返回七牛云的uptoken
     * @return
     */
    Map<String, String> returnUptoken();
}
