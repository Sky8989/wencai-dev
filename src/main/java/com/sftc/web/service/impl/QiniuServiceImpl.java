package com.sftc.web.service.impl;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.sftc.web.service.QiniuService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service.impl
 * @Description: 七牛文件上传操作接口实现
 * @date 2017/4/19
 * @Time 下午3:37
 */
@Service
public class QiniuServiceImpl implements QiniuService {

    public Map<String, String> returnUptoken() {
        Map<String, String> map = new HashMap<String, String>();
        Configuration configuration = new Configuration(Zone.zone2());
        UploadManager manager = new UploadManager(configuration);
        String accessKey = "Jyi6Ntprm38nI6n1heGjwXyQmzie8ZjY7l9Cq_Je";
        String secretKey = "eBqe82USZuB6gnqvjTsyg_qki6C6HwnkFIvNIH-y";
        String bucket = "sftc";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        map.put("uptoken", upToken);
        return map;
    }
}
