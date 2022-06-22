package com.bachan.xiaoer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    private static final String COMMON_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将日期转换为标准的日期格式
     * @param date
     * @return
     */
    public static String dateToDateStr(Date date){
        SimpleDateFormat format = new SimpleDateFormat(COMMON_FORMAT, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 将日期转换为标准的日期格式
     * @param date
     * @return
     */
    public static String dateToDateStr(Date date,String formatStr){
        SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 将毫秒转换为标准日期格式
     *
     * @param _ms
     * @return
     */
    public static String msToDate(long _ms) {
        Date date = new Date(_ms);
        SimpleDateFormat format = new SimpleDateFormat(COMMON_FORMAT, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 将毫秒（Java下时间戳）转换为日期格式，指定格式
     *
     * @param _ms
     * @param format
     * @return
     */
    public static String msToDate(long _ms, String format) {
        Date date = new Date(_ms);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 标准格式化时间字符串转换为时间戳
     *
     * @param _data
     * @return
     */
    public static long dateToMs(String _data) {
        SimpleDateFormat format = new SimpleDateFormat(COMMON_FORMAT,Locale.getDefault());
        try {
            Date date = format.parse(_data);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 格式化时间字符串转换为时间戳，指定要转化的时间字符串的格式
     *
     * @param pattern
     * @param _date
     * @return
     */
    public static long dateToMs(String _date,String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,Locale.getDefault());
        try {
            Date date = format.parse(_date);
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 计算时间差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String dateDistance(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 0) {
            timeLong = 0;
        }
        if (timeLong < 60 * 1000)
            return timeLong / 1000 + "秒前";
        else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if ((timeLong / 1000 / 60 / 60 / 24) < 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            return formatter.format(startDate);
        }
    }
}

