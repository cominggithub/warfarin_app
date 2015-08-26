package com.warfarin_app.transfer;

import android.bluetooth.BluetoothSocket;

import com.warfarin_app.data.ExamData;

import java.io.IOException;

/**
 * Created by Coming on 8/26/15.
 */
public class AckState implements TransferState {
    private final BluetoothSocket mmSocket;
    private String receivedString = "";
    public AckState(BluetoothSocket socket)
    {
        mmSocket = socket;
    }

    @Override
    public String action()
    {
        try
        {
            SocketTransceiver.write(mmSocket.getOutputStream(), "ACK");
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
