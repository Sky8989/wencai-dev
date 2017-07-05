package com.sftc.web.service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/6/20.
 */

public interface EvaluateService {
    public APIResponse getEvaluate(String uuid);
//CMS  分页查询 评价信息
    public APIResponse selectEvaluateListByPage(APIRequest apiRequest);
}
