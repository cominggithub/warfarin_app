package com.warfarin_app.data;

/**
 * Created by Coming on 8/9/15.
 */
public class ExamData {

    public double pt = 0.0;
    public double inr = 0.0;
    public long date = 0;
    public double warfarin = 0.0;

    public String getDateStr()
    {
        return "" + date;
    }

    public String getTimeStr()
    {
        return "" + date;
    }

    public String getPtStr()
    {
        return "" + pt;
    }

    public String getInrStr()
    {
        return "" + inr;
    }

    public String getWarfarin()
    {
        return "" + warfarin;
    }


}
