package com.sftc.tools.api;

import com.sftc.tools.md5.MD5Util;

import java.util.Random;

public class APIRandomUtil {

    private static final int ORDER_LENGTH = 12;

    private static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        String result = MD5Util.MD5(sb.toString());

        if (result != null && result.length() > ORDER_LENGTH) {
            result = result.substring(0, ORDER_LENGTH);
        }

        return result;
    }

    public static String getRandomString() {
        return getRandomString(ORDER_LENGTH);
    }
}
