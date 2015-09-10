package com.warfarin_app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.warfarin_app.data.LogData;
import com.warfarin_app.db.DbUtil;
import com.warfarin_app.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by Coming on 8/19/15.
 */
public class LogFragment extends android.support.v4.app.Fragment implements LogMsgConsumer, View.OnClickListener{
    LogMsgProvider msgProvider;
    String logMsg;
    private TextView tvMsg;
    MainActivity mainActivity;
    private Button btStop;
    boolean enableLogging = true;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        logMsg = "";
        mainActivity = (MainActivity)activity;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.log, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
        tvMsg = (TextView)this.getView().findViewById(R.id.log_tvMsg);
        LogUtil.addLogMsgConsumer(this);

        btStop = (Button) this.getView().findViewById(R.id.log_btStop);
        btStop.setOnClickListener(this);
        setStartLogging();
        updateLogMessage();

    }

    public void updateLogMessage()
    {
        if (!enableLogging)
            return;

        if (tvMsg != null) {
            StringBuilder sb = new StringBuilder();
            ArrayList<LogData> logList = new ArrayList<>();
            DbUtil.loadLogHistory(logList, 200);
//            for(int i=logList.size()-1; i>=0; i-- )
            for(int i=0; i<logList.size(); i++)
            {
                sb.append(logList.get(i).getDataString() + " " + logList.get(i).msg + "\n");

            }
            tvMsg.setText(sb.toString());
            tvMsg.invalidate();
        }
    }


    public void appendMsg(final String s)
    {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            updateLogMessage();

            }
        });
    }


    @Override
    public void onClick(View v)
    {
        if (v == btStop)
        {
            if(btStop.getText().toString().equals("Start"))
            {
                setStartLogging();
            }
            else
            {
                setStopLogging();
            }
        }
    }

    public void setStopLogging()
    {
        btStop.setText("Start");
        btStop.setBackgroundColor(Color.GREEN);
        enableLogging = false;
        btStop.invalidate();
    }

    public void setStartLogging()
    {
        btStop.setText("Stop");
        btStop.setBackgroundColor(Color.RED);
        enableLogging = true;
        btStop.invalidate();
    }
}
