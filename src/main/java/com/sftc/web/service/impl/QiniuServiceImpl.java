package com.sftc.web.service.impl;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.sftc.web.service.QiniuService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.sftc.tools.constant.ThirdPartyConstant.QN_ACCESSKEY;
import static com.sftc.tools.constant.ThirdPartyConstant.QN_BUCKET;
import static com.sftc.tools.constant.ThirdPartyConstant.QN_SECRETKEY;

@Service
public class QiniuServiceImpl implements QiniuService {

    public Map<String, String> returnUptoken() {
        Map<String, String> map = new HashMap<String, String>();
        Configuration configuration = new Configuration(Zone.zone2());
        UploadManager manager = new UploadManager(configuration);
        Auth auth = Auth.create(QN_ACCESSKEY, QN_SECRETKEY);
        String upToken = auth.uploadToken(QN_BUCKET);
        map.put("uptoken", upToken);
        return map;
    }
}
