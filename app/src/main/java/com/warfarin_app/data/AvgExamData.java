package com.warfarin_app.data;

import java.util.List;

/**
 * Created by Coming on 10/31/15.
 */
public class AvgExamData {

    private AvgMode mode = AvgMode.RECENT;
    private List<ExamData> dataList;
    private List<ExamData> avgDataList;
    public void setAvgMode(AvgMode mode)
    {
        this.mode = mode;

    }

    public void setExamDataList(List<ExamData> list)
    {
        dataList = list;
    }

    public String getX(int index)
    {
        return "";
    }

    public double getPt(int index)
    {
        return avgDataList.get(index).pt;
    }

    public double getInr(int index)
    {
        return avgDataList.get(index).inr;
    }

    public double getWafarin(int index)
    {
        return avgDataList.get(index).warfarin;
    }

    public int size()
    {
        return avgDataList.size();
    }
}
