package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/26/15.
 */
public class DataReadSignal {
    protected boolean hasDataToProcess = false;

    public synchronized boolean hasDataToProcess(){
        return this.hasDataToProcess;
    }

    public synchronized void setHasDataToProcess(boolean hasData){
        this.hasDataToProcess = hasData;
    }
}
