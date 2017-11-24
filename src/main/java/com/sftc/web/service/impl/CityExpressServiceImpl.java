package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.CityExpressMapper;
import com.sftc.web.dao.redis.CityExpressRedisDao;
import com.sftc.web.service.CityExpressService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;

/**
 * 同城专送逻辑业务层
 *
 * @author ： CatalpaFlat
 * @date ：Create in 10:23 2017/11/24
 */
@Service
public class CityExpressServiceImpl implements CityExpressService {

    @Resource
    private CityExpressMapper cityExpressMapper;
    @Resource
    private CityExpressRedisDao cityExpressRedisDao;

    @Override
    public APIResponse getCityExpressList() {
        //缓存获取
        JSONArray cityExpresssCache = cityExpressRedisDao.getCityExpresssCache();
        if (cityExpresssCache == null) {
            List<String> cityLists = cityExpressMapper.quaryCityExpressList();
            cityExpresssCache = JSONArray.fromObject(cityLists);
            if (cityLists != null && cityLists.size() > 0) {
                cityExpressRedisDao.setCityExpresssCache(JSONArray.fromObject(cityLists));
            }
        }
        JSONObject cityJson = new JSONObject();
        cityJson.put("cities", cityExpresssCache);
        return APIUtil.getResponse(SUCCESS, cityJson);
    }
}
