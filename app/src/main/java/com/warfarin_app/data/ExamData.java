package com.warfarin_app.data;

import com.warfarin_app.SysUtil;

import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;
/**
 * Created by Coming on 8/9/15.
 */
public class ExamData {

    public long id;
    public double pt = 0.0;
    public double inr = 0.0;
    public long date = 0;
    public double warfarin = 0.0;
    private Locale locale;

    public ExamData()
    {
        date = new Date().getTime();
    }

    public String getDateStr()
    {

        DateFormat shortDateFormat =
                DateFormat.getDateInstance(
                        DateFormat.SHORT, SysUtil.getLocal());


        Date d = new Date();
        d.setTime(date);
        return shortDateFormat.format(d);
    }

    public String getTimeStr()
    {

        DateFormat shortDateFormat =
                DateFormat.getTimeInstance(
                        DateFormat.SHORT, SysUtil.getLocal());

        Date d = new Date();
        d.setTime(date);
        return shortDateFormat.format(d);
    }

    public String getPtStr()
    {

        return String.format("%.2f", pt);

    }

    public String getInrStr()
    {

        return String.format("%.2f", inr);
    }

    public String getWarfarinStr()
    {

        return String.format("%.2f", warfarin);
    }

    public static ExamData parseBytes(byte[] bytes)
    {

        return null;
    }


}
