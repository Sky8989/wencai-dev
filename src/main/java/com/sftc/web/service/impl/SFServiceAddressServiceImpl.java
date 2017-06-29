package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sftc.tools.api.*;
import com.sftc.web.mapper.SFServiceMapper;
import com.sftc.web.model.sfmodel.SFServiceAddress;
import com.sftc.web.service.SFServiceAddressService;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SFServiceAddressServiceImpl implements SFServiceAddressService {

    private static final String SF_SERVICE_ADDRESS_DOMIAN = "http://www.sf-express.com/sf-service-owf-web/service/region/";
    private static final String SF_SERVICE_ADDRESS_COUNTRY = SF_SERVICE_ADDRESS_DOMIAN + "A000000000/subRegions/origins?level=-1&lang=sc&region=cn&translate=";
    private static final String SF_SERVICE_ADDRESS_PROVINCE = SF_SERVICE_ADDRESS_DOMIAN + "{countryCode}/subRegions/origins?level=-1&lang=sc&region=cn&translate=";
    private static final String SF_SERVICE_ADDRESS_CITY = SF_SERVICE_ADDRESS_DOMIAN + "{provinceCode}/subRegions/origins?level=2&lang=sc&region=cn&translate=";
    private static final String SF_SERVICE_ADDRESS_AREA = SF_SERVICE_ADDRESS_DOMIAN + "{cityCode}/subRegions/dests?originCode=A440305000&level=3&lang=sc&region=cn&translate=";

    @Resource
    private SFServiceMapper sfServiceMapper;

    private Gson gson = new Gson();

    public APIResponse updateServiceAddress() {

        updateCountry();

        return APIUtil.getResponse(APIStatus.SUCCESS, null);
    }

    private void updateCountry() {

        HttpGet get = new HttpGet(SF_SERVICE_ADDRESS_COUNTRY);
        String result = APIGet.getGet(get);

        List<SFServiceAddress> countries = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress country : countries) {
            sfServiceMapper.insertSFServiceAddress(country);
            if (country.getName().equals("中国")) {
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
            sfServiceMapper.insertSFServiceAddress(province);
            updateCity(province);
        }
    }

    private void updateCity(SFServiceAddress province) {

        String url = SF_SERVICE_ADDRESS_CITY.replace("{provinceCode}", province.getCode());
        String result = APIGet.getGet(new HttpGet(url));

        List<SFServiceAddress> cities = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress city : cities) {
            sfServiceMapper.insertSFServiceAddress(city);
            updateArea(city);
        }
    }

    private void updateArea(SFServiceAddress city) {

        String url = SF_SERVICE_ADDRESS_AREA.replace("{cityCode}", city.getCode());
        String result = APIGet.getGet(new HttpGet(url));

        List<SFServiceAddress> areas = gson.fromJson(result, new TypeToken<List<SFServiceAddress>>() {
        }.getType());

        for (SFServiceAddress area : areas) {
            sfServiceMapper.insertSFServiceAddress(area);
        }
    }
}
