package com.warfarin_app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Coming on 8/19/15.
 */
public class LogFragment extends android.support.v4.app.Fragment implements LogMsgConsumer {
    LogMsgProvider msgProvider;
    String logMsg;
    private TextView tvMsg;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        logMsg = "";
        MainActivity mainActivity = (MainActivity)activity;
        mainActivity.addLogMsgConsumer(this);
        Log.d("app", "onAttach");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.log, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
        tvMsg = (TextView)this.getView().findViewById(R.id.log_tvMsg);
    }

    public void updateLogMessage()
    {
        tvMsg.setText(logMsg, TextView.BufferType.NORMAL);
    }


    public void appendMsg(String s)
    {
        logMsg += s + "\n" + logMsg;
        updateLogMessage();
    }
}
