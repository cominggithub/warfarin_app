package com.warfarin_app.view;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.warfarin_app.R;
import com.warfarin_app.data.Patient;
import com.warfarin_app.db.DbUtil;
import com.warfarin_app.transfer.BTManager;
import com.warfarin_app.transfer.BTUtil;
import com.warfarin_app.transfer.ExamDataListener;
import com.warfarin_app.util.DateUtil;
import com.warfarin_app.util.SystemInfo;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost tabHost;
    private Context context;


    private HealthDashboardFragment healthDashboardFragment;
    private BTManager btManager;
    private Patient patient;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("app", "MaiActivity onCreate");
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);



        tabHost.setup(MainActivity.this, getSupportFragmentManager(), R.id.realtabcontent);

        //1

//        tabHost.addTab(tabHost.newTabSpec("Bluetooth")
//                        .setIndicator("藍芽設定"),
//                BlueDevListFragment.class,
//                null);

        tabHost.addTab(tabHost.newTabSpec("User")
                        .setIndicator("使用者資料"),
                PatientFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("Exam")
                        .setIndicator("檢測結果"),
                ExamFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("Health Dashboard")
                        .setIndicator("健康指南"),
                HealthDashboardFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("Food Guide")
                        .setIndicator("飲食指南"),
                FoodGuideFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("History")
                        .setIndicator("歷史記錄"),
                HistoryFragment.class,
                null);

        tabHost.addTab(tabHost.newTabSpec("Log")
                        .setIndicator("除錯"),
                LogFragment.class,
                null);

        TextView title;

        title = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        title.setSingleLine(false);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        title = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        title.setSingleLine(false);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        title = (TextView) tabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        title.setSingleLine(false);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        title = (TextView) tabHost.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
        title.setSingleLine(false);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        title = (TextView) tabHost.getTabWidget().getChildAt(4).findViewById(android.R.id.title);
        title.setSingleLine(false);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        init();

        dumpSystemInfo();
    }

    public void dumpSystemInfo()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Log.d("app", String.format("screen %d x %d\n", width, height));

        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;


        Log.d("app", String.format("screen dp %.0f x %.0f\n", dpWidth, dpHeight));
        Log.d("app", String.format("screen density %.0f\n", density));

        Log.d("app", Build.FINGERPRINT);
        Build.FINGERPRINT.startsWith("generic");

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

    private void init()
    {
        context = this.getApplicationContext();

        SystemInfo.setContext(getBaseContext());
        initDb();
        initBt();
    }


    public void initDb()
    {
        patient = new Patient();
        patient.setName("尚未設定");
        patient.setGender(true);
        patient.setBirthday("1999/01/02");
        patient.setDoctor("尚未設定");
        patient.setIsWarfarin(true);
        patient.setBlueDevName("尚未設定");

        DbUtil.init(context);
        DbUtil.cleanExamData();
        DbUtil.cleanLogData();
        DbUtil.loadPatient(patient);

        DateUtil.dumpWeek();
    }

    public void initBt()
    {

        if (Build.FINGERPRINT.startsWith("generic"))
        {
            SystemInfo.isBluetooth = false;
        }
        if (SystemInfo.isBluetooth) {
            BTUtil.setMainActivity(this);

            if (btManager == null)
            {
                btManager = BTManager.getInstance(this);
            }

            BTManager.setDeviceByAddress(patient.getBlueDevAddress());

            if (!btManager.isRunning()) {
                btManager.start();
            }
        }
    }

    public void addExamDataListener(ExamDataListener listener)
    {
        if (btManager != null) {
            btManager.addExamDataListener(listener);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (SystemInfo.isBluetooth) {
            if (btManager != null && btManager.isRunning() == true) {
                btManager.close();
            }
            BTUtil.close();
        }
    }
}