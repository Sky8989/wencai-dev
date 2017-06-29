package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.APIGet;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.mapper.SFServiceAddressMapper;
import com.sftc.web.model.Order;
import com.sftc.web.model.OrderExpress;
import com.sftc.web.model.sfmodel.SFServiceAddress;
import com.sftc.web.service.SFServiceAddressService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

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
     * 查询动态配送时效价格
     */
    public APIResponse selectDynamicPrice(APIRequest request) {

        String orderiId = (String) request.getParameter("order_id");
        if (orderiId == null || orderiId.equals(""))
            return APIUtil.paramErrorResponse("order_id不能为空");

        int order_id = Integer.parseInt(orderiId);
        if (order_id < 1)
            return APIUtil.paramErrorResponse("order_id无效");

        Order order = orderMapper.selectOrderDetailByOrderId(order_id);
        if (order == null)
            return APIUtil.selectErrorResponse("订单不存在", null);

        OrderExpress oe = order.getOrderExpressList().get(0);
        if (oe == null)
            return APIUtil.selectErrorResponse("快递不存在", null);

        if (order.getOrderExpressList().size() != 1) // 暂时只有单包裹才能算配送方式
            return APIUtil.selectErrorResponse("暂时只支持单包裹的订单查询运费时效", null);

        String senderCity = order.getSender_city().replace("市", "");
        String receiverCity = oe.getShip_city().replace("市", "");

        String senderArea = order.getSender_area();
        String receiverArea = oe.getShip_area();

        SFServiceAddress senderCityAddress = sfServiceAddressMapper.selectServiceAddressByNameAndLevel(senderCity, 3);
        SFServiceAddress receiveCityAddress = sfServiceAddressMapper.selectServiceAddressByNameAndLevel(receiverCity, 3);

        String senderAreaCode = getServiceAddressCode(senderArea, 4, senderCityAddress.getCode());
        String receiverAreaCode = getServiceAddressCode(receiverArea, 4, receiveCityAddress.getCode());
        String weight = (String) request.getParameter("weight");
        if (weight == null || weight.equals("")) weight = "1";

        return getServiceRate(senderAreaCode, receiverAreaCode, weight);
    }

    // 获取顺丰服务地址编码
    private String getServiceAddressCode(String keyword, int level, String parentCode) {

        String url = SF_SERVICE_ADDRESS_QUERY.replace("{keyword}", keyword).replace("{level}", level + "");
        String result = APIGet.getGet(new HttpGet(url));
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
    private APIResponse getServiceRate(String origin, String dest, String weight) {

        String pattern = "yyyy-MM-dd'T'HH:mm:ssZZ";
        String time = DateFormatUtils.format(new Date(), pattern);
        try {
            time = URLEncoder.encode(time, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return APIUtil.selectErrorResponse("不支持URLEncode，服务器系统异常", e.getLocalizedMessage());
        }

        String rateUrl = SF_SERVICE_RATE.replace("{origin}", origin).replace("{dest}", dest).replace("{time}", time).replace("{weight}", weight);
        HttpGet get = new HttpGet(rateUrl);
        String result = APIGet.getGet(get);

        Object resultObj = gson.fromJson(result, Object.class);
        return APIUtil.getResponse(SUCCESS, resultObj);
    }

    public APIResponse updateServiceAddress(APIRequest request) {

        updateCountry();

        return APIUtil.getResponse(SUCCESS, null);
    }

    private void updateCountry() {

        HttpGet get = new HttpGet(SF_SERVICE_ADDRESS_COUNTRY);
        String result = APIGet.getGet(get);

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
        String result = APIGet.getGet(new HttpGet(url));

        List<SFServiceAddress> provinces = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress province : provinces) {
            sfServiceAddressMapper.insertSFServiceAddress(province);
            updateCity(province);
        }
    }

    private void updateCity(SFServiceAddress province) {

        String url = SF_SERVICE_ADDRESS_CITY.replace("{provinceCode}", province.getCode());
        String result = APIGet.getGet(new HttpGet(url));

        List<SFServiceAddress> cities = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress city : cities) {
            sfServiceAddressMapper.insertSFServiceAddress(city);
//            updateArea(city);
        }
    }

    private void updateArea(SFServiceAddress city) {

        String url = SF_SERVICE_ADDRESS_AREA.replace("{cityCode}", city.getCode());
        String result = APIGet.getGet(new HttpGet(url));

        List<SFServiceAddress> areas = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress area : areas) {
            sfServiceAddressMapper.insertSFServiceAddress(area);
        }
    }
}
