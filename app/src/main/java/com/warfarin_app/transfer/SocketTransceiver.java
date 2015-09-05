package com.warfarin_app.transfer;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Coming on 8/26/15.
 */
public class SocketTransceiver {

    public static void write(OutputStream mmOutStream, String s)
    {
        write(mmOutStream, s.getBytes());
    }

    private static void writePrefix(OutputStream mmOutStream)
    {

    }
    /* Call this from the main activity to send data to the remote device */
    public static void write(OutputStream mmOutStream, byte[] bytes) {

        try {
            mmOutStream.write("AAA".getBytes());
            mmOutStream.write(bytes);
            mmOutStream.flush();
            Log.d("bt", String.format("send %d: %s(%s)",
                    bytes.length,
                    new String(bytes),
                    byteToString(bytes, bytes.length))
            );
        } catch (IOException e) {
            Log.d("bt", e.getMessage());
        }


    }

    public static int read(InputStream is, byte[] buffer, int length)
    {
        int cumReadCnt = 0;
        int readCnt = 0;
        int i;
        byte[] localBuf = new byte[256];

        try {
            while(cumReadCnt < length)
            {
                readCnt = is.read(localBuf, 0, length-cumReadCnt);
                for (i = 0; i < readCnt && i + cumReadCnt < length; i++) {

                    buffer[cumReadCnt+i] = localBuf[i];
                }

                cumReadCnt += readCnt;

            }
            Log.d("bt", String.format("received %d:%d: %s (%s)",
                    cumReadCnt,
                    length,
                    new String(Arrays.copyOf(buffer, cumReadCnt)),
                    byteToString(buffer, cumReadCnt)));
        }catch (IOException e)
        {
            Log.e("bt", "exception", e);
        }

        return cumReadCnt;
    }

    public static String byteToString(byte[] bytes, int length)
    {
        StringBuilder sb = new StringBuilder();
        length = bytes.length < length ? bytes.length:length;
        for (int i=0; i<length; i++)
        {
            sb.append(String.format("%X-", bytes[i]));
        }

        return sb.toString();
    }

}
