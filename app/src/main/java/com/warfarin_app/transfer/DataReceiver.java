package com.warfarin_app.transfer;

/**
 * Created by Coming on 8/26/15.
 */

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.warfarin_app.data.ExamData;

import java.io.IOException;
import java.util.Arrays;

import java.io.InputStream;
import java.io.OutputStream;

public class DataReceiver {
    WaitingOkState waitingOkState;
    AckState ackState;
    RecvExamDataState recvExamDataState;
    TransferContext transferContext;
    ExamData data;
    BluetoothSocket socket;
    InputStream is;
    OutputStream os;
    boolean alive = true;
    public DataReceiver(BluetoothSocket socket)
    {
        waitingOkState = new WaitingOkState(socket);
        recvExamDataState = new RecvExamDataState(socket);
        ackState = new AckState(socket);
        transferContext = new TransferContext();

        this.socket = socket;
        try {
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();
        }catch (IOException e)
        {
            Log.e("bt", "exception", e);
        }
    }

    public ExamData getExamDateDirectly()
    {
        byte[] buffer = new byte[128];
        int byteRead = 0;
        try
        {
            byteRead = SocketTransceiver.read(socket.getInputStream(), buffer,12);

            Log.d("bt", String.format("received %d: %s", byteRead, byteToString(buffer, byteRead)));
            Log.d("bt", String.format("%s", new String(Arrays.copyOf(buffer, byteRead))));

        }catch (IOException e)
        {
            Log.e("bt", "exception", e);
        }

        return null;

    }

    private void sendBtCmd(String cmd)
    {
        try
        {
            if(SocketTransceiver.write(socket.getOutputStream(), cmd) == -1)
            {
                alive = false;
            }
        }catch (IOException e)
        {
            Log.e("bt", "exception", e);
            alive = false;
        }
    }

//    public ExamData getExamData()
//    {
//        String result;
//        transferContext.setState(waitingOkState);
//        boolean alive = true;
//        sendConn();
//        while(alive)
//        {
//            result = transferContext.action();
//
//            Log.d("bt", String.format("result string: %s", result));
//
//            if (result.equals("ExamData")) {
//                Log.d("bt", "recv exam data state");
//                transferContext.setState(recvExamDataState);
//            }
//            else if(result.equals("OK"))
//            {
//                Log.d("bt", "Ack state");
//                transferContext.setState(ackState);
//            }
//            // including "ACK"
//            else
//            {
//                sendConn();
//                Log.d("bt", "Waiting OK State");
//                transferContext.setState(waitingOkState);
//                alive = false;
//            }
//        }
//        return data;
//    }

    public ExamData getExamData()
    {

        String cmd = "";
        ExamData d = null;

        sendBtCmd(BTCmd.CONN);
        alive = true;
        while(alive)
        {
            cmd = getCommand();

            if (cmd == null || cmd.equals(""))
            {
                alive = false;
            }
            else if(cmd.equals(BTCmd.OK))
            {
                sendBtCmd(BTCmd.ACK1);
            }
            else if(cmd.equals(BTCmd.DATA))
            {
                d = readExamData();
                sendBtCmd(BTCmd.ACK2);
                alive = false;
            }

//            try
//            {
//                if (socket == null || !socket.isConnected() || socket.getInputStream() == null || socket.getOutputStream() == null)
//                {
//                    Log.d("bt", "check bt socket failed");
//                    alive = false;
//                }
//            }catch (IOException e)
//            {
//                Log.e("bt", "exception", e);
//                alive = false;
//            }

        }

        return d;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String byteToString(byte[] bytes, int length)
    {
        StringBuilder sb = new StringBuilder();
        length = bytes.length < length ? bytes.length:length;
        for (int i=0; i<length; i++)
        {
            sb.append(String.format("%d-", bytes[i]));
        }

        return sb.toString();
    }
    public static String bytesToHex(byte[] bytes, int length) {
        char[] hexChars = new char[bytes.length * 2];
        length = bytes.length < length ? bytes.length:length;

        for ( int j = 0; j < length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public String getCommand()
    {
        byte[] b;
        String cmd;

        b = new byte[4];
        skipToPrefix();

        try {
            if (SocketTransceiver.read(socket.getInputStream(), b, 4) == -1) {
                alive = false;
                return "";
            }
        }catch(Exception e)
        {
            Log.e("bt", "exception", e);
            alive = false;
            return "";
        }
        cmd = new String(b);

        Log.d("bt", "cmd: " + cmd);
        return cmd;

    }

    public boolean skipToPrefix()
    {
        int i;
        int j;
        byte[] b;
        byte[] buf;
        b = new byte[1];
        buf = new byte[BTCmd.PREFIX.length()];
        String prefix = "";


        i = 0;
        Log.d("bt", "skip to prefix " + BTCmd.PREFIX);
        while(!prefix.equals(BTCmd.PREFIX))
        {
            if (SocketTransceiver.read(is, b, 1) < 1)
                return false;
            if (i < BTCmd.PREFIX.length())
            {
                buf[i++] = b[0];
            }
            else
            {
                for(j=0; j<BTCmd.PREFIX.length()-1; j++)
                {
                    buf[j] = buf[j+1];
                }
                buf[j-1] = b[0];
            }
            prefix = new String(buf);
            Log.d("bt", "current prefix: " + prefix);
        }

        Log.d("bt", "match prefix: " + prefix + "(" + BTCmd.PREFIX + ")");
        return true;
    }

    public ExamData readExamData()
    {

        byte[] b = new byte[12];
        SocketTransceiver.read(is, b, 12);
        return ExamData.parseBytes(b);

    }
}
