package com.brb.bluetoothscanner.c.ui.main.devicesbt.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.config.CommonFunction;
import com.brb.bluetoothscanner.c.eu_consent_Class;
import com.brb.bluetoothscanner.c.eu_consent_Helper;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.adapter.DeviceDataAdapter;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.model.DeviceDataModel;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;

import java.util.ArrayList;
import java.util.List;

public class SearchStregthActivty extends AppCompatActivity {

    RecyclerView listViewDetectedStronger, listViewDetectedIntermidiate, listViewDetectedWeaker;
    BluetoothDevice bdDevice;
    BluetoothAdapter bluetoothAdapter = null;
    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
    DeviceDataAdapter detectedAdapterweaker, detectedAdapterstronger, detectedAdapterintermiadiate;
    LinearLayout buttonSearch;
    ImageView buttonBack;
    private ButtonClicked clicked;
    CommonFunction commonFunction;
    BluetoothLeScanner bLeScanner;
    LinearLayout llstrong, llintermidiate, llweak;
    List<DeviceDataModel> weaklistdata = new ArrayList<>();
    List<DeviceDataModel> intermidiatelistdata = new ArrayList<>();
    List<DeviceDataModel> stronglistdata = new ArrayList<>();

    RelativeLayout rel_ad_layout;
    AdRequest banner_adRequest;
    AdManagerAdRequest banner_manager_request;
    AdView adView;
    AdManagerAdView adManagerAdView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength);

        initObject();
    }

    private void initObject() {
        listViewDetectedStronger = findViewById(R.id.listViewDetectedStronger);
        listViewDetectedIntermidiate = findViewById(R.id.listViewDetectedIntermidiate);
        listViewDetectedWeaker = findViewById(R.id.listViewDetectedWeaker);
        llstrong = findViewById(R.id.llstrong);
        llintermidiate = findViewById(R.id.llintermidiate);
        llweak = findViewById(R.id.llweak);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        clicked = new ButtonClicked();
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonBack = findViewById(R.id.backbutton);
        buttonBack.setOnClickListener(clicked);
        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();
        commonFunction = new CommonFunction();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        intOnClickList();
        startSearching();
        startScanning();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void intOnClickList() {
        buttonSearch.setOnClickListener(clicked);
        bLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }


    private void startSearching() {
        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();

    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
//            Toast.makeText(context, "Enter in reciver.", Toast.LENGTH_SHORT).show();
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
//                Log.e("Tag", "  RSSI: " + rssi + "dBm");

                for (int i = 0; i < weaklistdata.size(); i++) {
                    if (device.getAddress().equals(weaklistdata.get(i).dadd)) {
                        if (rssi <= -10 && rssi >= -60) {
                            weaklistdata.remove(weaklistdata.get(i));
                            detectedAdapterweaker.notifyDataSetChanged();
                        }
                    }
                }

                for (int i = 0; i < stronglistdata.size(); i++) {
                    if (device.getAddress().equals(stronglistdata.get(i).dadd)) {
                        if (rssi <= -30) {
                            stronglistdata.remove(stronglistdata.get(i));
                            detectedAdapterstronger.notifyDataSetChanged();
                        }
                    }
                }


                for (int i = 0; i < intermidiatelistdata.size(); i++) {
                    if (device.getAddress().equals(intermidiatelistdata.get(i).dadd)) {
                        if (rssi <= -30 && rssi >= -60) {
                            //do nothing
                        } else {
                            intermidiatelistdata.remove(intermidiatelistdata.get(i));
                            detectedAdapterintermiadiate.notifyDataSetChanged();
                        }
                    }
                }
                if (stronglistdata.isEmpty()) {
                    llstrong.setVisibility(View.GONE);
                }
                if (intermidiatelistdata.isEmpty()) {
                    llintermidiate.setVisibility(View.GONE);
                }
                if (weaklistdata.isEmpty()) {
                    llweak.setVisibility(View.GONE);
                }
                Log.e("Tag", device.getName() + " " + device.getAddress() + " " + rssi);
                try {
                } catch (Exception e) {
                    Log.e("Tag", "Inside the exception: ");
                    e.printStackTrace();
                }
                if (rssi <= -10 && rssi >= -30) {
                    stronglistdatafound(device, rssi);
                } else if (rssi <= -30 && rssi >= -60) {
                    intetmidiatelistdatafound(device, rssi);
                } else {
                    weaklistdatafound(device, rssi);
                }

            }
        }
    };


    class ButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonSearch:
                    startSearching();
                    startScanning();
                    break;
                case R.id.backbutton:
                    finish();
                    break;
            }
        }
    }

    private void connectdevice(DeviceDataModel item, int item_type) {

        bdDevice = item.device;
        Boolean isBonded = false;
        try {
            isBonded = commonFunction.createBond(bdDevice);
            if (isBonded) {
                if (isBonded) {

                }
            } else {
                Log.e("Tag", "The bond is created: " + isBonded);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }//connect(bdDevice)
    }

    private void stronglistdatafound(BluetoothDevice device, int rssi) {
        if (stronglistdata.size() < 1) {
            stronglistdata.add(
                    new DeviceDataModel(device.getName(),
                            device.getAddress(),
                            rssi, device));
            llstrong.setVisibility(View.VISIBLE);
            detectedAdapterstronger = new DeviceDataAdapter(stronglistdata, new DeviceDataAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DeviceDataModel item) {
                    connectdevice(item, 1);
                }
            });
            listViewDetectedStronger.setAdapter(detectedAdapterstronger);
            detectedAdapterstronger.notifyDataSetChanged();

//                Log.e("Tag","First enter");
        } else {
            boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
            for (int i = 0; i < stronglistdata.size(); i++) {
                if (device.getAddress().equals(stronglistdata.get(i).dadd)) {
//                            arrayListBluetoothDevices.remove(i);
                    if (stronglistdata.get(i).rssid != rssi) {
                        stronglistdata.get(i).rssid = rssi;
                        detectedAdapterstronger.edit(stronglistdata.get(i), i);
                        detectedAdapterstronger.notifyDataSetChanged();
                    }
                    flag = false;
                }
            }
            if (flag) {
                stronglistdata.add(
                        new DeviceDataModel(device.getName(),
                                device.getAddress(),
                                rssi, device));
//                    Log.e("Tag", "weak Device Name: " + device.getName() + " " + device.getAddress() + " " + " rssi: " + rssi + "\n");
                llstrong.setVisibility(View.VISIBLE);
                detectedAdapterstronger.add(new DeviceDataModel(device.getName(), device.getAddress(), rssi, device));
                detectedAdapterstronger.notifyDataSetChanged();
            }
        }
    }

    private void intetmidiatelistdatafound(BluetoothDevice device, int rssi) {
        if (intermidiatelistdata.size() < 1) {
            intermidiatelistdata.add(
                    new DeviceDataModel(device.getName(),
                            device.getAddress(),
                            rssi, device));
            llintermidiate.setVisibility(View.VISIBLE);
            detectedAdapterintermiadiate = new DeviceDataAdapter(intermidiatelistdata, new DeviceDataAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DeviceDataModel item) {
                    connectdevice(item, 2);
                }
            });
            listViewDetectedIntermidiate.setAdapter(detectedAdapterintermiadiate);
            detectedAdapterintermiadiate.notifyDataSetChanged();

//                Log.e("Tag","First enter");
        } else {
            boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
            for (int i = 0; i < intermidiatelistdata.size(); i++) {
                if (device.getAddress().equals(intermidiatelistdata.get(i).dadd)) {
//                            arrayListBluetoothDevices.remove(i);
                    if (intermidiatelistdata.get(i).rssid != rssi) {
                        intermidiatelistdata.get(i).rssid = rssi;
                        detectedAdapterintermiadiate.edit(intermidiatelistdata.get(i), i);
                        detectedAdapterintermiadiate.notifyDataSetChanged();
                    }
                    flag = false;
                }
            }
            if (flag) {
                intermidiatelistdata.add(
                        new DeviceDataModel(device.getName(),
                                device.getAddress(),
                                rssi, device));

                llintermidiate.setVisibility(View.VISIBLE);
                detectedAdapterintermiadiate.add(new DeviceDataModel(device.getName(), device.getAddress(), rssi, device));
                detectedAdapterintermiadiate.notifyDataSetChanged();
            }
        }
    }

    private void weaklistdatafound(BluetoothDevice device, int rssi) {
        if (weaklistdata.size() < 1) {
            weaklistdata.add(
                    new DeviceDataModel(device.getName(),
                            device.getAddress(),
                            rssi, device));
            llweak.setVisibility(View.VISIBLE);
            detectedAdapterweaker = new DeviceDataAdapter(weaklistdata, new DeviceDataAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DeviceDataModel item) {
                    connectdevice(item, 3);
                }
            });
            listViewDetectedWeaker.setAdapter(detectedAdapterweaker);
            detectedAdapterweaker.notifyDataSetChanged();

        } else {
            boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
            for (int i = 0; i < weaklistdata.size(); i++) {
                if (device.getAddress().equals(weaklistdata.get(i).dadd)) {
//                            arrayListBluetoothDevices.remove(i);
                    if (weaklistdata.get(i).rssid != rssi) {
                        weaklistdata.get(i).rssid = rssi;
                        detectedAdapterweaker.edit(weaklistdata.get(i), i);
                        detectedAdapterweaker.notifyDataSetChanged();
                    }
                    flag = false;
                }
            }
            if (flag) {
                weaklistdata.add(
                        new DeviceDataModel(device.getName(),
                                device.getAddress(),
                                rssi, device));
//                    Log.e("Tag", "weak Device Name: " + device.getName() + " " + device.getAddress() + " " + " rssi: " + rssi + "\n");
                llweak.setVisibility(View.VISIBLE);
                detectedAdapterweaker.add(new DeviceDataModel(device.getName(), device.getAddress(), rssi, device));
                detectedAdapterweaker.notifyDataSetChanged();
            }
        }
    }

    public void startScanning() {
        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                bLeScanner.startScan(leScanCallback);
            }
        });
    }

    public void stopScanning() {
        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                bLeScanner.stopScan(leScanCallback);
            }
        });
    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
//            peripheralTextView.append("Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() + "\n");
//            if(result.getDevice().getName() != null) {
            for (int i = 0; i < weaklistdata.size(); i++) {
                if (result.getDevice().getAddress().equals(weaklistdata.get(i).dadd)) {
//                            arrayListBluetoothDevices.remove(i);
                    if (result.getRssi() <= -30 && result.getRssi() >= -60) {
                        weaklistdata.remove(weaklistdata.get(i));
                        detectedAdapterweaker.notifyDataSetChanged();
                    }
                }
            }


            for (int i = 0; i < stronglistdata.size(); i++) {
                if (result.getDevice().getAddress().equals(stronglistdata.get(i).dadd)) {
                    if (result.getRssi() <= -30) {
                        stronglistdata.remove(stronglistdata.get(i));
                        detectedAdapterstronger.notifyDataSetChanged();
                    }
                }
            }

            for (int i = 0; i < intermidiatelistdata.size(); i++) {
                if (result.getDevice().getAddress().equals(intermidiatelistdata.get(i).dadd)) {
                    if (result.getRssi() <= -30 && result.getRssi() >= -60) {
                        //do nothing
                    } else {
                        intermidiatelistdata.remove(intermidiatelistdata.get(i));
                        detectedAdapterintermiadiate.notifyDataSetChanged();
                    }
                }
            }
            if (stronglistdata.isEmpty()) {
                llstrong.setVisibility(View.GONE);
            }
            if (intermidiatelistdata.isEmpty()) {
                llintermidiate.setVisibility(View.GONE);
            }
            if (weaklistdata.isEmpty()) {
                llweak.setVisibility(View.GONE);
            }
            if (result.getRssi() <= -10 && result.getRssi() >= -30) {
                stronglistdatafound(result);
            } else if (result.getRssi() <= -30 && result.getRssi() >= -60) {
                intetmidiatelistdatafound(result);
            } else {
                weaklistdatafound(result);
            }

//            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stronglistdatafound(ScanResult result) {
        if (stronglistdata.size() < 1) {
            stronglistdata.add(
                    new DeviceDataModel(result.getDevice().getName(),
                            result.getDevice().getAddress(),
                            result.getRssi(), result.getDevice()));
            llstrong.setVisibility(View.VISIBLE);
            detectedAdapterstronger = new DeviceDataAdapter(stronglistdata, new DeviceDataAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DeviceDataModel item) {
                    connectdevice(item, 1);
                }
            });
            listViewDetectedStronger.setAdapter(detectedAdapterstronger);
            detectedAdapterstronger.notifyDataSetChanged();

//                Log.e("Tag","First enter");
        } else {
            boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
            for (int i = 0; i < stronglistdata.size(); i++) {
                if (result.getDevice().getAddress().equals(stronglistdata.get(i).dadd)) {
//                            arrayListBluetoothDevices.remove(i);
                    if (stronglistdata.get(i).rssid != result.getRssi()) {
                        stronglistdata.get(i).rssid = result.getRssi();
                        detectedAdapterstronger.edit(stronglistdata.get(i), i);
                        detectedAdapterstronger.notifyDataSetChanged();
                    }
                    flag = false;
                }
            }
            if (flag) {
                stronglistdata.add(
                        new DeviceDataModel(result.getDevice().getName(),
                                result.getDevice().getAddress(),
                                result.getRssi(), result.getDevice()));
//                    Log.e("Tag", "weak Device Name: " + result.getDevice().getName() + " " + result.getDevice().getAddress() + " " + " rssi: " + result.getRssi() + "\n");
                llstrong.setVisibility(View.VISIBLE);
                detectedAdapterstronger.add(new DeviceDataModel(result.getDevice().getName(), result.getDevice().getAddress(), result.getRssi(), result.getDevice()));
                detectedAdapterstronger.notifyDataSetChanged();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void intetmidiatelistdatafound(ScanResult result) {
        if (intermidiatelistdata.size() < 1) {
            intermidiatelistdata.add(
                    new DeviceDataModel(result.getDevice().getName(),
                            result.getDevice().getAddress(),
                            result.getRssi(), result.getDevice()));
            llintermidiate.setVisibility(View.VISIBLE);
            detectedAdapterintermiadiate = new DeviceDataAdapter(intermidiatelistdata, new DeviceDataAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DeviceDataModel item) {
                    connectdevice(item, 2);
                }
            });
            listViewDetectedIntermidiate.setAdapter(detectedAdapterintermiadiate);
            detectedAdapterintermiadiate.notifyDataSetChanged();

        } else {
            boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
            for (int i = 0; i < intermidiatelistdata.size(); i++) {
                if (result.getDevice().getAddress().equals(intermidiatelistdata.get(i).dadd)) {
//                            arrayListBluetoothDevices.remove(i);
                    if (intermidiatelistdata.get(i).rssid != result.getRssi()) {
                        intermidiatelistdata.get(i).rssid = result.getRssi();
                        detectedAdapterintermiadiate.edit(intermidiatelistdata.get(i), i);
                        detectedAdapterintermiadiate.notifyDataSetChanged();
                    }
                    flag = false;
                }
            }
            if (flag) {
                intermidiatelistdata.add(
                        new DeviceDataModel(result.getDevice().getName(),
                                result.getDevice().getAddress(),
                                result.getRssi(), result.getDevice()));
//                    Log.e("Tag", "weak Device Name: " + result.getDevice().getName() + " " + result.getDevice().getAddress() + " " + " rssi: " + result.getRssi() + "\n");
                llintermidiate.setVisibility(View.VISIBLE);
                detectedAdapterintermiadiate.add(new DeviceDataModel(result.getDevice().getName(), result.getDevice().getAddress(), result.getRssi(), result.getDevice()));
                detectedAdapterintermiadiate.notifyDataSetChanged();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void weaklistdatafound(ScanResult result) {
        if (weaklistdata.size() < 1) {
            weaklistdata.add(
                    new DeviceDataModel(result.getDevice().getName(),
                            result.getDevice().getAddress(),
                            result.getRssi(), result.getDevice()));
            llweak.setVisibility(View.VISIBLE);
            detectedAdapterweaker = new DeviceDataAdapter(weaklistdata, new DeviceDataAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DeviceDataModel item) {
                    connectdevice(item, 3);
                }
            });
            listViewDetectedWeaker.setAdapter(detectedAdapterweaker);
            detectedAdapterweaker.notifyDataSetChanged();

//                Log.e("Tag","First enter");
        } else {
            boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
            for (int i = 0; i < weaklistdata.size(); i++) {
                if (result.getDevice().getAddress().equals(weaklistdata.get(i).dadd)) {
//                            arrayListBluetoothDevices.remove(i);
                    if (weaklistdata.get(i).rssid != result.getRssi()) {
                        weaklistdata.get(i).rssid = result.getRssi();
                        detectedAdapterweaker.edit(weaklistdata.get(i), i);
                        detectedAdapterweaker.notifyDataSetChanged();
                    }
                    flag = false;
                }
            }
            if (flag) {
                weaklistdata.add(
                        new DeviceDataModel(result.getDevice().getName(),
                                result.getDevice().getAddress(),
                                result.getRssi(), result.getDevice()));
//                    Log.e("Tag", "weak Device Name: " + result.getDevice().getName() + " " + result.getDevice().getAddress() + " " + " rssi: " + result.getRssi() + "\n");
                llweak.setVisibility(View.VISIBLE);
                detectedAdapterweaker.add(new DeviceDataModel(result.getDevice().getName(), result.getDevice().getAddress(), result.getRssi(), result.getDevice()));
                detectedAdapterweaker.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopScanning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScanning();
    }

    public void onResume() {
        try {
            super.onResume();
            eu_consent_Helper.admob_banner_ad_unit = FastSave.getInstance().getString(eu_consent_Helper.banner_code, "");
            eu_consent_Helper.admob_interstial_ad_id = FastSave.getInstance().getString(eu_consent_Helper.interstitial_code, "");
            eu_consent_Helper.ad_mob_native_ad_id = FastSave.getInstance().getString(eu_consent_Helper.native_code, "");
            eu_consent_Helper.ad_mob_reward_ad_id = FastSave.getInstance().getString(eu_consent_Helper.reward_code, "");
            eu_consent_Helper.ad_mob_open_ad_id = FastSave.getInstance().getString(eu_consent_Helper.open_code, "");
            eu_consent_Helper.ad_type = FastSave.getInstance().getString(eu_consent_Helper.adtype, "");
            eu_consent_Helper.pub_id_code = FastSave.getInstance().getString(eu_consent_Helper.pub_id_code, "");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AdMobConsent();
                }
            });

        } catch (Exception e) {
            e.toString();
        }
    }

    private void AdMobConsent() {
        boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
        if (!is_hide_ads) {
            boolean is_online = eu_consent_Class.isOnline(this);
            if (is_online) {
                boolean is_eea_user = FastSave.getInstance().getBoolean(eu_consent_Helper.EEA_USER_KEY, false);
                if (is_eea_user) {
                    boolean is_consent_set = FastSave.getInstance().getBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY, false);
                    if (is_consent_set) {
                        boolean is_g_user = FastSave.getInstance().getBoolean(eu_consent_Helper.GOOGLE_PLAY_STORE_USER_KEY, false);
                        if (is_g_user) {
                            LoadAd();

                        } else {
                            // Hide Ads
                            Hide_Ad_Layout();
                        }
                    } else {
                        eu_consent_Class.DoConsentProcess(this, SearchStregthActivty.this);
                    }
                } else {
                    boolean is_g_user = FastSave.getInstance().getBoolean(eu_consent_Helper.GOOGLE_PLAY_STORE_USER_KEY, false);
                    if (is_g_user) {
                        LoadAd();

                    } else {
                        // Hide Ads
                        Hide_Ad_Layout();
                    }
                }
            } else {
                // Hide Ads
                Hide_Ad_Layout();
            }
        } else {
            // Hide Ads
            Hide_Ad_Layout();
        }
    }

    private void Hide_Ad_Layout() {
        // Hide Ads
        rel_ad_layout = (RelativeLayout) findViewById(R.id.ad_layout);
        rel_ad_layout.setVisibility(View.GONE);
    }

    private void LoadAd() {
        // TODO Auto-generated method stub
        try {
            Bundle non_personlize_bundle = new Bundle();
            non_personlize_bundle.putString("npa", "1");

            boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, false);
            if (is_show_non_personalize) {
                if (eu_consent_Helper.ad_type.equals("1"))
                {
                    banner_adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personlize_bundle).build();
                }else if (eu_consent_Helper.ad_type.equals("2"))
                {
                    banner_manager_request = (AdManagerAdRequest) new AdManagerAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personlize_bundle).build();
                }

            } else {
                if (eu_consent_Helper.ad_type.equals("1"))
                {
                    banner_adRequest = new AdRequest.Builder().build();
                }else if (eu_consent_Helper.ad_type.equals("2"))
                {
                    banner_manager_request = new AdManagerAdRequest.Builder().build();
                }

            }
            if (eu_consent_Helper.ad_type.equals("1"))
            {
                adView = new AdView(SearchStregthActivty.this);
                AdSize adSize = getAdSize();
                adView.setAdSize(adSize);
                //  adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                adView.setAdUnitId(eu_consent_Helper.admob_rectangle_ad_unit);
                adView.loadAd(banner_adRequest);

           /* AdView adView1 = new AdView(TestActivity.this);
            AdSize adSize = getAdSize();
            adView1.setAdSize(adSize);
            //adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            adView1.setAdUnitId(eu_consent_Helper.admob_rectangle_ad_unit);
            adView1.loadAd(banner_adRequest);*/

                //Banner Ad Start //
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rel_ad_layout = (RelativeLayout) findViewById(R.id.ad_layout);
                rel_ad_layout.addView(adView, params);
            }else if (eu_consent_Helper.ad_type.equals("2"))
            {
                adManagerAdView = new AdManagerAdView(SearchStregthActivty.this);
                AdSize adSize = getAdSize();
                adManagerAdView.setAdSize(adSize);
                //adManagerAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                adManagerAdView.setAdUnitId(eu_consent_Helper.admob_banner_ad_unit);
                adManagerAdView.loadAd(banner_manager_request);

           /* AdView adView1 = new AdView(TestActivity.this);
            AdSize adSize = getAdSize();
            adView1.setAdSize(adSize);
            //adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            adView1.setAdUnitId(eu_consent_Helper.admob_rectangle_ad_unit);
            adView1.loadAd(banner_adRequest);*/

                //Banner Ad Start //
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rel_ad_layout = (RelativeLayout) findViewById(R.id.ad_layout);
                rel_ad_layout.addView(adManagerAdView, params);
            }



            //RelativeLayout rel_ad_layout1 = (RelativeLayout) findViewById(R.id.ad_layout1);
            //rel_ad_layout1.addView(adView1, params);
            //Banner Ad End //

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }



    private AdSize getAdSize()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
        {
            // Do something for lollipop and above versions
            int windowWidth = getResources().getConfiguration().screenWidthDp;

            // Step 3 - Get adaptive ad size and return for setting on the ad view.
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, windowWidth);
        }
        else
        {
            // Step 2 - Determine the screen width (less decorations) to use for the ad width.
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);

            float widthPixels = outMetrics.widthPixels;
            float density = outMetrics.density;

            int adWidth = (int) (widthPixels / density);

            // Step 3 - Get adaptive ad size and return for setting on the ad view.
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
        }
    }
}