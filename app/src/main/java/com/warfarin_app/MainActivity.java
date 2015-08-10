package com.warfarin_app;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.content.Context;

import com.warfarin_app.db.DbUtil;


public class MainActivity extends FragmentActivity {
    private FragmentTabHost tabHost;
    private Patient patient;
    private Context context;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        init();

        context = this.getApplicationContext();

        DbUtil.init(context);
//        tabHost.addTab(tabHost.newTabSpec("PatientFragment")
//                        .setIndicator("PatientFragment"),
//                null,
//                null);

//        tabHost.addTab(tabHost.newTabSpec("PatientFragment")
//                        .setIndicator("PatientFragment"),
//                PatientFragment.class,
//                null);

//        tabHost.addTab(tabHost.newTabSpec("aaa")
//                        .setIndicator("AAA"));


//        tabHost.addTab(tabHost.newTabSpec("Profile2")
//                        .setIndicator("PersonalProfile2"),
//                PatientFragment.class,
//                null);
//
//        tabHost.addTab(tabHost.newTabSpec("Profile3")
//                .setIndicator("PersonalProfile3"),
//                        PatientFragment.class,
//                        null);


//        FragmentTabHost mTabHost;
//        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
//        mTabHost.setup(HomeActivity.this, getSupportFragmentManager(), android.R.id.tabcontent);
//
//        mTabHost.addTab(mTabHost.newTabSpec("home").setIndicator("Home"), HomeFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("mysheets").setIndicator("MySheets"));
//        mTabHost.addTab(mTabHost.newTabSpec("bookmarks").setIndicator("Bookmarks"));

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

    private void init()
    {
        patient = new Patient();

        patient.setName("new name");

        patient.setName("lod_name");

    }
}