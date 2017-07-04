package com.sftc.web.service.impl;

import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.EvaluateMapper;
import com.sftc.web.model.Evaluate;
import com.sftc.web.service.EvaluateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Resource
    private EvaluateMapper evaluateMapper;

    /**
     * 通过 uuid 去获取大网订单的快递信息 在本地数据库里
     */
    public APIResponse getEvaluate(String uuid) {
        APIStatus status = APIStatus.SUCCESS;
        System.out.println("-   -uuid:"+uuid);
        List<Evaluate> evaluateList =  evaluateMapper.selectByUuid(uuid);
        if (evaluateList.size() == 0){
            return APIUtil.selectErrorResponse("该订单无评价信息",null);
        }else {
            return APIUtil.getResponse(status,evaluateList.get(0));
        }
    }
}
