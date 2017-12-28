package com.sftc.web.service;

import com.sftc.tools.api.ApiResponse;
import com.sftc.web.model.vo.swaggerRequest.CoordinateVO;
import com.sftc.web.model.vo.swaggerRequest.PriceExplainVO;
import com.sftc.web.model.vo.swaggerRequest.SFAPIRequestVO;
import com.sftc.web.model.vo.swaggerRequest.SFAccessTokenRequestVO;

/**
 * 
 * @author Administrator
 *
 */
public interface IndexService {

    /**
     * 设置环境
     */
    ApiResponse setupEnvironment(SFAPIRequestVO body);

    /**
     * 设置公共token
     */
    ApiResponse setupCommonToken(SFAccessTokenRequestVO body);

    /**
     * 根据城市获取顺丰服务电话
     */
    ApiResponse getServicePhoneByCity(PriceExplainVO body);

    /**
     * 获取所有常见问题
     */
    ApiResponse getCommonQuestion();

    /**
     * 获取赔偿规则说明
     */
    ApiResponse getCompensate();

    ApiResponse getPriceExplainByCity(PriceExplainVO body);

    /**
     * 获取所有礼品卡
     * @return
     */
    ApiResponse getGiftCardList();

    ApiResponse getCityExpressList();

    /**
     * 获取随机经纬度
     * @param body
     * @return
     */
    ApiResponse getLatitudeLongitude(CoordinateVO body);

}
