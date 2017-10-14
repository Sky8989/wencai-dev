package com.sftc.tools.sf;

import com.sftc.tools.md5.MD5Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SFOrderHelper {

    private static final int ORDER_LENGTH = 12;
    private static final String RANDOM_SOURCE_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String getRandomString(int length) {

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(RANDOM_SOURCE_STRING.length());
            sb.append(RANDOM_SOURCE_STRING.charAt(number));
        }
        String result = MD5Util.MD5(sb.toString());
//        String timeRandom = Long.toString(System.currentTimeMillis());
//        String result = "C" + timeRandom +randomResult;

        if (result != null && result.length() > ORDER_LENGTH) {
            result = result.substring(0, ORDER_LENGTH);
        }

        return result;
    }

    public static String getOrderNumber() {
        return getRandomString(ORDER_LENGTH);
    }

    public static Map<String, String> getKeywordMap() {
        Map<String, String> map = new HashMap<String, String>();
        //匹配完成状态
        map.put("已", "'FINISHED'");
        map.put("已完成", "'FINISHED'");
        map.put("完成", "'FINISHED'");
        map.put("待填写", "'WAIT_FILL'");
        map.put("已填写", "'ALREADY_FILL'");
        map.put("下单", "'INIT'");
        map.put("待支付", "'INIT'");
        map.put("支付", "'PAYING'");
        map.put("支付中", "'PAYING'");
        map.put("待揽件", "'WAIT_HAND_OVER'");
        map.put("揽件", "'WAIT_HAND_OVER'");
        //退款
        map.put("退款", "'WAIT_REFUND|REFUNDING|WAIT_REFUND'");
        map.put("已退款", "'REFUNDED'");
        map.put("退款中", "'REFUNDING'");
        map.put("待退款", "'WAIT_REFUND'");
        //派送中
        map.put("派送", "'DELIVERING'");
        map.put("派送中", "'DELIVERING'");
        map.put("派件", "'DELIVERING'");
        map.put("派件中", "'DELIVERING'");
        //匹配 取消
        map.put("取消", "'CANCELED' OR attributes REGEXP 'CUSTOMER_CANCEL|CONTACT_CUSTOMER_FAILURE|ERROR_CUSTOMER_ADDRESS|CONFORM_TO_ORDER_FAILURE|PICK_UP_OTHERS|DISPATCH_TIME_OUT'  ");
        map.put("已取消", "'CANCELED' OR attributes REGEXP 'CUSTOMER_CANCEL|CONTACT_CUSTOMER_FAILURE|ERROR_CUSTOMER_ADDRESS|CONFORM_TO_ORDER_FAILURE|PICK_UP_OTHERS|DISPATCH_TIME_OUT'  ");
        map.put("取消订单", "'CANCELED' OR attributes REGEXP 'CUSTOMER_CANCEL|CONTACT_CUSTOMER_FAILURE|ERROR_CUSTOMER_ADDRESS|CONFORM_TO_ORDER_FAILURE|PICK_UP_OTHERS|DISPATCH_TIME_OUT'  ");
        map.put("订单取消", "'CANCELED' OR attributes REGEXP 'CUSTOMER_CANCEL|CONTACT_CUSTOMER_FAILURE|ERROR_CUSTOMER_ADDRESS|CONFORM_TO_ORDER_FAILURE|PICK_UP_OTHERS|DISPATCH_TIME_OUT'  ");

        return map;
    }
}
