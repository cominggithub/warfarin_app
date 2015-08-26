package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/19/15.
 */

import com.warfarin_app.LogMsgConsumer;
import com.warfarin_app.LogMsgProvider;
import com.warfarin_app.data.ExamData;

import java.util.ArrayList;

public class ExamDataReceiver implements LogMsgProvider {

    ArrayList<ExamDataListener> listeners;

    ArrayList<LogMsgConsumer> logConsumers;


    String logMsg;
    public ExamDataReceiver()
    {
        listeners = new ArrayList<>();
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

    public void appendMsg(String s)
    {
        logMsg += logMsg + "\n" + s;
        notifyAppendMsg(s);
    }


    public void logBTPaired()
    {
        appendMsg("paired");
    }

    public void logCRCError()
    {
        appendMsg("crc error");
    }

    public void logRecvBytes(byte data[])
    {
        StringBuilder sb;
        sb = new StringBuilder();
        for (byte b : data)
        {
            sb.append(String.format("%d ", b));
        }
    }
    public void logExamData(ExamData d)
    {

    }

    @Override
    public String getLogMsg()
    {
        return logMsg;
    }
}
