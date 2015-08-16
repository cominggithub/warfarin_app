package com.warfarin_app;


import android.os.Bundle;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.health_dashboard, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadExamData();
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
}
