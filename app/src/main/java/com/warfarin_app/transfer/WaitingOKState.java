package com.warfarin_app.transfer;

import android.bluetooth.BluetoothSocket;

import com.warfarin_app.data.ExamData;

import java.io.IOException;

/**
 * Created by Coming on 8/26/15.
 */
public class WaitingOkState implements TransferState {

    private final BluetoothSocket mmSocket;
    private String receivedString = "";
    public WaitingOkState(BluetoothSocket socket)
    {
        mmSocket = socket;
    }

    @Override
    public String action()
    {
        byte[] buffer = new byte[128];
        try
        {
            SocketTransceiver.read(mmSocket.getInputStream(), buffer);
            receivedString = new String(buffer);
        }catch (IOException e)
        {

        }

        return receivedString;
    }

    @Override
    public ExamData getExamData()
    {
        return null;
    }
}
