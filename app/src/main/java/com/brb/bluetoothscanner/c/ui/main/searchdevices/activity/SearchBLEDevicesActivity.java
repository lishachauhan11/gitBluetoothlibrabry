package com.brb.bluetoothscanner.c.ui.main.searchdevices.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class SearchBLEDevicesActivity extends AppCompatActivity {
    //    Button startScanningButton, stopScanningButton;
    LinearLayout startScanningButton;
    ImageView buttonBack;
    PulsatorLayout pulsator;
    BluetoothLeScanner bLeScanner;
    RecyclerView listViewDetectedStronger;
    LinearLayout llstrong, llintermidiate, llweak;
    BluetoothDevice bdDevice;
    CommonFunction commonFunction;
    BluetoothAdapter bluetoothAdapter = null;
    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
    List<DeviceDataModel> stronglistdata = new ArrayList<>();
    SwipeRefreshLayout swipeContainer;
    TextView title_type;
    DeviceDataAdapter  detectedAdapterstronger;
    TextView scanning_text;

    RelativeLayout rel_ad_layout;
    AdRequest banner_adRequest;
    AdManagerAdRequest banner_manager_request;
    AdView adView;
    AdManagerAdView adManagerAdView;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchble_device);
//
        initObject();
        startScanning();
        title_type.setText(R.string.search);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                stronglistdata.clear();
                detectedAdapterstronger.notifyDataSetChanged();
                startScanning();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initObject() {
        swipeContainer = findViewById(R.id.swipeContainer);
        title_type = findViewById(R.id.title_type);
        listViewDetectedStronger = findViewById(R.id.listViewDetectedStronger);
        llstrong = findViewById(R.id.llstrong);
        llintermidiate = findViewById(R.id.llintermidiate);
        llweak = findViewById(R.id.llweak);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        startScanningButton = findViewById(R.id.startScanningButton);
        buttonBack = findViewById(R.id.backbutton);
        pulsator = findViewById(R.id.pulsator);
        scanning_text = findViewById(R.id.scanning_text);
        bLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();
        pulsator.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ButtonClicked clicked = new ButtonClicked();
        buttonBack.setOnClickListener(clicked);
        startScanningButton.setOnClickListener(clicked);
        commonFunction = new CommonFunction();
    }

    class ButtonClicked implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.startScanningButton:
                    if(startScanningButton.getTag().toString().equalsIgnoreCase("true")){
                        startScanningButton.setTag("false");
                        scanning_text.setText(R.string.start_searching);
                        stopScanning();
                        pulsator.stop();
                    }
                    else {
                        startScanningButton.setTag("true");
                        scanning_text.setText(R.string.stop_searching);
                        startScanning();
                        pulsator.start();
                    }
                    break;
                case R.id.backbutton:
                    finish();
                    break;
            }
        }
    }


    public void startScanning() {
//        startScanningButton.setVisibility(View.GONE);
//        stopScanningButton.setVisibility(View.VISIBLE);
        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                bLeScanner.startScan(leScanCallback);
            }
        });
    }

    public void stopScanning() {
//        startScanningButton.setVisibility(View.VISIBLE);
//        stopScanningButton.setVisibility(View.GONE);
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

            Log.e("Tag", "Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() + " ");
//
            stronglistdatafound(result);

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

    public void connectdevice(DeviceDataModel item, int item_type) {

        bdDevice = item.device;
        Boolean isBonded = false;
        try {
            isBonded = commonFunction.createBond(bdDevice);
            if (isBonded) {
//                if(item_type==1){
//                    stronglistdata.remove(item);
//                    detectedAdapterstronger.remove(item);
//                    detectedAdapterstronger.notifyDataSetChanged();
//                }
            } else {
                Log.e("Tag", "The bond is created: " + isBonded);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }//connect(bdDevice)
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
                        eu_consent_Class.DoConsentProcess(this, SearchBLEDevicesActivity.this);
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
                adView = new AdView(SearchBLEDevicesActivity.this);
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
                adManagerAdView = new AdManagerAdView(SearchBLEDevicesActivity.this);
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


