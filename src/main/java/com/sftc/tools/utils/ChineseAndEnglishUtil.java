package com.sftc.tools.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 是否中英文判断工具类
 *
 * @author ： CatalpaFlat
 * @date ：Create in 15:34 2017/11/25
 */
public class ChineseAndEnglishUtil {


    /**
     * 是否是中文
     */

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        // GENERAL_PUNCTUATION 判断中文的"号

        // CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号

        // HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;

    }

    /**
     * 是否是英文
     */

    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");

    }

    public static boolean isChinese(String str) {

        String regEx = "[\\u4e00-\\u9fa5]+";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        if (m.find()) {
            return true;
        } else {
            return false;
        }

    }


    public static void main(String[] args) {

        System.out.println(isChinese('员'));

        System.out.println(isChinese('s'));

        System.out.println(isEnglish("程序员"));

        System.out.println(isEnglish("chen"));
    }
}
