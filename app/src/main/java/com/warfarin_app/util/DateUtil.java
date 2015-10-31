package com.warfarin_app.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormatSymbols;

/**
 * Created by Coming on 10/31/15.
 */
public class DateUtil {

    public static long getTimeBy(int year, int month, int day, int hour, int min, int sec)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return df.parse(String.format("%d-%d-%d %d:%d:%d", year, month, day, hour, min, sec)).getTime();
        }catch (Exception e)
        {
            Log.e("app", "exception", e);
        }

        return 0;
    }

    public static long getTimeByString(String dateTime)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return df.parse(dateTime).getTime();

        }catch (Exception e)
        {
            Log.e("app", "exception", e);
        }

        return 0;
    }

    public static int getWeek(long date)
    {
        Calendar cal = Calendar.getInstance();
        Date d = new Date();
        d.setTime(date);
        cal.setTime(d);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static long getTimeByWeek(String year, int week)
    {
        return getDateByWeek(year, week).getTime();
    }

    public static Date getDateByWeek(String year, int week)
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Log.d("app", formatter.format(cal.getTime()));
        return cal.getTime();
    }

    public static long getTimeByYearMonth(String year, String month)
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Log.d("app", formatter.format(cal.getTime()));
        return cal.getTime().getTime();
    }

    public static void dumpWeek()
    {
        for (int i=1; i<53;i++) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(Calendar.YEAR, 2015);
            cal.set(Calendar.WEEK_OF_YEAR, i);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

//            Log.d("app", formatter.format(cal.getTime()) + ", week: " + cal.get(Calendar.WEEK_OF_YEAR));
        }
    }

    public static String getYearByTime(long time)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy 年");
        Date d = new Date();
        d.setTime(time);
        return formatter.format(d);
    }

    public static String getMonthByTime(long time)
    {
        Calendar cal = Calendar.getInstance();
        Date d = new Date();
        d.setTime(time);
        cal.setTime(d);
        return (cal.get(Calendar.MONTH)+1) + "月";
    }

    public static String getWeekByTime(long time)
    {
        int week = getWeek(time);
        return "第" + week + "週";
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }


}
