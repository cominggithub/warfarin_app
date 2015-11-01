package com.warfarin_app.transfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.warfarin_app.data.ExamData;
import com.warfarin_app.util.LogUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * Created by Coming on 8/25/15.
 */

public class BTConnectionHandler extends Thread {
    private final BluetoothDevice mmDevice;
    private BluetoothSocket mmSocket = null;
    private boolean isRunning = true;
    private DataReadSignal dataReadSignal;
    private ExamData data;
    BluetoothAdapter mBluetoothAdapter;
    private TransferContext transferContext;
    private byte[] bufferredData;
    private static int id = 0;

    private List<ExamData> examDataQueue;

    //    private BluetoothAdapter mBluetoothAdapter;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean isConnected = false;

    private BTConnection connThread;
    public BTConnectionHandler(BluetoothDevice device, BluetoothAdapter bleAdapter) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final

        Log.d("bt", "Connect to " + device.getName() + "(" + device.getAddress() + ")");
        LogUtil.appendMsg("Connect to " + device.getName() + "(" + device.getAddress() + ")");

        mmDevice = device;
        mBluetoothAdapter = bleAdapter;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code

            Method m= mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            mmSocket =    (BluetoothSocket) m.invoke(device, 1);
            isConnected = true;
        } catch (Exception e) {
            Log.e("bt", "exception", e);
            stopRunning();
        }
    }

    public void setDataReadSignal(DataReadSignal signal)
    {
        dataReadSignal = signal;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
        isRunning = true;
        Log.d("bt", "start connect");
        Thread t = Thread.currentThread();
        t.setName("BT Connection Handler " + id++);

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();

            DataReceiver receiver = new DataReceiver(mmSocket);


            while (isRunning) {
                data = receiver.getExamData();
                if (data != null)
                {
                    Log.d("bt", data.toString());
                    dataReadSignal.setHasDataToProcess(true);
                    synchronized (dataReadSignal)
                    {
                        try
                        {
                            dataReadSignal.notify();
                        }catch (Exception e)
                        {
                            Log.e("bt", "exception", e);
                            isRunning = false;
                        }
                    }
                }
                else
                {
                    Log.d("bt", "BT Connection handler gets null exam data");
                    isRunning = false;
                }
            }


        } catch (Exception e) {
            // Unable to connect; close the socket and get out
            Log.e("bt", "exception", e);
            if (e.getMessage().contains("Connection is not created (failed or aborted)"))
            {
                LogUtil.appendMsg("無法連線建立，請重新開機");
            }
        }

        stopRunning();
    }

    public ExamData getExamData()
    {
        return data;
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public boolean isConnected() { return isConnected; }
    public void stopRunning()
    {
        isRunning = false;
        isConnected = false;
        Log.d("bt", "stop BT Connection Handler");
        if (dataReadSignal != null)
        {
            dataReadSignal.setHasDataToProcess(false);
            synchronized (dataReadSignal)
            {
                try
                {
                    dataReadSignal.notifyAll();
                }catch (Exception e)
                {
                    Log.e("bt", "exception", e);
                    isRunning = false;
                }
            }
        }

        if (mmSocket != null) {
            try {
                mmSocket.close();
            } catch (IOException closeException) {

                Log.e("bt", "exception", closeException);

            }
        }
    }
}
