package com.sftc.web.service.impl;

import static com.sftc.tools.api.APIStatus.SUCCESS;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.jpa.PriceExplainDao;
import com.sftc.web.dao.mybatis.PriceExaplainMapper;
import com.sftc.web.model.entity.PriceExplain;
import com.sftc.web.model.vo.swaggerRequest.PriceExaplainVO;
import com.sftc.web.service.PriceExaplainService;


@Service("priceExaplainService")
public class PriceExaplainServiceImpl implements PriceExaplainService {
    @Resource
    private PriceExaplainMapper priceExaplainMapper;
    @Resource
    private PriceExplainDao priceExplainDao;
    /**
     * 根据城市获取相对应的价格说明
     */
    @Override
    public APIResponse getPriceExplainByCity(APIRequest apiRequest) {
        PriceExaplainVO priceExaplainVO = (PriceExaplainVO)apiRequest.getRequestParam();
        if (priceExaplainVO==null)
            return APIUtil.paramErrorResponse("城市为空");

        String city = priceExaplainVO.getCity();

        PriceExplain priceExaplainInfo = priceExaplainMapper.queryPriceExplainByCirty(city);
        PriceExplain priceExaplain = new PriceExplain();
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
    /**
     * 价格说明 通过id删除
     */
	@Override
	public APIResponse deletePriceExplain(int id) {
		 if(priceExaplainMapper.deletePriceExplain(id) > 0){
	    	   return APIUtil.getResponse(APIStatus.SUCCESS, id);
	       }else{
	    	   return APIUtil.getResponse(APIStatus.PARAM_ERROR, "删除失败，不存在id="+id);
	       }
	}
	/**
	 * 新增价格说明
	 */
	@Override
	public APIResponse addPriceExplain(PriceExplain priceExplain) {
		if(priceExplain != null){
			priceExplain.setCreate_time(Long.toString(System.currentTimeMillis()));
			priceExplain.setUpdate_time(Long.toString(System.currentTimeMillis()));
			priceExplainDao.save(priceExplain);
			return APIUtil.getResponse(APIStatus.SUCCESS, priceExplain);
		}else{
			return APIUtil.getResponse(APIStatus.PARAM_ERROR,"对象为空");
		}
	}
	/**
	 * 修改价格说明
	 */
	@Override
	public APIResponse updatePriceExplain(PriceExplain priceExplain) {
		if(priceExplain != null){
			priceExplain.setUpdate_time(Long.toString(System.currentTimeMillis()));
		if(priceExaplainMapper.updatePriceExplain(priceExplain) > 0 ){
        	return APIUtil.getResponse(APIStatus.SUCCESS, priceExplain);
        }else{
        	return APIUtil.getResponse(APIStatus.PARAM_ERROR, "修改失败,不存在id="+priceExplain.getId());
        }
		}
		return APIUtil.getResponse(APIStatus.PARAM_ERROR, "修改失败,对象为null");
	}
    
    

}
