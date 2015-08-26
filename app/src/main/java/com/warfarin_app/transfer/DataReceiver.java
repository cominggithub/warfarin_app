package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/26/15.
 */

import android.bluetooth.BluetoothSocket;

import com.warfarin_app.data.ExamData;
public class DataReceiver {
    WaitingOkState waitingOkState;
    AckState ackState;
    RecvExamDataState recvExamDataState;
    TransferContext transferContext;
    ExamData data;

    public DataReceiver(BluetoothSocket socket)
    {
        waitingOkState = new WaitingOkState(socket);
        recvExamDataState = new RecvExamDataState(socket);
        ackState = new AckState(socket);
        transferContext = new TransferContext();


    }
    public ExamData getExamData()
    {
        String result;
        transferContext.setState(waitingOkState);
        boolean alive = true;
        while(alive)
        {
            result = transferContext.action();

            if (result == "OK") {
                transferContext.setState(recvExamDataState);
            }
            else if(result == "ExamData")
            {
                transferContext.setState(ackState);
            }
            // including "ACK"
            else
            {
                transferContext.setState(waitingOkState);
            }
        }
        return data;
    }
}
