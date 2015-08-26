package com.warfarin_app.transfer;
import com.warfarin_app.data.ExamData;
/**
 * Created by Coming on 8/26/15.
 */
public interface TransferState {
    public String action();
    public ExamData getExamData();

}
