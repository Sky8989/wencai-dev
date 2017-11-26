package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.EvaluateMapper;
import com.sftc.web.model.entity.Evaluate;
import com.sftc.web.service.EvaluateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Resource
    private EvaluateMapper evaluateMapper;

    /**
     * 通过 uuid 去获取订单的快递信息 在本地数据库里
     */
    public APIResponse getEvaluate(APIRequest apiRequest) {
        String uuid = (String) apiRequest.getParameter("uuid");
        APIStatus status = APIStatus.SUCCESS;
        List<Evaluate> evaluateList =  evaluateMapper.selectByUuid(uuid);
        if (evaluateList.size() == 0){
            return APIUtil.selectErrorResponse("该订单无评价信息",null);
        }else {
            return APIUtil.getResponse(status,evaluateList.get(0));
        }
    }
}
