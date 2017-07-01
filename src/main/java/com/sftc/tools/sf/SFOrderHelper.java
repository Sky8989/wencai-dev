package com.sftc.tools.sf;

import com.sftc.tools.md5.MD5Util;

import java.util.Random;

public class SFOrderHelper {

    private static final int ORDER_LENGTH = 12;
    private static final String RANDOM_SOURCE_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static String getRandomString(int length) {

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(RANDOM_SOURCE_STRING.length());
            sb.append(RANDOM_SOURCE_STRING.charAt(number));
        }
        String result = MD5Util.MD5(sb.toString());

        if (result != null && result.length() > ORDER_LENGTH) {
            result = result.substring(0, ORDER_LENGTH);
        }

        return result;
    }

    public static String getOrderNumber() {
        return getRandomString(ORDER_LENGTH);
    }
}
