package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/25/15.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.warfarin_app.LogMsgConsumer;
import com.warfarin_app.LogMsgProvider;
import com.warfarin_app.data.ExamData;
public class BTManager extends Thread {
    private ExamDataReceiver examDataReceiver;
    private boolean alive = true;
    private BluetoothDevice device;
    private static final String BLUETEH = "BOLUTEK";
    private static boolean hasData = false;
    private static DataReadSignal dataReadSignal = new DataReadSignal();
    private static BTConnectionHandler btConnectionHandler;
    private static ExamData data;
    public void run() {

        while(alive)
        {
            if (!isBtHandlerStarted())
            {
                startBtHandler();
            }

            data = recvExamData();
            if (data != null)
            {
                examDataReceiver.notifyExamDataReceived(data);
            }
        }

    }

    public void startBtHandler()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pairBTDevice();

        btConnectionHandler = new BTConnectionHandler(device, mBluetoothAdapter);
        btConnectionHandler.start();
    }

    public boolean isBtHandlerStarted()
    {

        if(btConnectionHandler == null)
            return false;

        return btConnectionHandler.isRunning();
    }
    public void stopThread()
    {
        alive = false;
    }
    public boolean pairBTDevice()
    {
        if (device == null)
        {
            device = BTUtil.scan_device(BLUETEH);
        }

        if (device == null)
            return false;

        examDataReceiver.logBTPaired();

        return true;
    }

    public ExamData recvExamData()
    {

        if (dataReadSignal.hasDataToProcess)
        {
            return btConnectionHandler.getExamData();
        }

        return null;
    }


    public LogMsgProvider getLogMsgProvider()
    {
        return examDataReceiver;
    }

    public void addExamDataListener(ExamDataListener listener)
    {
        examDataReceiver.addExamDataListener(listener);
    }

    public void addLogMsgConsumer(LogMsgConsumer c)
    {
        examDataReceiver.addLogMsgConsumer(c);
    }
}