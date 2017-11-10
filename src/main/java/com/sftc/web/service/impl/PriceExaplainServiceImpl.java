package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.PriceExaplainMapper;
import com.sftc.web.model.SwaggerRequestVO.PriceExaplainVO;
import com.sftc.web.model.entity.PriceExplain;
import com.sftc.web.service.PriceExaplainService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.sftc.tools.api.APIStatus.PARAM_ERROR;
import static com.sftc.tools.api.APIStatus.SUCCESS;


@Service
public class PriceExaplainServiceImpl implements PriceExaplainService {
    @Autowired
    private PriceExaplainMapper priceExaplainMapper;
    @Autowired
    private PriceExplain priceExaplain;
    /**
     * 根据城市获取相对应的价格说明
     * @param apiRequest
     */
    @Override
    public APIResponse getPriceExplainByCity(APIRequest apiRequest) {
        PriceExaplainVO priceExaplainVO = (PriceExaplainVO)apiRequest.getRequestParam();
        if (priceExaplainVO==null)
            return APIUtil.paramErrorResponse("城市为空");

        String city = priceExaplainVO.getCity();

        PriceExplain priceExaplainInfo = priceExaplainMapper.queryPriceExplainByCirty(city);
        priceExaplain.setCity(city);
        if (priceExaplainInfo==null){
            priceExaplain.setDistance_price("");
            priceExaplain.setWeight_price("");
        }else{
            priceExaplain.setDistance_price(priceExaplainInfo.getDistance_price());
            priceExaplain.setWeight_price(priceExaplainInfo.getWeight_price());
        }
        return APIUtil.getResponse(SUCCESS, priceExaplain);

    }

}
