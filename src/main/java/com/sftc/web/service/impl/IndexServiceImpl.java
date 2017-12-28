package com.sftc.web.service.impl;

import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiStatus;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.constant.LlConstant;
import com.sftc.tools.constant.SfConstant;
import com.sftc.tools.sf.SfTokenHelper;
import com.sftc.tools.utils.ChineseAndEnglishUtil;
import com.sftc.tools.utils.ChineseToPinyinUtil;
import com.sftc.web.dao.mybatis.IndexMapper;
import com.sftc.web.dao.redis.*;
import com.sftc.web.model.dto.CompensateDTO;
import com.sftc.web.model.dto.GiftCardListDTO;
import com.sftc.web.model.entity.*;
import com.sftc.web.model.vo.swaggerRequest.CoordinateVO;
import com.sftc.web.model.vo.swaggerRequest.PriceExplainVO;
import com.sftc.web.model.vo.swaggerRequest.SFAPIRequestVO;
import com.sftc.web.model.vo.swaggerRequest.SFAccessTokenRequestVO;
import com.sftc.web.service.IndexService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.SfConstant.SFEnvironment.*;

/**
 * @author Administrator
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Resource
    private IndexMapper indexMapper;
    @Resource
    private ServicePhoneRedisDao servicePhoneRedisDao;
    @Resource
    private CommonQuestionRedisDao commonQuestionRedisDao;
    @Resource
    private CompensateRedisDao compensateRedisDao;
    @Resource
    private PriceExplainRedisDao priceExplainRedisDao;
    @Resource
    private CityExpressRedisDao cityExpressRedisDao;

    @Override
    public ApiResponse setupEnvironment(SFAPIRequestVO sfApiRequestVO) {


        if (StringUtils.isEmpty(sfApiRequestVO.getEnvironment())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "参数environment不能为空");
        }
        String environment = sfApiRequestVO.getEnvironment();
        String product = "product";
        String stage = "stage";
        String dev = "dev";
        if (product.equals(environment)) {
            SfConstant.setEnvironment(SfEnvironmentProduct);
        } else if (stage.equals(environment)) {
            SfConstant.setEnvironment(SfEnvironmentStage);
        } else if (dev.equals(environment)) {
            SfConstant.setEnvironment(SfEnvironmentDev);
        } else {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "参数environment不正确");
        }

        JSONObject respObject = new JSONObject();
        respObject.put("sftc_domain", SfConstant.getSfSameDomain());

        return ApiUtil.getResponse(SUCCESS, respObject);
    }

    @Override
    public ApiResponse setupCommonToken(SFAccessTokenRequestVO sfAccessTokenRequestVO) {

        if (StringUtils.isEmpty(sfAccessTokenRequestVO.getAccess_token())) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "参数access_token不能为空");
        }
        String accessToken = sfAccessTokenRequestVO.getAccess_token();

        SfTokenHelper.COMMON_ACCESSTOKEN = accessToken;

        JSONObject respObject = new JSONObject();
        respObject.put("access_token", accessToken);
        return ApiUtil.getResponse(SUCCESS, respObject);
    }

    /**
     * 根据城市获取相对应的服务电话
     */
    @Override
    public ApiResponse getServicePhoneByCity(PriceExplainVO priceExplainVO) {
        if (priceExplainVO == null) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "城市为空");
        }

        String city = priceExplainVO.getCity();
        boolean isEnglish = ChineseAndEnglishUtil.isEnglish(city);
        if (isEnglish) {
            JSONArray servicePhoneCache = servicePhoneRedisDao.getServicePhoneCache();
            if (servicePhoneCache == null) {
                List<String> cityLists = indexMapper.queryCityName();
                if (cityLists != null) {
                    servicePhoneCache = JSONArray.fromObject(cityLists);
                    servicePhoneRedisDao.setServicePhoneCache(servicePhoneCache);
                }
            }
            List<String> cityLists = (List<String>) JSONArray.toCollection(servicePhoneCache, String.class);
            for (String str : cityLists) {
                String toPinyin = ChineseToPinyinUtil.toHanyuPinyin(str);
                if (toPinyin.contains(city)) {
                    city = str;
                    break;
                }
            }
        }
        ServicePhone servicePhone;
        servicePhone = indexMapper.getServicePhoneByCity(city);

        ServicePhone servicePhone1 = new ServicePhone();
        servicePhone1.setCity(city);
        if (servicePhone == null) {
            servicePhone1.setCity("");
            servicePhone1.setPhone("");
        } else {
            servicePhone1.setCity(servicePhone.getCity());
            servicePhone1.setPhone(servicePhone.getPhone());
        }
        JSONObject responeJSON = JSONObject.fromObject(servicePhone1);
        responeJSON.remove("id");
        responeJSON.remove("create_time");
        responeJSON.remove("update_time");
        return ApiUtil.getResponse(SUCCESS, responeJSON);
    }

    /**
     * 获取常见问题
     */
    @Override
    public ApiResponse getCommonQuestion() {
        // 尝试从redis缓存中获取数据
        List<CommonQuestion> commonQuestionList = commonQuestionRedisDao.getCommonQuestionsFromCache();
        if (commonQuestionList == null) {
            commonQuestionList = indexMapper.getCommonQuestion();
            commonQuestionRedisDao.setCommonQuestionsToCache(commonQuestionList);
        }

        return ApiUtil.getResponse(ApiStatus.SUCCESS, commonQuestionList);
    }

    @Override
    public ApiResponse getCompensate() {
        // 尝试从redis缓存中获取数据
        List<Compensate> compensateList = compensateRedisDao.getCompensateFromCache();
        if (compensateList == null) {
            compensateList = indexMapper.getCompensate();
            compensateRedisDao.setCompensateToCache(compensateList);
        }
        CompensateDTO compensateDTO = new CompensateDTO();
        compensateDTO.setCompensates(compensateList);

        return ApiUtil.getResponse(ApiStatus.SUCCESS, compensateDTO);
    }

    /**
     * 根据城市获取相对应的价格说明
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse getPriceExplainByCity(PriceExplainVO priceExplainVO) {
        if (priceExplainVO == null) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "the request body is empty");
        }

        String city = priceExplainVO.getCity();
        boolean isChinese = ChineseAndEnglishUtil.isEnglish(city);
        PriceExplain priceExaplainInfo;
        if (isChinese) {
            JSONArray cityExpresssCache = priceExplainRedisDao.getCityExpresssCache();
            if (cityExpresssCache == null) {
                List<String> cityLists = indexMapper.queryCityName();
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
        priceExaplainInfo = indexMapper.queryPriceExplainByCirty(city);
        JSONObject respJson = new JSONObject();
        respJson.put("city", city);
        respJson.put("price_explain", JSONArray.fromObject(priceExaplainInfo.getPrice_explain()));
        return ApiUtil.getResponse(SUCCESS, respJson);
    }

    @Override
    public ApiResponse getGiftCardList() {

        List<GiftCard> giftCards = indexMapper.giftCardList();
        List<GiftCardListDTO> giftCardLists = new ArrayList<>();

        for (GiftCard giftCard : giftCards) {
            // 遍历当前的giftCardLists数组，判断这个type的GiftCardList是否已经存在，如果存在，取出这个type的giftCardList
            GiftCardListDTO giftCardList = null;
            for (GiftCardListDTO tempList : giftCardLists) {
                if (tempList.getType().equals(giftCard.getType())) {
                    giftCardList = tempList;
                    break;
                }
            }
            // 已存在，GiftCardList对象的giftCards数组添加giftCard
            if (giftCardList != null) {
                List<GiftCard> gitCards = giftCardList.getGiftCards();
                gitCards.add(giftCard);
            } else { // 不存在，新建一个GiftCardList，进行赋值，再add进giftCardLists数组
                GiftCardListDTO list = new GiftCardListDTO();
                List<GiftCard> gitCards = new ArrayList<>();
                gitCards.add(giftCard);
                list.setType(giftCard.getType());
                list.setGiftCards(gitCards);
                giftCardLists.add(list);
            }
        }
        return ApiUtil.getResponse(SUCCESS, giftCardLists);
    }

    @Override
    public ApiResponse getCityExpressList() {
        //缓存获取
        JSONArray cityExpresssCache = cityExpressRedisDao.getCityExpresssCache();
        if (cityExpresssCache == null) {
            List<CityExpress> cityLists = indexMapper.quaryCityExpressList();
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
        return ApiUtil.getResponse(SUCCESS, responJson);
    }

    @Override
    public ApiResponse getLatitudeLongitude(CoordinateVO coordinateVO) {

        // 核对时间 限定范围时间内 才可以获取
        ApiResponse apiResponse = checkTimeIsLogical();
        if (apiResponse != null) {
            return apiResponse;
        }

        // 验参
        if (coordinateVO.getLatitude() == null || coordinateVO.getLatitude() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "缺少纬度参数latitude");
        } else if (coordinateVO.getLongitude() == null || coordinateVO.getLongitude() == 0) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "缺少经度参数longitude");
        }
        double latitude = coordinateVO.getLatitude();
        double longitude = coordinateVO.getLongitude();

        // 计算生成经纬度点的数量
        int generatedNumber = (LlConstant.MIN_LL_NUMBER + new Random().nextInt(LlConstant.MAX_LL_NUMBER));
        // 调用算法
        List<Map<String, Double>> llResults = LlConstant.calculate3(latitude, longitude, LlConstant.RANGE_NUMBER,
                generatedNumber);
        if (llResults == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "No_Point");
        }

        return ApiUtil.getResponse(ApiStatus.SUCCESS, llResults);
    }

    ////////////////////////////// private////////////////////////////////////

    /**
     * 检测获取随机点的时间是否符合逻辑
     *
     * @return ApiResponse
     */
    private ApiResponse checkTimeIsLogical() {
        // 核对时间 限定范围时间内 才可以获取
        int nowHour = LocalTime.now().getHour();
        if (nowHour >= LlConstant.END_HOUR || nowHour < LlConstant.BEGIN_HOUR) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "param No_Point");
        }
        return null;
    }
}
