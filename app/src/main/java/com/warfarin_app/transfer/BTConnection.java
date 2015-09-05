package com.warfarin_app.transfer;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Coming on 8/25/15.
 */
public class BTConnection extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public BTConnection(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        Log.d("app", "Start to Send data");
        // Keep listening to the InputStream until an exception occurs
//        while (true) {
        try {
            // Read from the InputStream
            byte i=0;
            String msg;
            String recv;
            for(i=0; i<10; i++){
                msg = "Hello_"+i;
                byte[] sendb = new byte[1];
                sendb[0] = i;
//                    write(msg.getBytes());
                write(sendb);

//                    Log.d("CC", "send: " + msg);
                Log.d("app", "send: " + sendb[0]);
                bytes = mmInStream.read(buffer);

//                    recv = String.valueOf(buffer);
//                    Log.d("CC", "recv: " + recv);
                Log.d("app", "recv: " + buffer[0]);
            }
            mmSocket.close();


        } catch (IOException e) {

        }

    }

    public void write(String s)
    {

    }
    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
