package com.warfarin_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.warfarin_app.data.ExamData;
import com.warfarin_app.db.DbUtil;

import java.util.ArrayList;

/**
 * Created by Coming on 8/5/15.
 */
public class ExamFragment extends android.support.v4.app.Fragment {

    ExamData currentExam;
    ExamData lastExam;

    TextView tvExamDate;
    TextView tvPt;
    TextView tvInr;
    EditText etWarfarin;
    TextView tvLastExamDate;
    TextView tvLastExamResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentExam = null;
        lastExam = null;
        return inflater.inflate(R.layout.exam, container, false);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("app", "onPauss ExamFrag");
        saveData();
    }

    public void saveData()
    {
        Log.d("app", "saveData");
        if (currentExam == null)
            return;
        loadExamFromUI();
        DbUtil.updateExam(currentExam);
    }

    public void setData(ExamData current, ExamData last)
    {
        currentExam = current;
        lastExam = last;
        loadExamToUI();

    }

    public void referesh()
    {
        ArrayList<ExamData> data = new ArrayList<>();
        DbUtil.loadExamHistoryWithLimit(data, 2);
        setData(data.get(0), data.get(1));
    }

    public void loadExamToUI()
    {
        tvExamDate.setText(currentExam.getDateStr());
        tvPt.setText(currentExam.getPtStr());
        tvInr.setText(currentExam.getInrStr());
        etWarfarin.setText(currentExam.getWarfarinStr());

        if (lastExam != null) {
            tvLastExamDate.setText(lastExam.getDateStr());
            tvLastExamResult.setText("PT: " + lastExam.getPtStr() + " INR: " + lastExam.getWarfarinStr());
        }
    }

    public boolean isDataChanged()
    {
        if (currentExam == null)
            return false;
        return !etWarfarin.getText().equals(currentExam.getWarfarinStr());
    }
    public void loadExamFromUI()
    {
        if (isDataChanged())
        {
            currentExam.warfarin = Double.parseDouble(etWarfarin.getText().toString());
        }
    }

}