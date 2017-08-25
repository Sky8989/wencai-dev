package com.sftc.tools.common;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static Locale locale = Locale.CHINA;

    public static String iSO8601DateWithTimeStamp(long timeStamp) {
        Date date = new Date(timeStamp);
        String pattern = "yyyy-MM-dd'T'HH:mm:sssZZ";

        return DateFormatUtils.format(date, pattern);
    }

    public static String iSO8601DateWithTimeStampAndFormat(String timeStampStr, String pattern) {
        Date date = new Date(Long.parseLong(timeStampStr));

        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 带时区的时间转化方法
     *
     * @param timeStampStr
     * @param pattern
     * @return
     */
    public static String iSO8601DateWithTimeStampAndFormat2(String timeStampStr, String pattern) {
        return DateFormatUtils.format(Long.parseLong(timeStampStr), pattern, locale);
    }

    public static String iSO8601DateWithTimeStampStr(String timeStampStr) {
        long timeStamp = Long.parseLong(timeStampStr);
        if (timeStampStr.length() == 10) {
            timeStamp = timeStamp * 1000;
        }
        return iSO8601DateWithTimeStamp(timeStamp);
    }
}
