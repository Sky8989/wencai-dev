package com.sftc.web.dao.mybatis;

import com.sftc.web.model.entity.CityExpress;

import java.util.List;

/**
 * @author ： CatalpaFlat
 * @date ：Create in 10:27 2017/11/24
 */
public interface CityExpressMapper {
    List<CityExpress> quaryCityExpressList();
}
