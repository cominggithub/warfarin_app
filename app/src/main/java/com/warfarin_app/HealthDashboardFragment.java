package com.warfarin_app;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.warfarin_app.data.ExamData;
import com.warfarin_app.db.DbUtil;

import java.util.ArrayList;

;

/**
 * Created by Coming on 8/5/15.
 */
public class HealthDashboardFragment extends android.support.v4.app.Fragment {


    ExamData examData;
    ImageView lowImage;
    ImageView highImage;
    ImageView normalImage;
    ImageView indicatorImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        return inflater.inflate(R.layout.health_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lowImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivLow);
        highImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivHigh);
        normalImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivNormal);
        indicatorImage = (ImageView) this.getView().findViewById(R.id.healthDashboard_ivIndicator);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        loadExamData();

        dumpPosition();
    }

    public void setArrowNormal()
    {

    }

    public void setArrayLow()
    {

    }

    public void setArrayHigh()
    {

    }

    public void loadExamDataToUI()
    {
        if (examData == null)
            return;
        if (examData.inr < 2)
            setArrayLow();
        else if(examData.inr > 4)
            setArrayHigh();
        else
            setArrowNormal();

    }
    public void loadExamData()
    {
        ArrayList<ExamData> data =new ArrayList<ExamData>();
        DbUtil.loadExamHistoryWithLimit(data, 1);

        if(data.size() > 0)
        {
            examData = data.get(0);
        }
    }

    private void dumpPosition()
    {

        dumpViewPosition("lowImage", lowImage);
        dumpViewPosition("highImage", highImage);
        dumpViewPosition("normalImage", normalImage);
        dumpViewPosition("indicatorImage", indicatorImage);
    }

    private void dumpViewPosition(String name, View v)
    {

        int pos[] = new int[4];

        v.getLocationInWindow(pos);
        Log.d("app",
                String.format("%s: x: %d, y: %d, a: %d, b: %d",
                        name,
                        pos[0],
                        pos[1],
                        pos[2],
                        pos[3]

                )
        );

        v.getLocationOnScreen(pos);
        Log.d("app",
                String.format("%s: x: %d, y: %d, a: %d, b: %d",
                        name,
                        pos[0],
                        pos[1],
                        pos[2],
                        pos[3]
                )
        );
    }
}
