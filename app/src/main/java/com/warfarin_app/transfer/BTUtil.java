package com.warfarin_app.transfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private static boolean isConnected = false;
    private static BluetoothDevice device;

    public static void setMainActivity(MainActivity activity)
    {
        mainActivity = activity;
        init();
    }
    public static BluetoothDevice scan_device(String name)
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Log.d("bt", "scan paired Bluetooth device \"" + name + "\"");
        LogUtil.appendMsg("scan paired Bluetooth device \"" + name + "\"");
                requestBluetoothPermission();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                Log.d("bt", "found paired Bluetooth device " + device.getName() + ", " + device.getAddress());
                LogUtil.appendMsg("found paired Bluetooth device " + device.getName() + ", " + device.getAddress());
                if (device.getName().equals(name))
                {
                    LogUtil.appendMsg("use Bluetooth device " + device.getName() + ", " + device.getAddress());
                    Log.d("bt", "use paired Bluetooth device " + device.getName() + ", " + device.getAddress());
                    setDevice(device);
                    return device;
                }
            }
        }
        else
        {
            LogUtil.appendMsg("no Bluetooth device found, please pair Bluetooth device before using this APP");
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

    private static final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            Log.d("btevt", "BT event");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                LogUtil.appendMsg("found Bluetooth device " + device.getName() + ", " + device.getAddress());
                Log.d("btevt", "found Bluetooth device " + device.getName() + ", " + device.getAddress());

            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                LogUtil.appendMsg(device.getName() + " " + device.getAddress() + " connect");
                Log.d("btevt", device.getName() + " " + device.getAddress() + " connect");
                isConnected = true;

            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("btevt", device.getName() + " " + device.getAddress() + " discovery finished");

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                Log.d("btevt", "disconnect requested " + device.getAddress());

            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                LogUtil.appendMsg(device.getName() + " " + device.getAddress() + " disconnect");
                Log.d("btevt", device.getName() + " " + device.getAddress() + " disconnect");
                if (getDevice() == device) {
                    setDevice(null);
                    isConnected = false;
                }

            }
        }
    };

    public static void init()
    {
        Log.d("btevt", "BTUtil init");
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mainActivity.registerReceiver(mReceiver, filter1);
        mainActivity.registerReceiver(mReceiver, filter2);
        mainActivity.registerReceiver(mReceiver, filter3);
        mainActivity.registerReceiver(mReceiver, filter4);
    }

    public static void close()
    {
        Log.d("btevt", "BTUtil close");
        if (mainActivity != null) {
            mainActivity.unregisterReceiver(mReceiver);
        }
    }
    private static BluetoothDevice getDevice()
    {
        return BTUtil.device;
    }

    public static boolean isConnected()
    {
        return isConnected;
    }
    private static void setDevice(BluetoothDevice d) {
        device = d;
    }

//    public static void destroy()
//    {
//        mainActivity.unregisterReceiver(mReceiver);
//    }

}
