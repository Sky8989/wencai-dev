package com.sftc.web.service.impl;

import com.sftc.tools.api.ApiRequest;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiStatus;
import com.sftc.tools.api.ApiUtil;
import com.sftc.web.dao.mybatis.EvaluateMapper;
import com.sftc.web.model.entity.Evaluate;
import com.sftc.web.service.EvaluateService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Resource
    private EvaluateMapper evaluateMapper;

    /**
     * 通过 uuid 去获取订单的快递信息 在本地数据库里
     */
    @Override
    public ApiResponse getEvaluate(ApiRequest apiRequest) {
        String uuid = (String) apiRequest.getParameter("uuid");
        ApiStatus status = ApiStatus.SUCCESS;
        List<Evaluate> evaluateList = evaluateMapper.selectByUuid(uuid);
        if (evaluateList.size() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "该订单无评价信息");
        } else {
            return ApiUtil.getResponse(status, evaluateList.get(0));
        }
    }
}
