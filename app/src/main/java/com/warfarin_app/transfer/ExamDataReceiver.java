package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/19/15.
 */

import android.util.Log;

import com.warfarin_app.view.LogMsgConsumer;
import com.warfarin_app.data.ExamData;
import com.warfarin_app.db.DbUtil;
import com.warfarin_app.util.LogUtil;

import java.util.ArrayList;

public class ExamDataReceiver {

    ArrayList<ExamDataListener> listeners;

    ArrayList<LogMsgConsumer> logConsumers;


    public ExamDataReceiver()
    {

        listeners = new ArrayList<>();
        logConsumers = new ArrayList<>();
    }

    public void addExamDataListener(ExamDataListener r)
    {
        if (!listeners.contains(r))
            listeners.add(r);
    }

    public void addLogMsgConsumer(LogMsgConsumer c)
    {

        if (!logConsumers.contains((c)))
            logConsumers.add(c);
    }

    public void notifyExamDataReceived(ExamData d)
    {
        if (d == null)
            return;

        DbUtil.saveExamData(d);
        LogUtil.appendMsg("received: " + d.toString());
        Log.d("app", "notify receive: " + d.toString());
        Log.d("bt", "notify receive: " + d.toString());
        for (ExamDataListener r : listeners)
        {
            r.onExamDataReceived(d);
        }
    }
    private void notifyAppendMsg(String s)
    {
        for (LogMsgConsumer c : logConsumers)
        {
            c.appendMsg(s);
        }
    }
}
