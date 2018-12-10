package com.holderzone.intelligencepos.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/3/30.
 */

public class DateUtils {
    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";
    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    public final static String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return FORMAT_LONG;
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串(yyyy-MM-dd HH:mm:ss)
     * @param pattern
     * @return
     */
    public static String anewFormat(String strDate, String pattern) {
        return format(parse(strDate, getDatePattern()), pattern);
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Calendar dateString2Calendar(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Calendar mCalendar = Calendar.getInstance(Locale.CHINESE);
        try {
            mCalendar.setTime(df.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mCalendar;
    }

    public static String getWeekDay(int week) {
        if (week > 6 || week < 0) {
            return null;
        }
        return dayNames[week];
    }
}
