package com.example.mychatapplication.util;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static android.text.format.DateUtils.FORMAT_12HOUR;
import static android.text.format.DateUtils.FORMAT_24HOUR;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_TIME;
import static android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;
import static android.text.format.DateUtils.FORMAT_SHOW_YEAR;
import static android.text.format.DateUtils.MINUTE_IN_MILLIS;
import static android.text.format.DateUtils.WEEKDAY_FORMAT;

import android.content.Context;
import android.text.format.DateUtils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;

public class TimeUtil {
    public final static String[] weeks = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    public final static String[] weekChinas = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    public static CharSequence dateDisplayFormat(Context context,long timestamp){
//        这行测试用
//        timestamp = timestamp - 365L * 24 * 60 * 60 * 1000;
        if(DateUtils.isToday(timestamp)){
            return DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_TIME);
        }else if(isSameWeek(timestamp)){
            if(isYesterday(timestamp)){
                return "昨天";
            }
            return DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_WEEKDAY);
        } else if (isCurrentYear(timestamp)) {
            return DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_DATE);
        }else{
            return DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_YEAR);
        }
    }

    public static CharSequence detailDateDisplayFormat(Context context,long timestamp){
//        这行测试用
//        timestamp = timestamp - 365L * 24 * 60 * 60 * 1000;
        if(DateUtils.isToday(timestamp)){
            return DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_TIME);
        }else if(isSameWeek(timestamp)){
            if(isYesterday(timestamp)){
                return "昨天 " + DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_TIME);
            }
            return DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_WEEKDAY);
        } else if (isCurrentYear(timestamp)) {
            String formatData = DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_DATE|FORMAT_SHOW_TIME|FORMAT_12HOUR);
            return formatData.substring(0, formatData.indexOf("午") + 1) + DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_TIME);
        }else{
            return DateUtils.formatDateTime(context, timestamp, FORMAT_SHOW_YEAR);
        }
    }
    public static boolean isYesterday(long timestamp) {
        // 将时间戳转换为LocalDate
        ZonedDateTime zonedDateTime = null; // 使用系统默认时区
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            zonedDateTime = Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault());
            LocalDate dateToCheck = zonedDateTime.toLocalDate();

            // 获取今天的日期
            LocalDate today = LocalDate.now();

            // 计算昨天的日期
            LocalDate yesterday = today.minusDays(1);
            return dateToCheck.equals(yesterday);
        }
        return false;
    }
    public static boolean isSameWeek(long timestamp) {
        // 将时间戳转换为LocalDate
        LocalDate date1 = null;
        LocalDate date2 = null;
        LocalDate mondayOfWeek1 = null;
        LocalDate mondayOfWeek2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date1 = Instant.ofEpochMilli(System.currentTimeMillis())
                    .atZone(ZoneId.systemDefault()) // 使用系统默认时区，或者指定其他时区
                    .toLocalDateTime()
                    .toLocalDate();
            date2 = Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault()) // 使用系统默认时区，或者指定其他时区
                    .toLocalDateTime()
                    .toLocalDate();
            mondayOfWeek1 = date1.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            mondayOfWeek2 = date2.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            return mondayOfWeek1.equals(mondayOfWeek2);
        }
        return false;
    }
    public static boolean isCurrentYear(long timestamp) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // 将时间戳转换为LocalDate
            ZonedDateTime zonedDateTime = null; // 使用系统默认时区
            zonedDateTime = Instant.ofEpochMilli(timestamp)
                    .atZone(ZoneId.systemDefault());
            LocalDate dateToCheck = zonedDateTime.toLocalDate();

            // 获取今年的年份
            int currentYear = LocalDate.now().getYear();

            // 比较年份
            return dateToCheck.getYear() == currentYear;
        }
        return false;
    }
}
