package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/25/15.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.warfarin_app.data.ExamData;
import com.warfarin_app.util.LogUtil;
import com.warfarin_app.util.SystemInfo;
import com.warfarin_app.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class BTManager extends Thread {
    private ExamDataReceiver examDataReceiver;
    private boolean alive = false;
    private BluetoothDevice device;
    private BluetoothDevice targetDevice;
    private static final String BLUETEH = "BOLUTEK";
    private static boolean hasData = false;
    private static DataReadSignal dataReadSignal = new DataReadSignal();
    private static BTConnectionHandler btConnectionHandler;
    private static ExamData data;
    private static int BTScanInterval = 10000;
    private static int recvExamInterval = 20000;
    private static boolean isConnected = false;
    MainActivity mainActivity;
    private static int id = 0;
    private static BTManager instance;
    private boolean isDeviceChanged = false;
    private boolean isRecvDataTimeout = false;
    private Object deviceSyncObject = new Object();

    private List<ExamData> examDataQueue;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            Log.d("bt", "BT event");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                LogUtil.appendMsg("found Bluetooth device " + device.getName() + ", " + device.getAddress());
                Log.d("bt", "found Bluetooth device " + device.getName() + ", " + device.getAddress());

            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                LogUtil.appendMsg(device.getName() + " " + device.getAddress() + " connect");
                Log.d("bt", device.getName() + " " + device.getAddress() + " connect");
                isConnected = true;

            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("bt", device.getName() + " " + device.getAddress() + " discovery finished");

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                Log.d("bt", "disconnect requested " + device.getAddress());

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                LogUtil.appendMsg(device.getName() + " " + device.getAddress() + " disconnect");
                Log.d("bt", device.getName() + " " + device.getAddress() + " disconnect");
                if (getDevice() == device)
                {
                    isConnected = false;
                }

            }
        }
    };

    public void init()
    {
        examDataQueue = new ArrayList();
    }

    public void registerReceiver()
    {
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mainActivity.registerReceiver(mReceiver, filter1);
        mainActivity.registerReceiver(mReceiver, filter2);
        mainActivity.registerReceiver(mReceiver, filter3);
        mainActivity.registerReceiver(mReceiver, filter4);
    }

    private BTManager(MainActivity mainActivity)
    {
        examDataReceiver = new ExamDataReceiver();
        this.mainActivity = mainActivity;
        init();
    }

    public static BTManager getInstance(MainActivity mainActivity)
    {
        if (instance == null)
        {
            instance = new BTManager(mainActivity);
        }
        return instance;
    }

    public static BTManager getInstance()
    {
        return instance;
    }

    public void run() {
        Log.d("bt", "bt manager start");
        LogUtil.appendMsg("Start Bluetooth Manager");

        Thread t = Thread.currentThread();
        t.setName("BT Manager " + id++);
        alive = true;
        registerReceiver();
        while(alive)
        {
            Log.d("bt", "manager loop");

            if (isDeviceChanged)
            {
                isDeviceChanged = false;
                stopBtHandler();
                startBtHandler();
            }

            if (device == null)
            {
                LogUtil.appendMsg("no bt device configured");
                try {
                    synchronized (deviceSyncObject)
                    {
                        deviceSyncObject.wait();
                    }
                }catch (Exception e)
                {
                    Log.e("bt", "exception", e);
                }
            }
            // user has select device to be connected with
            else
            {
                Log.d("bt", "start recv exam data");
                // start bt handler if not started yet
                if (!isBtHandlerStarted()) {
                    Log.d("bt", "restart bt handler");
                    startBtHandler();
                }

                // receive exam data
                data = recvExamData();
                if (data != null) {
                    examDataReceiver.notifyExamDataReceived(data);
                }
                // receive null exam data, something wrong, stop bt handler
                else
                {
                    Log.d("bt", "null exam data");
                    if (isBtHandlerStarted() && !btConnectionHandler.isConnected()) {
                        stopBtHandler();
                    }

                    try {
                        Thread.sleep(BTScanInterval);
                    }catch (Exception e)
                    {
                        Log.e("bt", "exception", e);
                    }
                }
            }

        }
        close();
    }

    public void startBtHandler() {
        Log.d("bt", "startBtHandler");

        LogUtil.appendMsg("Start Bluetooth Connection Handler");

        if (isBtHandlerStarted())
        {
            return;
        }

//        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.cancelDiscovery();
        btConnectionHandler = new BTConnectionHandler(device, mBluetoothAdapter);
        btConnectionHandler.setDataReadSignal(dataReadSignal);
        btConnectionHandler.start();
    }

    public void stopBtHandler() {
        Log.d("bt", "stopBtHandler");
        LogUtil.appendMsg("stop Bluetooth Connection Handler");
        if (btConnectionHandler != null && btConnectionHandler.isRunning()) {
            btConnectionHandler.stopRunning();
            btConnectionHandler = null;
        }
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

    public ExamData recvExamData()
    {

        Log.d("bt", "wait dataReadSignal\n");
        isRecvDataTimeout = false;
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
                else if (btConnectionHandler.isConnected())
                {
                    Log.d("bt", "wait exam data timeout");
                    isRecvDataTimeout = true;
                }
                else
                {
                    Log.d("bt", "wait exam data timeout and disconnected");
                    isConnected = false;
                }
            }
            catch (InterruptedException ix)
            {
                Log.d("app", "wait exam data timeout (interrupt)");
                Log.d("bt", "wait exam data timeout (interrupt)");
                isConnected = false;
                isRecvDataTimeout = true;
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
        synchronized (deviceSyncObject) {
            if (d != null && (device == null || device != d)) {
                device = d;
                isDeviceChanged = true;
                restart();
                deviceSyncObject.notifyAll();
            }
        }
    }

    public void restart()
    {
        isDeviceChanged = true;
    }

    public void addExamDataListener(ExamDataListener listener)
    {
        examDataReceiver.addExamDataListener(listener);
    }

    public boolean isRunning()
    {
        return this.isAlive();
    }

    public void close()
    {
        alive = false;

        stopBtHandler();

        if (mainActivity != null && mReceiver != null) {
            try {
                mainActivity.unregisterReceiver(mReceiver);
                mReceiver = null;
            }catch(Exception e)
            {
                Log.e("bt", "exception", e);
            }
        }

        LogUtil.appendMsg("Stop Bluetooth Manager");
    }

    public static void setDeviceByAddress(String address)
    {
        if (!SystemInfo.isBluetooth)
            return;

        BTManager btManager = getInstance();
        BluetoothDevice device = BTUtil.getDeviceByAddress(address);
        btManager.setDevice(device);

    }

}
