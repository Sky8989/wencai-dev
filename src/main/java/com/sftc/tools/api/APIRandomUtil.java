package com.sftc.tools.api;

import com.sftc.tools.md5.MD5Util;

import java.util.Random;

public class APIRandomUtil {

    private static final int ORDER_LENGTH = 9;

    private static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }

        return MD5Util.MD5(sb.toString());
    }

    public static String getRandomString() {
        return getRandomString(ORDER_LENGTH);
    }
}
