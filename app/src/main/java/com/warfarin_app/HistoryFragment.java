package com.warfarin_app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import java.util.ArrayList;

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


        for(int i=0; i<10; i++){
            HashMap<String,String> item = new HashMap<String,String>();
            item.put("date", "date"+i);
            item.put("time", "time"+i);
            item.put("inr", ""+i);
            examDataList.add( item );
        }

        String[] fields = {"date", "time", "inr"};
        int[] views = {R.id.exam_list_entry_tvDate, R.id.exam_list_entry_tvTime, R.id.exam_list_entry_tvInr};

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity().getBaseContext(),
                examDataList,
                R.layout.exam_list_entry,
                fields,
                views
        );


        listview  = (ListView) this.getView().findViewById(R.id.history_lvExamHistoryList);
        listview.setAdapter(adapter);
    }
}
