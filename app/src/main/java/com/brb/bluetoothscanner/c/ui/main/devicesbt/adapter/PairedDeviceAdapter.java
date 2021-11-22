package com.brb.bluetoothscanner.c.ui.main.devicesbt.adapter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.config.CommonFunction;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.model.DeviceDataModel;
import com.brb.bluetoothscanner.c.utils.ConnectThread;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PairedDeviceAdapter extends RecyclerView.Adapter<PairedDeviceAdapter.ViewHolder> {

    private List<DeviceDataModel> devicedata;
    private final OnItemClickListener listener;
    CommonFunction commonFunction;
    ConnectThread connectThread;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_device_name, tv_device_add, tv_device_connect;
        private RelativeLayout item_view;
        private LinearLayout bt_connect;

        public ViewHolder(View view) {
            super(view);

            tv_device_name = view.findViewById(R.id.tv_device_name);
            tv_device_add = view.findViewById(R.id.tv_device_add);
            tv_device_connect = view.findViewById(R.id.tv_device_connect);
            item_view = view.findViewById(R.id.item_view);
            bt_connect = view.findViewById(R.id.bt_connect);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(DeviceDataModel item);
    }

    public void add(DeviceDataModel data) {
        devicedata.add(data);
    }

    public void remove(DeviceDataModel data) {
        devicedata.remove(data);
    }

    public PairedDeviceAdapter(Context context, List<DeviceDataModel> devicedata, OnItemClickListener listener) {
        this.devicedata = devicedata;
        this.listener = listener;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_paired_device, viewGroup, false);
        commonFunction = new CommonFunction();
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(devicedata.get(position).dname.length() >14){
            viewHolder.tv_device_name.setText(devicedata.get(position).dname.substring(0,13)+"..");
        }else {
            viewHolder.tv_device_name.setText(devicedata.get(position).dname);
        }
        viewHolder.tv_device_add.setText(devicedata.get(position).dadd);
        viewHolder.bt_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(devicedata.get(position));
            }
        });

        viewHolder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (devicedata.get(position).devicetype) {
//                    viewHolder.tv_device_connect.setVisibility(View.VISIBLE);
//                    if (connectwithpaireddevice(devicedata.get(position).device)) {
//                        viewHolder.tv_device_connect.setText("Connected");
//                    } else {
//                        viewHolder.tv_device_connect.setText("Couldn't connect to device");
//                    }
//                } else {
                    final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    final ComponentName cn = new ComponentName("com.android.settings",
                            "com.android.settings.bluetooth.BluetoothSettings");
                    intent.setComponent(cn);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
//                }
            }
        });

    }

    private boolean connectwithpaireddevice(BluetoothDevice device) {
        UUID uniqueID = UUID.randomUUID();

        ParcelUuid[] uuids = device.getUuids();
//        device.setUuids(uuids);
//        device.getUuids().equals(uuids);
        UUID deviceNameUUID = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
//        connectThread = new ConnectThread(context);
////        connectThread.startClient(device, uuids[0].getUuid());
//        connectThread.startClient(device, uniqueID);

        BluetoothSocket mSocket = null;
        try {
            mSocket = device.createInsecureRfcommSocketToServiceRecord(uuids[0].getUuid());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            Log.e("TAG", "socket not created");
            e1.printStackTrace();
        }
        try {
            mSocket.connect();
            return true;
        } catch (IOException e) {
            try {
                mSocket.close();
                Log.e("TAG", "Cannot connect" + e.getMessage());
            } catch (IOException e1) {
                Log.d("TAG", "Socket not closed" + e.getMessage());
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        return false;

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devicedata.size();
    }
}