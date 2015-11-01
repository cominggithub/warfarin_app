package com.warfarin_app.view;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.warfarin_app.R;
import com.warfarin_app.data.ExamData;
import com.warfarin_app.data.Margin;
import com.warfarin_app.db.DbUtil;
import com.warfarin_app.transfer.ExamDataListener;

import java.util.ArrayList;

;

/**
 * Created by Coming on 8/5/15.
 */
public class HealthDashboardFragment extends android.support.v4.app.Fragment implements ExamDataListener {


    ExamData examData;
    ImageView lowImage;
    ImageView highImage;
    ImageView normalImage;
    ImageView lowIndicatorImage;
    ImageView normalIndicatorImage;
    ImageView highIndicatorImage;
    RelativeLayout mainLayout;
    TextView tvInr;


    Margin lowMargin;
    Margin highMargin;
    Margin normalMargin;

    MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity)activity;
        mainActivity.setHealthDashboardFragment(this);
        mainActivity.addExamDataListener(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.health_dashboard, container, false);
        mainLayout = (RelativeLayout)v.findViewById(R.id.healthDashboard_mainLayout);

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                dumpPosition();
                // gets called after layout has been done but before display.
                mainLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // get width and height
            }
        });



        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lowImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivLow);
        highImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivHigh);
        normalImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivNormal);
        lowIndicatorImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivIndicatorLow);
        normalIndicatorImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivIndicatorNormal);
        highIndicatorImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivIndicatorHigh);
        tvInr = (TextView) this.getView().findViewById(R.id.healthDashboard_tvInr);

        normalMargin = new Margin();

        normalMargin.left = 0;
        normalMargin.top = 0;
        normalMargin.right = 0;
        normalMargin.bottom = 0;
        setIndicatorNormal();
        refresh();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        loadExamData();

//        dumpPosition();
    }

    public void setIndicatorLow()
    {
        lowIndicatorImage.setVisibility(View.VISIBLE);
        normalIndicatorImage.setVisibility(View.INVISIBLE);
        highIndicatorImage.setVisibility(View.INVISIBLE);
    }

    public void setIndicatorNormal()
    {
        lowIndicatorImage.setVisibility(View.INVISIBLE);
        normalIndicatorImage.setVisibility(View.VISIBLE);
        highIndicatorImage.setVisibility(View.INVISIBLE);
    }

    public void setIndicatorHigh()
    {
        lowIndicatorImage.setVisibility(View.INVISIBLE);
        normalIndicatorImage.setVisibility(View.INVISIBLE);
        highIndicatorImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onExamDataReceived(ExamData d)
    {
//        examData = d;
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh();

            }
        });
    }
    public void refresh()
    {
        loadExamDataToUI();
    }
    public void loadExamDataToUI()
    {
        loadExamData();
        if (examData == null)
            return;
        if (examData.inr < 2) {
            setIndicatorLow();
        }
        else if(examData.inr > 4) {
            setIndicatorHigh();
        }
        else {
            setIndicatorNormal();
        }
        tvInr.setText("INR: " + examData.getInrStr());

    }
    public void loadExamData()
    {
        ArrayList<ExamData> data =new ArrayList<ExamData>();
        DbUtil.loadExamHistoryWithLimit(data, 1, null);

        if(data.size() > 0)
        {
            examData = data.get(0);
        }
    }

     void dumpPosition()
    {

//        dumpViewPosition("lowImage", lowImage);
//        dumpViewPosition("highImage", highImage);
//        dumpViewPosition("normalImage", normalImage);

    }


//    private void updateSizeInfo() {
//        RelativeLayout rl_cards_details_card_area = (RelativeLayout) findViewById(R.id.rl_cards_details_card_area);
//        w = rl_cards_details_card_area.getWidth();
//        h = rl_cards_details_card_area.getHeight();
//        Log.v("W-H", w+"-"+h);
//    }


//    public void dumpViewPosition(String name, View v)
//    {
//
//        int pos[] = new int[4];
//
//        v.getLocationInWindow(pos);
//        Log.d("app",
//                String.format("%s: x: %d, y: %d, a: %d, b: %d",
//                        name,
//                        pos[0],
//                        pos[1],
//                        pos[2],
//                        pos[3]
//
//                )
//        );
//
//        v.getLocationOnScreen(pos);
//        Log.d("app",
//                String.format("%s: x: %d, y: %d, a: %d, b: %d",
//                        name,
//                        pos[0],
//                        pos[1],
//                        pos[2],
//                        pos[3]
//                )
//        );
//
//        Log.d("app",
//                String.format("%s: left:%d, top:%d, width:%d, height: %d",
//                        name,
//                        v.getLeft(),
//                        v.getTop(),
//                        v.getWidth(),
//                        v.getRight()
//
//                )
//        );
//
//        Log.d("app",
//                String.format("%s: x:%f, y:%f, width:%d, height: %d",
//                        name,
//                        v.getX(),
//                        v.getY(),
//                        v.getWidth(),
//                        v.getRight()
//
//                )
//        );

//    }
}

