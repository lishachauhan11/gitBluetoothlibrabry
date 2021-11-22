package com.brb.bluetoothscanner.c.ui.main.searchdevices.model;

import android.bluetooth.BluetoothDevice;

public class DeviceDataModel {

    public int did = 0;
    public String dname;
    public String dadd;
    public int rssid;
    public BluetoothDevice device;
    public Boolean devicetype = false;

    public DeviceDataModel(String dname, String dadd, int rssid, BluetoothDevice device){
        this.dname = dname;
        this.dadd = dadd;
        this.rssid = rssid;
        this.device = device;
    }

    public DeviceDataModel(String dname, String dadd, int rssid, BluetoothDevice device,Boolean devicetype){
        this.dname = dname;
        this.dadd = dadd;
        this.rssid = rssid;
        this.device = device;
        this.devicetype = devicetype;
    }
}
