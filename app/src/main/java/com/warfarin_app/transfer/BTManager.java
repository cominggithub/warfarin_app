package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/25/15.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.warfarin_app.data.ExamData;
import com.warfarin_app.util.LogUtil;

public class BTManager extends Thread {
    private ExamDataReceiver examDataReceiver;
    private boolean alive = true;
    private BluetoothDevice device;
    private static final String BLUETEH = "BOLUTEK";
    private static boolean hasData = false;
    private static DataReadSignal dataReadSignal = new DataReadSignal();
    private static BTConnectionHandler btConnectionHandler;
    private static ExamData data;
    private static int BTScanInterval = 20000;

    public BTManager()
    {
        examDataReceiver = new ExamDataReceiver();
    }
    public void run() {
        Log.d("bt", "bt manager start");
        LogUtil.appendMsg("Start Bluetooth Manager");


        while(alive)
        {
            if (!pairBTDevice()) {
                try {
                    Thread.sleep(BTScanInterval);
                }catch (Exception e)
                {
                    Log.e("bt", "exception", e);
                }

            }
            else {

                if (!isBtHandlerStarted()) {
                    startBtHandler();
                }

                data = recvExamData();
                if (data != null) {
                    examDataReceiver.notifyExamDataReceived(data);
                }
            }
        }

        LogUtil.appendMsg("Stop Bluetooth Manager");
    }

    public void startBtHandler() {
        Log.d("bt", "startBtHandler");

        LogUtil.appendMsg("Start Bluetooth Connection Handler");

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        btConnectionHandler = new BTConnectionHandler(device, mBluetoothAdapter);
        btConnectionHandler.setDataReadSignal(dataReadSignal);
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

        return true;
    }

    public ExamData recvExamData()
    {

        synchronized (dataReadSignal)
        {
            try
            {
                dataReadSignal.wait();
                if (dataReadSignal.hasDataToProcess) {
                    dataReadSignal.setHasDataToProcess(false);
                    return btConnectionHandler.getExamData();
                }
            }
            catch (Exception e)
            {
                Log.e("bt", "exception", e);
            }

        }
        return null;
    }


    public void addExamDataListener(ExamDataListener listener)
    {
        examDataReceiver.addExamDataListener(listener);
    }
}
