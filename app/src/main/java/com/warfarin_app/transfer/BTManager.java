package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/25/15.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.warfarin_app.MainActivity;
import com.warfarin_app.data.ExamData;
import com.warfarin_app.util.LogUtil;

public class BTManager extends Thread {
    private ExamDataReceiver examDataReceiver;
    private boolean alive = false;
    private BluetoothDevice device;
    private static final String BLUETEH = "BOLUTEK";
    private static boolean hasData = false;
    private static DataReadSignal dataReadSignal = new DataReadSignal();
    private static BTConnectionHandler btConnectionHandler;
    private static ExamData data;
    private static int BTScanInterval = 10000;
    private static int recvExamInterval = 10000;
    private static boolean isConnected = false;
    MainActivity mainActivity;
    private static int id = 0;

    public BTManager(MainActivity mainActivity)
    {
        examDataReceiver = new ExamDataReceiver();
        this.mainActivity = mainActivity;
    }
    public void run() {
        Log.d("bt", "bt manager start");
        LogUtil.appendMsg("Start Bluetooth Manager");

        Thread t = Thread.currentThread();
        t.setName("BT Manager " + id++);
        alive = true;
        while(alive)
        {

            Log.d("bt", "manager loop");

            if (!pairBTDevice()) {
                Log.d("bt", "no paired device");
                try {
                    Thread.sleep(BTScanInterval);
                }catch (Exception e)
                {
                    Log.e("bt", "exception", e);
                    device = null;
                }

            }

            if(device != null)
            {
                Log.d("bt", "recv exam data");
                if (!isBtHandlerStarted()) {
                    Log.d("bt", "bt handler is not stared");
                    startBtHandler();
                }

                data = recvExamData();
                if (data != null) {
                    examDataReceiver.notifyExamDataReceived(data);
                }
                else
                {
                    Log.d("bt", "null exam data, set device null");
                    if (btConnectionHandler != null && btConnectionHandler.isRunning()) {
                        btConnectionHandler.stopRunning();
                        btConnectionHandler = null;
                    }
                    device = null;
                    try {
                        Thread.sleep(BTScanInterval);
                    }catch (Exception e)
                    {
                        Log.e("bt", "exception", e);
                        device = null;
                    }
                }
            }
            else
            {
                LogUtil.appendMsg("wait BT connection");
                try {
                    Thread.sleep(BTScanInterval);
                }catch (Exception e)
                {
                    Log.e("bt", "exception", e);
                    Log.d("bt", "set device null");
                    device = null;
                    btConnectionHandler = null;
                }
            }
        }


        close();

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
        Log.d("bt", "pair bt device");
        if (device == null)
        {
            Log.d("bt", "start scan");
            device = BTUtil.scan_device(BLUETEH);
        }

        if (device == null)
        {
            Log.d("bt", "scan fail");
            return false;
        }

        return true;
    }

    public ExamData recvExamData()
    {

        Log.d("bt", "wait dataReadSignal\n");
        synchronized (dataReadSignal)
        {
            try
            {
                Log.d("bt", "wait sync data\n");
                dataReadSignal.wait(recvExamInterval);

                if (dataReadSignal.hasDataToProcess) {
                    dataReadSignal.setHasDataToProcess(false);
                    return btConnectionHandler.getExamData();
                }
                else
                {
                    Log.d("bt", "wait exam data timeout");
                }
            }
            catch (InterruptedException ix)
            {
                Log.d("app", "wait exam data timeout");
                Log.d("bt", "wait exam data timeout");
                isConnected = false;
            }
            catch (Exception e)
            {
                Log.e("bt", "exception", e);
                isConnected = false;
            }

        }

        return null;
    }


    private BluetoothDevice getDevice()
    {
        return this.device;
    }

    private void setDevice(BluetoothDevice d)
    {
        device = d;
    }
    public void addExamDataListener(ExamDataListener listener)
    {
        examDataReceiver.addExamDataListener(listener);
    }

    public boolean isRunning()
    {
        return alive;
    }

    public void close()
    {
        alive = false;
        LogUtil.appendMsg("Stop Bluetooth Manager");
    }

}
