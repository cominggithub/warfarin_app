package com.warfarin_app.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.warfarin_app.R;
import com.warfarin_app.data.ExamData;
import com.warfarin_app.db.DbUtil;
import com.warfarin_app.transfer.ExamDataListener;

import java.util.ArrayList;

/**
 * Created by Coming on 8/5/15.
 */
public class ExamFragment extends android.support.v4.app.Fragment implements ExamDataListener {

    ExamData currentExam;
    ExamData lastExam;

    TextView tvExamDate;
    TextView tvPt;
    TextView tvInr;
    EditText etWarfarin;
    TextView tvLastExamDate;
    TextView tvLastExamResult;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentExam = null;
        lastExam = null;


        return inflater.inflate(R.layout.exam, container, false);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity)activity;
        mainActivity.addExamDataListener(this);
//        value = mainActivity.getPersonalProfileData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvExamDate = (TextView) this.getView().findViewById(R.id.exam_tvExamDate);
        tvPt = (TextView) this.getView().findViewById(R.id.exam_tvPt);
        tvInr = (TextView) this.getView().findViewById(R.id.exam_tvInr);
        etWarfarin = (EditText) this.getView().findViewById(R.id.exam_etWarfarin);
        tvLastExamDate = (TextView) this.getView().findViewById(R.id.exam_tvLastExamDate);
        tvLastExamResult = (TextView) this.getView().findViewById(R.id.exam_tvLastExamResult);

        refresh();

        Log.d("app", "Exam onActivityCreated");
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

    public void refresh()
    {
        Log.d("app", "refresh exam fragment");

        ArrayList<ExamData> data = new ArrayList<>();
        DbUtil.loadExamHistoryWithLimit(data, 2);

        if (data.size() == 2) {
            setData(data.get(0), data.get(1));
        }
        else if(data.size() == 1)
        {
            setData(data.get(0), null);
        }

    }

    public void loadExamToUI()
    {
        if (currentExam == null)
            return;

        tvExamDate.setText(currentExam.getDateStr());
        tvPt.setText(currentExam.getPtStr());
        tvInr.setText(currentExam.getInrStr());
        etWarfarin.setText(currentExam.getWarfarinStr());

        if (lastExam == null)
            return;

        tvLastExamDate.setText(lastExam.getDateStr());
        tvLastExamResult.setText("PT: " + lastExam.getPtStr() + " INR: " + lastExam.getInrStr());

    }

    public boolean isDataChanged()
    {
        String s;
        if (currentExam == null)
            return false;
        s = etWarfarin.getText().toString();

        if ( s == null)
            return false;
        return !s.equals(currentExam.getWarfarinStr());
    }
    public void loadExamFromUI()
    {
        if (isDataChanged())
        {
            currentExam.warfarin = Double.parseDouble(etWarfarin.getText().toString());
        }
    }

    @Override
    public void onExamDataReceived(ExamData d)
    {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                refresh();

            }
        });
    }
}