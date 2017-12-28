package com.sftc.tools.encode;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 */
public class Base64Util {

    /**
     * Base64加密
     *
     * @param string 明文
     * @return 密文
     */
    @SuppressWarnings("restriction")
    public static String getBase64(String string) {
        byte[] b = null;
        String s = null;
        try {
            b = string.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    /**
     * Base64解密
     *
     * @param string 密文
     * @return 明文
     */
    @SuppressWarnings("restriction")
    static String getFromBase64(String string) {
        byte[] b;
        String result = null;
        if (string != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(string);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}