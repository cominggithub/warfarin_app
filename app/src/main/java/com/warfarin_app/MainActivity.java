package com.warfarin_app;


import android.content.Context;
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

import com.warfarin_app.db.DbUtil;
import com.warfarin_app.transfer.BTManager;
import com.warfarin_app.transfer.BTUtil;
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

        tabHost.addTab(tabHost.newTabSpec("Exam")
                        .setIndicator("Exam"),
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

        context = this.getApplicationContext();

        DbUtil.init(context);
        DbUtil.deleteDb();
//        DbUtil.cleanDb();
        BTUtil.setMainActivity(this);
        if (BTUtil.hasBluetoothCapability())
        {
            btManager = new BTManager();
            btManager.start();
        }


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
        patient = new Patient();
        patient.setName("new name");
        patient.setName("lod_name");

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
        if (healthDashboardFragment != null)
        {
            Log.d("app", "WwWWwWWWwwwww");
            healthDashboardFragment.dumpPosition();
        }


    }




}