package com.warfarin_app.transfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.warfarin_app.data.ExamData;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Coming on 8/25/15.
 */
public class BTConnectionHandler extends Thread {
    private final BluetoothDevice mmDevice;
    private final BluetoothSocket mmSocket;
    private boolean isRunning = true;
    private DataReadSignal dataReadSignal;
    private ExamData data;
    BluetoothAdapter mBluetoothAdapter;
    private TransferContext transferContext;
    private byte[] bufferredData;

    //    private BluetoothAdapter mBluetoothAdapter;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BTConnection connThread;
    public BTConnectionHandler(BluetoothDevice device, BluetoothAdapter bleAdapter) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final

        Log.d("bt", "connect");
        BluetoothSocket tmp = null;
        mmDevice = device;
        mBluetoothAdapter = bleAdapter;


        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//            tmp = device.createRfcommSocketToServiceRecord(null);
        } catch (IOException e) {
            Log.d("bt", e.toString());
            stopRunning();

        }
        mmSocket = tmp;
    }

    public void setDataReadSignal(DataReadSignal signal)
    {
        dataReadSignal = signal;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        Log.d("bt", "start connect");
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
                        }
                    }

                }
            }


        } catch (Exception e) {
            // Unable to connect; close the socket and get out
            Log.e("bt", "exception", e);
        }


        stopRunning();
        return;
    }

    public ExamData getExamData()
    {
        return data;
    }

    public boolean isRunning()
    {
        return true;
    }

    public void stopRunning()
    {
        isRunning = false;
        Log.d("bt", "stop BT Manager");
        if (dataReadSignal != null)
        {
            dataReadSignal.setHasDataToProcess(true);
        }

        if (mmSocket != null) {
            try {
                mmSocket.close();
            } catch (IOException closeException) {

                Log.d("bt", closeException.toString());

            }
        }

    }


}
