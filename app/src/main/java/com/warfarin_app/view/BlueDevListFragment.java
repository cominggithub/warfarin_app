package com.warfarin_app.view;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.warfarin_app.R;
import com.warfarin_app.transfer.BTUtil;
import com.warfarin_app.util.SystemInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Coming on 10/24/15.
 */
public class BlueDevListFragment extends android.support.v4.app.Fragment
{
    ListView listview;

    ArrayList<HashMap<String,String>> devList = new ArrayList<HashMap<String,String>>();

    MainActivity mainActivity;
    ArrayList<View> entryViews;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity)activity;
        entryViews = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("app", "onCreateView");
        return inflater.inflate(R.layout.bluedev_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listview  = (ListView) this.getView().findViewById(R.id.bluedev_lvBluedevList);
        Log.d("app", "onActivityCreated");
        refresh();
    }

    public void refresh()
    {
        Log.d("app", "refresh start");
        Log.d("bt", "refresh start");

        loadBlueDev();
        Log.d("app", "refresh done");
        Log.d("bt", "refresh done");
    }


    public void loadBlueDev()
    {
        devList.clear();
        entryViews.clear();

        if (SystemInfo.isBluetooth) {


            for (BluetoothDevice device : BTUtil.getPairedDevice()) {
                HashMap<String, String> item = new HashMap<String, String>();
                String name = device.getName();
                String address = device.getAddress();

                if (name.equals(address)) {
                    item.put("name", name);
                } else {
                    item.put("name", name + "(" + address + ")");
                }
                item.put("connect", "hahah");
                devList.add(item);
            }

            if (devList.size() == 0) {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("name", "no paired Bluetooth device found");
                devList.add(item);
            }
        }
        else
        {
            HashMap<String, String> item;
            item = new HashMap<String, String>();
            item.put("name", "BLUETEH (11:22:33:44:55:66)");
            item.put("address", "11:22:33:44:55:66");
            devList.add(item);
            item = new HashMap<String, String>();
            item.put("name", "aa:bb:cc:dd:ee:ff");
            item.put("address", "aa:bb:cc:dd:ee:ff");
            devList.add(item);
        }

        String[] fields = {"name", "address"};
        int[] views = {
                R.id.bluedev_entry_tvName,
                R.id.bluedev_entry_tvAddress
        };

        SimpleAdapter adapter = new SimpleAdapter(
                mainActivity.getBaseContext(),
                devList,
                R.layout.bluedev_entry,
                fields,
                views
        )
        {
            @Override
            public View getView (int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                entryViews.add(v);
                setBtConnectText(v);
                final Button b = (Button) v.findViewById(R.id.bluedev_entry_btConnect);
                final TextView tvAddress = (TextView) v.findViewById(R.id.bluedev_entry_tvAddress);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Log.d("app", tvAddress.getText().toString() + " clicked");
                        if (!b.getText().equals("Connected")) {
                            setDevice(tvAddress.getText().toString());
                        }
                    }
                });
                return v;
            }
        };

        listview.setAdapter(null);
        listview.setAdapter(adapter);
    }

    private void setDevice(String address)
    {
//        BTUtil.setDeviceByAddress(address);

        for (View v : entryViews) {
            setBtConnectText(v);
        }
    }

    private void setBtConnectText(View v)
    {
        Button b = (Button) v.findViewById(R.id.bluedev_entry_btConnect);
        TextView tvAddress = (TextView) v.findViewById(R.id.bluedev_entry_tvAddress);
//        if (BTUtil.isConnectedDevice(tvAddress.getText().toString())) {
//            b.setText("Connected");
//        }
//        else
//        {
//            b.setText("Connect");
//        }
    }
}
