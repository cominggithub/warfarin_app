package com.warfarin_app.transfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.warfarin_app.MainActivity;
import com.warfarin_app.util.LogUtil;

import java.util.Set;
import java.util.Vector;

/**
 * Created by Coming on 8/25/15.
 */
public class BTUtil {

    private static final int REQUEST_ENABLE_BT = 2;
    private static MainActivity mainActivity;
    private static Vector<BluetoothAdapter> mArrayAdapter = new Vector<BluetoothAdapter>();
    private static final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
//                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                Log.d("bt", device.getName() + "\n" + device.getAddress());
            }
        }
    };

    public static void setMainActivity(MainActivity activity)
    {
        mainActivity = activity;
    }
    public static BluetoothDevice scan_device(String name)
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Log.d("bt", "scan device");
        LogUtil.appendMsg("scan Bluetooth device \"" + name + "\"");
                requestBluetoothPermission();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
// If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                Log.d("bt", device.getName() + ": " + device.getAddress());
                if (device.getName().equals(name))
                {
                    LogUtil.appendMsg("found Bluetooth device " + name + ", " + device.getAddress());
                    return device;
                }
            }
        }
        else
        {
            LogUtil.appendMsg("no Bluetooth device found");
        }

        return null;
    }

    public static void requestBluetoothPermission()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if (mBluetoothAdapter == null) {
            Log.d("bt", "this device doesn't support bluetooth");
            // Device does not support Bluetooth
        }




        if (!mBluetoothAdapter.isEnabled()) {
            Log.d("bt", "bluetooth is not enabled");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mainActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }
    }

    public static boolean hasBluetoothCapability()
    {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Log.d("bt", "this device doesn't support bluetooth");
            LogUtil.appendMsg("this device doesn't support bluetooth");
            return false;
        }

        if ( Build.FINGERPRINT.startsWith("generic"))
        {
            Log.d("bt", "this device doesn't support bluetooth on emulator");
            return false;
        }

        Log.d("bt", mBluetoothAdapter.getAddress());


        return true;
    }
    public static void destroy()
    {
        mainActivity.unregisterReceiver(mReceiver);
    }

}
