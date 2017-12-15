package com.sharechain.finance.utils;

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

}
