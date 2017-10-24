package com.sftc.web.model.Converter;

import com.sftc.web.model.OrderNationTime;

/**
 * Created by xf on 2017/10/23.
 */
public class OrderTimeFactory {
    public static OrderNationTime setOrderNationTimeOn(){
        OrderNationTime orderNationTime = new OrderNationTime();
        orderNationTime.setOn(0);
        orderNationTime.setDelay(0);
        orderNationTime.setPeriod(1800);
        return orderNationTime;
    }
}
