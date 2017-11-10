package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.EvaluateMapper;
import com.sftc.web.model.Evaluate;
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
     * 通过 uuid 去获取大网订单的快递信息 在本地数据库里
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


    /**
     * 获取所有评价信息列表  分页+条件查询
     */
    public APIResponse selectEvaluateListByPage(APIRequest apiRequest){
        APIStatus status = APIStatus.SUCCESS;
        HttpServletRequest httpServletRequest = apiRequest.getRequest();
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        Evaluate evaluate = new Evaluate(httpServletRequest);
        // 进行分页查询
        PageHelper.startPage(pageNumKey,pageSizeKey);
        List<Evaluate> evaluateList = evaluateMapper.selectByPage(evaluate);
        if (evaluateList.size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(status, evaluateList);
        }
    }
}
