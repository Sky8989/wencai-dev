package com.sftc.web.service.impl;

import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.CityExpressMapper;
import com.sftc.web.dao.redis.CityExpressRedisDao;
import com.sftc.web.model.entity.CityExpress;
import com.sftc.web.service.CityExpressService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
            List<CityExpress> cityLists = cityExpressMapper.quaryCityExpressList();
            cityExpresssCache = JSONArray.fromObject(cityLists);
            if (cityLists != null && cityLists.size() > 0) {
                cityExpressRedisDao.setCityExpresssCache(JSONArray.fromObject(cityLists));
            }
        }
        List<CityExpress> cityLists = (List<CityExpress>) JSONArray.toCollection(cityExpresssCache, CityExpress.class);
        JSONObject responJson = new JSONObject();
        List<String> citiesList = new ArrayList<>();
        List<String> hostCitiesList = new ArrayList<>();
        for (CityExpress cityExpress : cityLists) {
            Integer isHot = cityExpress.getIs_hot();
            String cityName = cityExpress.getCity_name();
            citiesList.add(cityName);
            if (isHot.equals(1)) {
                hostCitiesList.add(cityName);
            }
        }
        responJson.put("cities", citiesList);
        responJson.put("hot", hostCitiesList);
        return APIUtil.getResponse(SUCCESS, responJson);
    }
}
