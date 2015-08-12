package com.warfarin_app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.warfarin_app.data.ExamData;
import com.warfarin_app.db.DbUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Coming on 8/10/15.
 */
public class HistoryFragment extends android.support.v4.app.Fragment
{

    ListView listview;

    ArrayList<HashMap<String,String>> examDataList = new ArrayList<HashMap<String,String>>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("app", "onCreateView");
        return inflater.inflate(R.layout.history, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("app", "onActivityCreated");

        ArrayList<ExamData> data = new ArrayList<ExamData>();

        DbUtil.loadExamHistory(data);
        if (data.size() == 0)
        {
            DbUtil.insertExamHistorySample();
        }

        DbUtil.loadExamHistory(data);
        for(int i=0; i<data.size(); i++){
            HashMap<String,String> item = new HashMap<String,String>();
            item.put("date", data.get(i).getDateStr());
            item.put("time", data.get(i).getTimeStr());
            item.put("pt", "" + data.get(i).getPtStr());
            item.put("inr", "" + data.get(i).getInrStr());
            item.put("warfarin", "" + data.get(i).getWarfarinStr());
            examDataList.add( item );
        }

        String[] fields = {"date", "time", "pt", "inr", "warfarin"};
        int[] views = {
                R.id.exam_list_entry_tvDate,
                R.id.exam_list_entry_tvTime,
                R.id.exam_list_entry_tvPt,
                R.id.exam_list_entry_tvInr,
                R.id.exam_list_entry_tvWarfarin
        };

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity().getBaseContext(),
                examDataList,
                R.layout.exam_list_entry,
                fields,
                views
        );


        listview  = (ListView) this.getView().findViewById(R.id.history_lvExamHistoryList);
        listview.setAdapter(adapter);

        ExamData dd = new ExamData();
        Log.d("app", dd.getDateStr() + " " + dd.getTimeStr());
    }
}
