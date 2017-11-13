package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.APIGetUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.dao.mybatis.SFServiceAddressMapper;
import com.sftc.web.model.Express;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.entity.Order;
import com.sftc.web.model.entity.OrderExpress;
import com.sftc.web.model.sfmodel.SFServiceAddress;
import com.sftc.web.service.SFServiceAddressService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Service
public class SFServiceAddressServiceImpl implements SFServiceAddressService {

    private static final String SF_SERVICE_ADDRESS_BASE = "http://www.sf-express.com/";
    private static final String SF_SERVICE_ADDRESS_DOMIAN = SF_SERVICE_ADDRESS_BASE + "sf-service-owf-web/service/region/";
    private static final String SF_SERVICE_ADDRESS_COUNTRY = SF_SERVICE_ADDRESS_DOMIAN + "A000000000/subRegions/origins?level=-1&lang=sc&region=cn&translate=";
    private static final String SF_SERVICE_ADDRESS_PROVINCE = SF_SERVICE_ADDRESS_DOMIAN + "{countryCode}/subRegions/origins?level=-1&lang=sc&region=cn&translate=";
    private static final String SF_SERVICE_ADDRESS_CITY = SF_SERVICE_ADDRESS_DOMIAN + "{provinceCode}/subRegions/origins?level=2&lang=sc&region=cn&translate=";
    private static final String SF_SERVICE_ADDRESS_AREA = SF_SERVICE_ADDRESS_DOMIAN + "{cityCode}/subRegions/dests?originCode=A440305000&level=3&lang=sc&region=cn&translate=";

    private static final String SF_SERVICE_ADDRESS_QUERY = SF_SERVICE_ADDRESS_DOMIAN + "live/origin?lang=sc&region=cn&translate=&query={keyword}&limit=50&level={level}";

    private static final String SF_SERVICE_RATE = SF_SERVICE_ADDRESS_BASE + "sf-service-owf-web/service/rate?origin={origin}&dest={dest}&weight={weight}&time={time}&volume=0&queryType=2&lang=sc&region=cn";

    @Resource
    private SFServiceAddressMapper sfServiceAddressMapper;

    @Resource
    private OrderMapper orderMapper;

    private Gson gson = new Gson();

    /**
     * 查询订单动态配送时效价格
     */
    public APIResponse selectOrderDynamicPrice(APIRequest request) {

        String orderId = (String) request.getParameter("order_id");
        if (orderId == null || orderId.equals(""))
            return APIUtil.paramErrorResponse("order_id不能为空");

        if (orderId == null || orderId.equals(""))
            return APIUtil.paramErrorResponse("order_id无效");

        OrderDTO orderDTO = orderMapper.selectOrderDetailByOrderId(orderId);
        if (orderDTO == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        OrderExpress oe = orderDTO.getOrderExpressList().get(0);
        if (oe == null)
            return APIUtil.selectErrorResponse("快递不存在", null);

        //多包裹的估算规则还不清楚
        if (orderDTO.getOrderExpressList().size() != 1) // 暂时只有单包裹才能算配送方式
            return APIUtil.selectErrorResponse("暂时只支持单包裹的订单查询运费时效", null);

        String senderCity = orderDTO.getSender_city().replace("市", "");
        String receiverCity = oe.getShip_city().replace("市", "");
        String senderArea = orderDTO.getSender_area();
        String receiverArea = oe.getShip_area();
        if (!senderArea.endsWith("区")) senderArea = senderArea + "区";
        if (!receiverArea.endsWith("区")) receiverArea = receiverArea + "区";

        SFServiceAddress senderCityAddress = sfServiceAddressMapper.selectServiceAddressByNameAndLevel(senderCity, 3);
        SFServiceAddress receiveCityAddress = sfServiceAddressMapper.selectServiceAddressByNameAndLevel(receiverCity, 3);

        if (senderCityAddress == null)
            return APIUtil.selectErrorResponse("顺丰不支持寄件人城市", null);
        if (receiveCityAddress == null)
            return APIUtil.selectErrorResponse("顺丰不支持收件人城市", null);

        // request sf param
        String senderAreaCode = getServiceAddressCode(senderArea, 4, senderCityAddress.getCode());
        String receiverAreaCode = getServiceAddressCode(receiverArea, 4, receiveCityAddress.getCode());
        String weight = oe.getWeight();

        return getServiceRate(senderAreaCode, receiverAreaCode, weight, null);
    }

    /**
     * 查询动态配送时效价格
     */
    public APIResponse selectDynamicPrice(APIRequest request) {

        Object object = request.getRequestParam();
        JSONObject requestObject = JSONObject.fromObject(object);

        JSONObject targetObject = requestObject.getJSONObject("target");
        JSONObject sourceObject = requestObject.getJSONObject("source");
        String senderCity = (String) sourceObject.get("city");
        String senderArea = (String) sourceObject.get("area");
        String receiverCity = (String) targetObject.get("city");
        String receiverArea = (String) targetObject.get("area");
        String dateTime = requestObject.containsKey("query_time") ? (String) requestObject.get("query_time") : "";

        if (senderCity == null || receiverCity == null || senderArea == null || receiverArea == null || senderCity.equals("") || receiverCity.equals("") || senderArea.equals("") || receiverArea.equals(""))
            return APIUtil.paramErrorResponse("请求体不完整");

        // handle address
        senderCity = senderCity.replace("市", "");
        receiverCity = receiverCity.replace("市", "");
        //取消对area的末尾加上区，改为去除 区、县 sf接口不支持 ‘紫金县区’
//        if (!senderArea.endsWith("区")) senderArea = senderArea + "区";
//        if (!receiverArea.endsWith("区")) receiverArea = receiverArea + "区";
        if (senderArea.endsWith("区")) senderArea = senderArea.replace("区", "");
        if (senderArea.endsWith("县")) senderArea = senderArea.replace("县", "");
        if (receiverArea.endsWith("区")) receiverArea = receiverArea.replace("区", "");
        if (receiverArea.endsWith("县")) receiverArea = receiverArea.replace("县", "");

//        SFServiceAddress senderCityAddress = sfServiceAddressMapper.selectServiceAddressByNameAndLevel(senderCity, 3);
//        SFServiceAddress receiveCityAddress = sfServiceAddressMapper.selectServiceAddressByNameAndLevel(receiverCity, 3);

        SFServiceAddress senderCityAddress = sfServiceAddressMapper.selectServiceAddressByName(senderCity);
        SFServiceAddress receiveCityAddress = sfServiceAddressMapper.selectServiceAddressByName(receiverCity);

        if (senderCityAddress == null)
            return APIUtil.selectErrorResponse("顺丰不支持寄件人城市", null);
        if (receiveCityAddress == null)
            return APIUtil.selectErrorResponse("顺丰不支持收件人城市", null);

        // request sf param
        String senderAreaCode = getServiceAddressCode(senderArea, 4, senderCityAddress.getCode());
        String receiverAreaCode = getServiceAddressCode(receiverArea, 4, receiveCityAddress.getCode());

        if (senderAreaCode == null)
            return APIUtil.selectErrorResponse("顺丰不支持寄件人城市", null);
        if (receiverAreaCode == null)
            return APIUtil.selectErrorResponse("顺丰不支持收件人城市", null);

        int weight = 1;
        try {
            weight = requestObject.getInt("weight");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String weightStr = (weight == 0 ? 1 : weight) + "";

        return getServiceRate(senderAreaCode, receiverAreaCode, weightStr, dateTime);
    }

    // 获取顺丰服务地址编码
    private String getServiceAddressCode(String keyword, int level, String parentCode) {

        String url = SF_SERVICE_ADDRESS_QUERY.replace("{keyword}", keyword).replace("{level}", level + "");
        String result = APIGetUtil.get(new HttpGet(url));
        List<SFServiceAddress> addresses = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress address : addresses) {
            if (address.getParentCode().equals(parentCode)) {
                return address.getCode();
            }
        }
        return null;
    }

    // 获取时效计价
    private APIResponse getServiceRate(String origin, String dest, String weight, String dateTime) {

        String pattern = "yyyy-MM-dd'T'HH:mm:ssZZ";
        Date date = dateTime.equals("") ? new Date() : new Date(Long.parseLong(dateTime));
        String time = DateFormatUtils.format(date, pattern);
        try {
            time = URLEncoder.encode(time, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return APIUtil.selectErrorResponse("不支持URLEncode，服务器系统异常", e.getLocalizedMessage());
        }

        String rateUrl = SF_SERVICE_RATE.replace("{origin}", origin).replace("{dest}", dest).replace("{time}", time).replace("{weight}", weight);
        HttpGet get = new HttpGet(rateUrl);
        String result = APIGetUtil.get(get);
        Type type = new TypeToken<ArrayList<Express>>() {
        }.getType();
        List<Express> lists = gson.fromJson(result, type);

        if (lists != null && lists.size() >= 2) {
            Collections.sort(lists, new Comparator<Express>() {
                @Override
                public int compare(Express o1, Express o2) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    long time1 = 0;
                    long time2 = 0;
                    try {
                        if (o1.getDeliverTime() != null)
                            time1 = simpleDateFormat.parse(o1.getDeliverTime()).getTime();
                        if (o2.getDeliverTime() != null)
                            time2 = simpleDateFormat.parse(o2.getDeliverTime()).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return Long.compare(time1, time2);
                }
            });
        }
        List<Express> resultList = new ArrayList<>();
        if (lists != null && lists.size() > 0) {
            for (Express e : lists) {
                if (e.getDeliverTime() != null && e.getClosedTime() != null) {
                    if (e.getLimitTypeCode().equals("T801") ||
                            e.getLimitTypeCode().equals("T4") ||
                            e.getLimitTypeCode().equals("T6")) {
                        resultList.add(e);
                        break;
                    }
                }
            }
        }
        return APIUtil.getResponse(SUCCESS, resultList);
    }

    public APIResponse updateServiceAddress(APIRequest request) {

        updateCountry();

        return APIUtil.getResponse(SUCCESS, null);
    }

    private void updateCountry() {

        HttpGet get = new HttpGet(SF_SERVICE_ADDRESS_COUNTRY);
        String result = APIGetUtil.get(get);

        List<SFServiceAddress> countries = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress country : countries) {
            // 暂时只抓取中国的
            if (country.getName().equals("中国")) {
                sfServiceAddressMapper.insertSFServiceAddress(country);
                updateProvince(country);
            }
        }
    }

    private void updateProvince(SFServiceAddress country) {

        String url = SF_SERVICE_ADDRESS_PROVINCE.replace("{countryCode}", country.getCode());
        String result = APIGetUtil.get(new HttpGet(url));

        List<SFServiceAddress> provinces = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress province : provinces) {
            sfServiceAddressMapper.insertSFServiceAddress(province);
            updateCity(province);
        }
    }

    private void updateCity(SFServiceAddress province) {

        String url = SF_SERVICE_ADDRESS_CITY.replace("{provinceCode}", province.getCode());
        String result = APIGetUtil.get(new HttpGet(url));

        List<SFServiceAddress> cities = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress city : cities) {
            sfServiceAddressMapper.insertSFServiceAddress(city);
//            updateArea(city);
        }
    }

    private void updateArea(SFServiceAddress city) {

        String url = SF_SERVICE_ADDRESS_AREA.replace("{cityCode}", city.getCode());
        String result = APIGetUtil.get(new HttpGet(url));

        List<SFServiceAddress> areas = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress area : areas) {
            sfServiceAddressMapper.insertSFServiceAddress(area);
        }
    }
}
