package com.brb.bluetoothscanner.c.ui.main.searchdevices.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.config.CommonFunction;
import com.brb.bluetoothscanner.c.eu_consent_Class;
import com.brb.bluetoothscanner.c.eu_consent_Helper;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.model.DeviceDataModel;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.adapter.DeviceDataAdapter;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;

import java.util.ArrayList;
import java.util.List;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class SearchBTDeviceActivity extends AppCompatActivity {


    RecyclerView listViewDetectedWeaker;
    BluetoothDevice bdDevice;
    LinearLayoutCompat nodata_found;
    BluetoothAdapter bluetoothAdapter = null;
    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
    DeviceDataAdapter detectedAdapterweaker ;
    LinearLayout buttonSearch;
    private ButtonClicked clicked;
    CommonFunction commonFunction;
    List<DeviceDataModel> weaklistdata = new ArrayList<>();
    TextView title_type;
    ImageView buttonBack;
    TextView scanning_text;
    PulsatorLayout pulsator;

    RelativeLayout rel_ad_layout;
    AdRequest banner_adRequest;
    AdManagerAdRequest banner_manager_request;
    AdView adView;
    AdManagerAdView adManagerAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbt);

        initObject();
        startSearching();
        title_type.setText(R.string.search);
        pulsator.start();
    }

    private void initObject() {
        title_type = findViewById(R.id.title_type);
        nodata_found = findViewById(R.id.nodata_found);
        listViewDetectedWeaker = findViewById(R.id.listViewDetectedWeaker);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        clicked = new ButtonClicked();
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonBack = findViewById(R.id.backbutton);
        scanning_text = findViewById(R.id.scanning_text);
        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();
        commonFunction = new CommonFunction();
        pulsator = findViewById(R.id.pulsator);
    }

    @Override
    protected void onStart() {
        super.onStart();
        intOnClickList();
    }

    private void intOnClickList() {
        buttonSearch.setOnClickListener(clicked);
        buttonBack.setOnClickListener(clicked);
    }


    private void startSearching() {
        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    class ListItemClicked implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            bdDevice = arrayListBluetoothDevices.get(position);
            //bdClass = arrayListBluetoothDevices.get(position);
            Log.i("Log", "The dvice : " + bdDevice.toString());

            Boolean isBonded = false;
            try {
                isBonded = commonFunction.createBond(bdDevice);
                if (isBonded) {
                    //arrayListpaired.add(bdDevice.getName()+"\n"+bdDevice.getAddress());
                    //adapter.notifyDataSetChanged();
//                    getPairedDevices();
//                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("Tag", "The bond is created: " + isBonded);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }//connect(bdDevice);
        }
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
//            Toast.makeText(context, "Enter in reciver.", Toast.LENGTH_SHORT).show();
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();

                nodata_found.setVisibility(View.GONE);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                Log.e("Tag", "  RSSI: " + rssi + "dBm");
                Log.e("Tag", "" + device.getName() + " " + device.getAddress() + " " + rssi);
                if (device.getName() != null) {
//                if (rssi <= -10 && rssi >= -30) {
//                    stronglistdatafound(device, rssi);
//                } else if (rssi <= -30 && rssi >= -60) {
//                    intetmidiatelistdatafound(device, rssi);
//                } else {
                    weaklistdatafound(device, rssi);
//                }
                }

            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
//                loader.setVisibility(View.VISIBLE);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                buttonSearch.setVisibility(View.VISIBLE);
//                loader.setVisibility(View.GONE);
                if(weaklistdata.isEmpty()){
                    nodata_found.setVisibility(View.VISIBLE);
                }
            }
        }
    };


    class ButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonSearch:
                    if(buttonSearch.getTag().toString().equalsIgnoreCase("true")){
                        buttonSearch.setTag("false");
                        scanning_text.setText(R.string.start_searching);
                        bluetoothAdapter.cancelDiscovery();
                        pulsator.stop();
                    }
                    else {
                        buttonSearch.setTag("true");
                        scanning_text.setText(R.string.stop_searching);
                        startSearching();
                        pulsator.start();
                    }
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
//                    if(item_type == 3){
//                        weaklistdata.remove(item);
//                        detectedAdapterweaker.remove(item);
//                        detectedAdapterweaker.notifyDataSetChanged();
//                    }
                }
            } else {
                Log.e("Tag", "The bond is created: " + isBonded);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }//connect(bdDevice)
    }

    private void weaklistdatafound(BluetoothDevice device, int rssi) {
        if (weaklistdata.size() < 1) {
            weaklistdata.add(
                    new DeviceDataModel(device.getName(),
                            device.getAddress(),
                            rssi, device));
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
                if (device.getAddress().equals(weaklistdata.get(i).dadd)) {
//                            arrayListBluetoothDevices.remove(i);
                    if (weaklistdata.get(i).rssid != rssi) {
                        weaklistdata.get(i).rssid = rssi;
                        detectedAdapterweaker.edit(weaklistdata.get(i), i);
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
                detectedAdapterweaker.add(new DeviceDataModel(device.getName(), device.getAddress(), rssi, device));
                detectedAdapterweaker.notifyDataSetChanged();
            }
        }
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
                        eu_consent_Class.DoConsentProcess(this, SearchBTDeviceActivity.this);
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
                adView = new AdView(SearchBTDeviceActivity.this);
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
                adManagerAdView = new AdManagerAdView(SearchBTDeviceActivity.this);
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
