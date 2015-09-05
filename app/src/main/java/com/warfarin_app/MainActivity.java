package com.warfarin_app;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;

import com.warfarin_app.db.DbUtil;
import com.warfarin_app.transfer.BTManager;
import com.warfarin_app.transfer.ExamDataListener;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost tabHost;
    private Patient patient;
    private Context context;


    private HealthDashboardFragment healthDashboardFragment;
    private BTManager btManager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SysUtil.setContext(getBaseContext());
        setContentView(R.layout.activity_main);



        tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

        tabHost.setup(MainActivity.this, getSupportFragmentManager(), R.id.realtabcontent);

        //1

        tabHost.addTab(tabHost.newTabSpec("User")
                        .setIndicator("User"),
                PatientFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("exam")
                        .setIndicator("exam"),
                ExamFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("Health Dashboard")
                        .setIndicator("Health Dashboard"),
                HealthDashboardFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("Food Guide")
                        .setIndicator("Food Guide"),
                FoodGuideFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("History")
                        .setIndicator("History"),
                HistoryFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("Log")
                        .setIndicator("Log"),
                LogFragment.class,
                null);

        init();

        context = this.getApplicationContext();

        DbUtil.init(context);
        DbUtil.cleanDb();
        btManager = new BTManager();
        btManager.start();

    }

    /**************************
     *
     * 給子頁籤呼叫用
     *
     **************************/
    public String getPersonalProfileData(){
        return "oh shit";
    }
    public String getGoogleData(){
        return "Google 456";
    }
    public String getFacebookData(){
        return "Facebook 789";
    }
    public String getTwitterData(){
        return "Twitter abc";
    }

    public void setHealthDashboardFragment(HealthDashboardFragment h)
    {
        healthDashboardFragment = h;
    }

    public LogMsgProvider getLogMsgProvider()
    {
        return (LogMsgProvider)btManager.getLogMsgProvider();
    }

    private void init()
    {
        patient = new Patient();

        patient.setName("new name");

        patient.setName("lod_name");

    }

    public void addExamDataListener(ExamDataListener listener)
    {
        btManager.addExamDataListener(listener);
    }

    public void addLogMsgConsumer(LogMsgConsumer c)
    {
        btManager.addLogMsgConsumer(c);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub

        super.onWindowFocusChanged(hasFocus);
        if (healthDashboardFragment != null)
        {
            Log.d("app", "WwWWwWWWwwwww");
            healthDashboardFragment.dumpPosition();
        }


    }




}