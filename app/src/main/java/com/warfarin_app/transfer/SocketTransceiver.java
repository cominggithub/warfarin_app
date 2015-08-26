package com.warfarin_app.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Coming on 8/26/15.
 */
public class SocketTransceiver {

    public static void write(OutputStream mmOutStream, String s)
    {

    }
    /* Call this from the main activity to send data to the remote device */
    public static void write(OutputStream mmOutStream, byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }

    public static void read(InputStream is, byte[] buffer)
    {
        try {
            is.read(buffer);
        }catch (IOException e)
        {

        }
    }
}
