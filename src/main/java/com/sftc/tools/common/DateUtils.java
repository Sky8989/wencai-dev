package com.sftc.tools.common;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

public class DateUtils {

    public static String iSO8601DateWithTimeStamp(long timeStamp) {
        Date date = new Date(timeStamp);
        String pattern = "yyyy-MM-dd'T'HH:mm:ssZZ";

        return DateFormatUtils.format(date, pattern);
    }

    public static String iSO8601DateWithTimeStampAndFormat(String timeStampStr, String pattern) {
        Date date = new Date(Long.parseLong(timeStampStr));

        return DateFormatUtils.format(date, pattern);
    }

    public static String iSO8601DateWithTimeStampStr(String timeStampStr) {
        long timeStamp = Long.parseLong(timeStampStr);
        if (timeStampStr.length() == 10) {
            timeStamp = timeStamp * 1000;
        }
        return iSO8601DateWithTimeStamp(timeStamp);
    }
}
