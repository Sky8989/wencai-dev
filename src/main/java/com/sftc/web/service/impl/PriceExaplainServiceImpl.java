package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIStatus;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.utils.ChineseAndEnglishUtil;
import com.sftc.tools.utils.ChineseToPinyinUtil;
import com.sftc.web.dao.jpa.PriceExplainDao;
import com.sftc.web.dao.mybatis.PriceExaplainMapper;
import com.sftc.web.dao.redis.PriceExplainRedisDao;
import com.sftc.web.model.entity.PriceExplain;
import com.sftc.web.model.vo.swaggerRequest.DeletePriceExplain;
import com.sftc.web.model.vo.swaggerRequest.PriceExaplainVO;
import com.sftc.web.service.PriceExaplainService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Transactional
@Service("priceExaplainService")
public class PriceExaplainServiceImpl implements PriceExaplainService {
    @Resource
    private PriceExaplainMapper priceExaplainMapper;

    @Resource
    private PriceExplainRedisDao priceExplainRedisDao;

    /**
     * 根据城市获取相对应的价格说明
     */
    @Override
    public APIResponse getPriceExplainByCity(APIRequest apiRequest) {
        PriceExaplainVO priceExaplainVO = (PriceExaplainVO) apiRequest.getRequestParam();
        if (priceExaplainVO == null)
            return APIUtil.paramErrorResponse("城市为空");

        String city = priceExaplainVO.getCity();
        boolean isChinese = ChineseAndEnglishUtil.isEnglish(city);
        PriceExplain priceExaplainInfo;
        if (isChinese) {
            JSONArray cityExpresssCache = priceExplainRedisDao.getCityExpresssCache();
            if (cityExpresssCache == null) {
                List<String> cityLists = priceExaplainMapper.queryCityName();
                if (cityLists != null) {
                    cityExpresssCache = JSONArray.fromObject(cityLists);
                    priceExplainRedisDao.setCityExpresssCache(cityExpresssCache);
                }
            }
            List<String> cityLists = (List<String>) JSONArray.toCollection(cityExpresssCache, String.class);
            for (String str : cityLists) {
                String toPinyin = ChineseToPinyinUtil.toHanyuPinyin(str);
                if (toPinyin.contains(city)) {
                    city = str;
                    break;
                }
            }
        }
        priceExaplainInfo = priceExaplainMapper.queryPriceExplainByCirty(city);

        PriceExplain priceExaplain = new PriceExplain();
        priceExaplain.setCity(city);
        if (priceExaplainInfo == null) {
            priceExaplain.setDistance_price("");
            priceExaplain.setWeight_price("");
        } else {
            priceExaplain.setDistance_price(priceExaplainInfo.getDistance_price());
            priceExaplain.setWeight_price(priceExaplainInfo.getWeight_price());
        }
        JSONObject responeJSON = JSONObject.fromObject(priceExaplain);
        responeJSON.remove("id");
        responeJSON.remove("create_time");
        responeJSON.remove("update_time");
        return APIUtil.getResponse(SUCCESS, responeJSON);

    }
}
