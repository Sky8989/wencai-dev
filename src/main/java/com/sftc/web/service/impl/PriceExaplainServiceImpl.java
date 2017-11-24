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
import com.sftc.web.model.vo.swaggerRequest.DeletePriceExplain;
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
	public APIResponse deletePriceExplain(APIRequest apiRequest) {
		DeletePriceExplain priceExplain = (DeletePriceExplain) apiRequest.getRequestParam();
		 if(priceExplain != null && priceExaplainMapper.deletePriceExplain(priceExplain.getId()) > 0){
	    	   return APIUtil.getResponse(APIStatus.SUCCESS, priceExplain);
	       }else{
	    	   return APIUtil.getResponse(APIStatus.PARAM_ERROR, "删除失败 "+ priceExplain);
	       }
	}
	/**
	 * 新增价格说明
	 */
	@Override
	public APIResponse addPriceExplain(PriceExplain priceExplain) {
			priceExplain.setCreate_time(Long.toString(System.currentTimeMillis()));
			priceExplain.setUpdate_time(Long.toString(System.currentTimeMillis()));
			priceExplainDao.save(priceExplain);
			return APIUtil.getResponse(APIStatus.SUCCESS, priceExplain);
	}
	/**
	 * 修改价格说明
	 */
	@Override
	public APIResponse updatePriceExplain(PriceExplain priceExplain) {
			priceExplain.setUpdate_time(Long.toString(System.currentTimeMillis()));
		if(priceExaplainMapper.updatePriceExplain(priceExplain) > 0 ){
        	return APIUtil.getResponse(APIStatus.SUCCESS, priceExplain);
        }else{
        	return APIUtil.getResponse(APIStatus.PARAM_ERROR, "修改失败,不存在id="+priceExplain.getId());
        }
	}
	/**
	 * id为0时 为新增操作  id 非0为修改操作
	 */
	@Override
	public APIResponse save(APIRequest apiRequest) {
		PriceExplain priceExplain = (PriceExplain) apiRequest.getRequestParam();
		 if (priceExplain == null) {
		return APIUtil.getResponse(APIStatus.PARAM_ERROR, "save失败，传入对象为 =" + priceExplain);
	}
		 if(priceExplain.getId() != 0)
			 return updatePriceExplain(priceExplain);
		 
		 return addPriceExplain(priceExplain);
	}
    
    

}
