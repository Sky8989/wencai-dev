package com.sftc.tools.sf;

import com.sftc.tools.md5.MD5Util;

import java.util.Date;
import java.util.Random;

/**
 * Created by xf on 2017/10/14.
 */
public class TestOrder {
    private static final int ORDERID_LENGTH = 2;
    private static final String RANDOM_SOURCE_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static void main(String[] args) {
        String str =  getRandomId(ORDERID_LENGTH);
        System.out.println(str);
    }
    public static String getRandomId(int length) {

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(RANDOM_SOURCE_STRING.length());
            sb.append(RANDOM_SOURCE_STRING.charAt(number));
        }
        String randomResult = sb.toString();
        String timeRandom = Long.toString(new Date().getTime());
        String result = "C" + timeRandom + randomResult;
        return result;
    }
}
