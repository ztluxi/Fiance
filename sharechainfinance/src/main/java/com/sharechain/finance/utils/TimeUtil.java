package com.sharechain.finance.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * *Created by ${zhoutao} on 2017/12/14 0013.
 */

public class TimeUtil {
    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentTime() {
        final Calendar mCalendar = Calendar.getInstance();
        return mCalendar.getTime();
    }

    /**
     * 获取当前时间
     *
     * @param pattern "yyyy-MM-dd"
     * @return
     */
    public static String getCurrentTime(String pattern) {
        final Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(mCalendar.getTime());
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        final Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        final Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static int getCurrentDay() {
        final Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前小时
     *
     * @return
     */
    public static int getCurrentHour() {
        final Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前分钟
     *
     * @return
     */
    public static int getCurrentMinute() {
        final Calendar mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.MINUTE);
    }

    /**
     * 获取起始时间与当前时间的间距天数
     *
     * @param startTime
     * @return
     */
    public static int getTimeIntervalForDay(long startTime) {
        long currentTime = System.currentTimeMillis();
        long interval = currentTime - startTime;
        return (int) (interval / 1000 / 60 / 60 / 24);
    }

    /**
     * time转换成Date
     *
     * @param time
     * @return
     */
    public static Date timeMillisToDate(long time) {
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        return mCalendar.getTime();
    }
    /**
     * String转换成Date
     *
     * @param time
     * @return
     */
    public static Date StringToDate(String time) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//
        Date date= null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 获取距今前后多少天的日期
     *
     * @param interval
     * @return 返回Date
     */
    public static Date getBeforeLaterDayForDate(int interval) {
        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        mCalendar.add(Calendar.DAY_OF_MONTH, interval);
        return mCalendar.getTime();
    }

    /**
     * 获取距指定日期前后多少天的日期
     *
     * @param interval
     * @return 返回Date
     */
    public static Date getBeforeLaterDayForDate(long time, int interval) {
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        mCalendar.add(Calendar.DAY_OF_MONTH, interval);
        return mCalendar.getTime();
    }

    /**
     * 获取距今前后多少天的日期
     *
     * @param interval
     * @return 格式化后的时间
     */
    public static String getBeforeLaterDayForYMD(int interval) {
        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        mCalendar.add(Calendar.DAY_OF_MONTH, interval);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        String currentTime = year + "/" + month + "/" + day;
        return currentTime;
    }

    /**
     * 获取距今前后多少天的日期
     *
     * @param interval
     * @return 格式化后的时间
     */
    public static String getBeforeLaterDayForMD(int interval) {
        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        mCalendar.add(Calendar.DAY_OF_MONTH, interval);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        String currentTime = month + "/" + day;
        return currentTime;
    }

    /**
     * 获取距今前后多少月的日期
     *
     * @param month
     * @return 返回Date
     */
    public static Date getBeforeLaterMonthForDate(int month) {
        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        mCalendar.add(Calendar.MONTH, month);
        return mCalendar.getTime();
    }

    /**
     * 时间戳转换为时间
     *
     * @param timeStr 时间 例如: 2016-03-09
     * @param format  时间对应格式  例如: yyyy-MM-dd
     * @return
     */
    public static long formatTimeStampToTime(String timeStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前月份第一天
     *
     * @return
     */
    public static long getCurrentMonthFirstDay() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        return mCalendar.getTime().getTime();
    }

    /**
     * 时间戳转换为时间
     *
     * @param timeStr 时间 例如: 2016-03-09
     * @param format  时间对应格式  例如: yyyy-MM-dd
     * @return
     */
    public static long getTimeStamp(String timeStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static final long ONE_MINUTE = 60000L;
    public static final long ONE_HOUR = 3600000L;
    public static final long ONE_DAY = 86400000L;
    public static final long ONE_WEEK = 604800000L;

    public static final String ONE_SECOND_AGO = "秒前";
    public static final String ONE_MINUTE_AGO = "分钟前";
    public static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    public static String RelativeDateFormat(Date date) {

        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
            if (delta > 48 * ONE_HOUR) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                return df.format(date);
            }
//        if (delta < 30L * ONE_DAY) {
//            long days = toDays(delta);
//            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
//        }
//        if (delta < 12L * 4L * ONE_WEEK) {
//            long months = toMonths(delta);
//            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
//        } else {
//            long years = toYears(delta);
//            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
//        }

        return null;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


}
