package com.warfarin_app.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Coming on 9/6/15.
 */
public class LogData {
    public long id;
    public long date;
    public String msg;

    public LogData(String s)
    {
        msg = s;
        date = new Date().getTime();
    }
    public String getDataString()
    {
        SimpleDateFormat format;
        format = new SimpleDateFormat("hh:mm:ss.SSS");

        return format.format(date);
    }
}
