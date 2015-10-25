package com.warfarin_app.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.warfarin_app.R;
import com.warfarin_app.data.ExamData;
import com.warfarin_app.db.DbUtil;
import com.warfarin_app.transfer.ExamDataListener;
import com.warfarin_app.util.SystemInfo;

import java.util.ArrayList;
import java.util.HashMap;

//import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition.RIGHT_BOTTOM;

/**
 * Created by Coming on 8/10/15.
 */
public class HistoryFragment extends android.support.v4.app.Fragment implements ExamDataListener
{

    ListView listview;

    ArrayList<HashMap<String,String>> examDataList = new ArrayList<HashMap<String,String>>();
    LineChart mChart;
    ArrayList<ExamData> data;
    MainActivity mainActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity)activity;
        mainActivity.addExamDataListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("app", "onCreateView");
        return inflater.inflate(R.layout.history, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listview  = (ListView) this.getView().findViewById(R.id.history_lvExamHistoryList);
        Log.d("app", "onActivityCreated");
        initChart();
        refresh();
    }

    public void refresh()
    {
        Log.d("app", "refresh start");
        Log.d("bt", "refresh start");
        loadExamDataFromDb();
        loadExamDataToListView();
        refreshChartData();
        Log.d("app", "refresh done");
        Log.d("bt", "refresh done");
    }

    public void loadExamDataToListView()
    {
        examDataList.clear();
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
//                getActivity().getBaseContext(),
                mainActivity.getBaseContext(),
                examDataList,
                R.layout.exam_list_entry,
                fields,
                views
        );

        listview.setAdapter(null);
        listview.setAdapter(adapter);
    }


    public void loadExamDataFromDb()
    {
        data = new ArrayList<>();

        if (SystemInfo.isBluetooth)
        {
            DbUtil.loadExamHistory(data);

        }
        else
        {
            DbUtil.cleanExamData();
            DbUtil.insertExamHistorySample();
            DbUtil.loadExamHistory(data);
        }
    }

    public void initChart()
    {
        mChart = (LineChart) this.getView().findViewById(R.id.chart);
        mChart.setDrawGridBackground(false);

        mChart.setDescription("");
        mChart.setNoDataTextDescription("No Data");

//        mChart.setHighlightEnabled(true);

        // x-axis limit line
//        LimitLine llXAxis = new LimitLine(10f, "Index 10");
//        llXAxis.setLineWidth(4f);
//        llXAxis.enableDashedLine(10f, 10f, 0f);
////        llXAxis.setLabelPosition(LimitLabelPosition.POS_LEFT);
//        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(20);
//        xAxis.addLimitLine(llXAxis);

//        LimitLine ll1 = new LimitLine(130f, "Upper Limit");
//        ll1.setLineWidth(4f);
//        ll1.enableDashedLine(10f, 10f, 0f);
////        ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
//        ll1.setTextSize(10f);
//
//        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
////        ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
//        ll2.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
//        leftAxis.setAxisMaxValue(5f);
//        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setTextSize(20);

        // limit lines are drawn behind data (and not on top)
//        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        // add data
//        setExamData(45, 100);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

//        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
//        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();

        refreshChartData();
    }

    private void refreshChartData()
    {
        ArrayList<String> xVals = new ArrayList<String>();
        if (data == null)
            return;
        for (int i = 0; i < data.size(); i++) {
//        for (int i = data.size()-1; i >= 0; i--) {
            xVals.add(data.get(i).getDateStr());
        }

        // pt
        ArrayList<Entry> ptVals = new ArrayList<Entry>();

        for (int i = 0; i < data.size(); i++) {
            ptVals.add(new Entry((float)data.get(i).pt, data.size() - i-1));
        }

        // create a dataset and give it a type
        LineDataSet ptSet = new LineDataSet(ptVals, "PT");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        ptSet.enableDashedLine(10f, 5f, 0f);
        ptSet.setColor(Color.GREEN);
        ptSet.setCircleColor(Color.GREEN);
        ptSet.setLineWidth(3f);
        ptSet.setCircleSize(6f);
        ptSet.setDrawCircleHole(false);
        ptSet.setValueTextSize(20f);
        ptSet.setFillAlpha(65);
        ptSet.setFillColor(Color.BLACK);

        // inr
        ArrayList<Entry> inrVals = new ArrayList<Entry>();

        for (int i = 0; i < data.size(); i++) {

            inrVals.add(new Entry((float)data.get(i).inr, data.size() - i-1));
        }

        // create a dataset and give it a type
        LineDataSet inrSet = new LineDataSet(inrVals, "INR");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        inrSet.enableDashedLine(10f, 5f, 0f);
        inrSet.setColor(Color.BLUE);
        inrSet.setCircleColor(Color.BLUE);
        inrSet.setLineWidth(3f);
        inrSet.setCircleSize(6f);
        inrSet.setDrawCircleHole(false);
        inrSet.setValueTextSize(20f);
        inrSet.setFillAlpha(65);
        inrSet.setFillColor(Color.BLACK);

        // warfarin
        ArrayList<Entry> warfarinVals = new ArrayList<Entry>();

        for (int i = 0; i < data.size(); i++) {

            warfarinVals.add(new Entry((float)data.get(i).warfarin, data.size() - i-1));
        }

        // create a dataset and give it a type
        LineDataSet warfarinSet = new LineDataSet(warfarinVals, "INR");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        warfarinSet.enableDashedLine(10f, 5f, 0f);
        warfarinSet.setColor(Color.RED);
        warfarinSet.setCircleColor(Color.RED);
        warfarinSet.setLineWidth(3f);
        warfarinSet.setCircleSize(6f);
        warfarinSet.setDrawCircleHole(false);
        warfarinSet.setValueTextSize(20f);
        warfarinSet.setFillAlpha(65);
        warfarinSet.setFillColor(Color.BLACK);

//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(ptSet); // add the datasets
        dataSets.add(inrSet); // add the datasets
        dataSets.add(warfarinSet); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);

        mChart.invalidate();

    }

    private void setExamData()
    {

    }
    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 3;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "PT");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);
    }

    @Override
    public void onExamDataReceived(ExamData d) {

        Log.d("app", "onExamDataReceived " + d.toString());
        Log.d("bt", "onExamDataReceived " + d.toString());
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                refresh();

            }
        });
    }
}
