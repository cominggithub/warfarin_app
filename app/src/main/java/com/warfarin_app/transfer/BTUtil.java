package com.warfarin_app.transfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.warfarin_app.util.LogUtil;
import com.warfarin_app.util.SystemInfo;
import com.warfarin_app.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Coming on 8/25/15.
 */
public class BTUtil {

    private static final int REQUEST_ENABLE_BT = 2;
    private static MainActivity mainActivity;
    private static Vector<BluetoothAdapter> mArrayAdapter = new Vector<BluetoothAdapter>();
    private static boolean isConnected = false;
//    private static BluetoothDevice device;
//    private static String deviceAddress = "11:22:33:44:55:66";

    public static void setMainActivity(MainActivity activity)
    {
        mainActivity = activity;
    }

//    public static BluetoothDevice scan_device()
//    {
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        Log.d("bt", "scan paired Bluetooth device \"" + deviceAddress + "\"");
//        LogUtil.appendMsg("scan paired Bluetooth device \"" + deviceAddress + "\"");
//                requestBluetoothPermission();
//
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            // Loop through paired devices
//            for (BluetoothDevice device : pairedDevices) {
//                Log.d("bt", "found paired Bluetooth device " + device.getName() + ", " + device.getAddress());
//                LogUtil.appendMsg("found paired Bluetooth device " + device.getName() + ", " + device.getAddress());
//                if (device.getAddress().equals(deviceAddress))
//                {
//                    LogUtil.appendMsg("use Bluetooth device " + device.getName() + ", " + device.getAddress());
//                    Log.d("bt", "use paired Bluetooth device " + device.getName() + ", " + device.getAddress());
//                    setDevice(device);
//                    return device;
//                }
//            }
//        }
//        else
//        {
//            LogUtil.appendMsg("no Bluetooth device found, please pair Bluetooth device before using this APP");
//        }
//
//        return null;
//    }

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

    public static List<BluetoothDevice> getPairedDevice()
    {
        ArrayList<BluetoothDevice> devices = new ArrayList<>();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (SystemInfo.isBluetooth) {
            requestBluetoothPermission();
            devices.addAll(mBluetoothAdapter.getBondedDevices());
        }


        return devices;
    }


    public static void close()
    {

    }

//    private static BluetoothDevice getDevice()
//    {
//        return BTUtil.device;
//    }

    public static boolean isConnected()
    {
        return isConnected;
    }

//    private static void setDevice(BluetoothDevice d) {
//        device = d;
//        deviceAddress = device.getAddress();
//    }

//    public static boolean setDeviceByAddress(String address)
//    {
//        if (SystemInfo.isBluetooth)
//        {
//            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//            for(BluetoothDevice device: mBluetoothAdapter.getBondedDevices())
//            {
//                if (device.getAddress().equals(address))
//                {
//                    setDevice(device);
//                    return true;
//                }
//            }
//            return false;
//        }
//        else
//        {
//            deviceAddress = address;
//        }
//        return true;
//
//    }

//    public static boolean isConnectedDevice(String mac)
//    {
//        if (mac.equals(deviceAddress))
//        {
//            return true;
//        }
//        return false;
//    }

    public static String getAddressFromNameAddress(String nameAddress)
    {
        return nameAddress.substring(nameAddress.indexOf(" ")+1);
    }

    public static String getNameFromNameAddress(String nameAddress)
    {
        return nameAddress.substring(0, nameAddress.indexOf(" "));
    }
//    public static void destroy()
//    {
//        mainActivity.unregisterReceiver(mReceiver);
//    }

    public static BluetoothDevice getDeviceByAddress(String address)
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        for(BluetoothDevice device: mBluetoothAdapter.getBondedDevices())
        {
            if (device.getAddress().equals(address))
            {
                return device;
            }
        }

        return null;
    }

}
