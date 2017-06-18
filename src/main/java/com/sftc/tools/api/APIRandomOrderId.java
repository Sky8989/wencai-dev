package com.sftc.tools.api;

import com.sftc.tools.md5.MD5Util;

import java.util.Random;

/**
 * Created by Administrator on 2017/6/14.
 */
public class APIRandomOrderId {
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//含有字符和数字的字符串
        Random random = new Random();//随机类初始化
        StringBuffer sb = new StringBuffer();//StringBuffer类生成，为了拼接字符串

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);// [0,62)

            sb.append(str.charAt(number));
        }
       String order_id = MD5Util.MD5(sb.toString());
        return order_id;
    }
}
