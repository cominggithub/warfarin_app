package com.warfarin_app.util;

import com.warfarin_app.LogMsgConsumer;
import com.warfarin_app.data.LogData;
import com.warfarin_app.db.DbUtil;

import java.util.ArrayList;

/**
 * Created by Coming on 9/6/15.
 */
public class LogUtil {

    private static ArrayList<LogMsgConsumer> logConsumers = new ArrayList<>();
    public static void addLogMsgConsumer(LogMsgConsumer c)
    {
        if (!logConsumers.contains((c)))
            logConsumers.add(c);
    }

    private static void notifyAppendMsg(String s)
    {
        for (LogMsgConsumer c : logConsumers)
        {
            c.appendMsg(s);
        }
    }

    public static void appendMsg(String s)
    {
        DbUtil.addLog(new LogData(s));
        notifyAppendMsg(s);
    }
}
