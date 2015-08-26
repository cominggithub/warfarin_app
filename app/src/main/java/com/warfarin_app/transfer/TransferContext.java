package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/26/15.
 */

import com.warfarin_app.data.ExamData;

public class TransferContext {

    private TransferState state;
    public void setState(TransferState s)
    {
        state = s;
    }
    public TransferState getState()
    {
        return state;
    }

    public ExamData getExamData()
    {
        return state.getExamData();
    }

    public String action()
    {
        return state.action();
    }
}
