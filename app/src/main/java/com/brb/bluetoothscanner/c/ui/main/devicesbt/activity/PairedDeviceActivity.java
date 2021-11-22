package com.brb.bluetoothscanner.c.ui.main.devicesbt.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.config.CommonFunction;
import com.brb.bluetoothscanner.c.eu_consent_Class;
import com.brb.bluetoothscanner.c.eu_consent_Helper;
import com.brb.bluetoothscanner.c.ui.main.devicesbt.adapter.PairedDeviceAdapter;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.model.DeviceDataModel;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PairedDeviceActivity extends AppCompatActivity {

    RecyclerView listViewPaired;
    List<DeviceDataModel> arrayListpaired = new ArrayList<>();
    PairedDeviceAdapter adapter;
    BluetoothDevice bdDevice;
    BluetoothClass bdClass;
    BluetoothAdapter bluetoothAdapter = null;
    List<DeviceDataModel> arrayListPairedBluetoothDevices = new ArrayList<>();
    CommonFunction commonFunction;
    ImageView backbutton;
    private ButtonClicked clicked;

    RelativeLayout rel_ad_layout;
    AdRequest banner_adRequest;
    AdManagerAdRequest banner_manager_request;
    AdView adView;
    AdManagerAdView adManagerAdView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paireddevice);
        initObj();
        getPairedDevices();
    }

    private void initObj() {
        listViewPaired = findViewById(R.id.listViewPaired);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        commonFunction = new CommonFunction();
        backbutton = findViewById(R.id.backbutton);
        clicked = new ButtonClicked();
        backbutton.setOnClickListener(clicked);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getPairedDevices() {

        if (bluetoothAdapter.enable()) {
            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if (pairedDevice.size() > 0) {
                for (BluetoothDevice device : pairedDevice) {
                    boolean devicetype = false;

                    BluetoothClass bluetoothClass = device.getBluetoothClass();
                    if (bluetoothClass.getDeviceClass() == BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET) {
                        devicetype = true;
                    }
                    if (bluetoothClass.getDeviceClass() == BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES) {
                        devicetype = true;
                    }
                    if (bluetoothClass.getDeviceClass() == BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE) {
                        devicetype = true;
                    }

                    if (arrayListpaired.size() < 1) {

                        arrayListpaired.add(new DeviceDataModel(device.getName(), device.getAddress(), 0, device, devicetype));
                        arrayListPairedBluetoothDevices.add(new DeviceDataModel(device.getName(), device.getAddress(), 0, device, devicetype));

                        adapter = new PairedDeviceAdapter(PairedDeviceActivity.this, arrayListpaired, new PairedDeviceAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(DeviceDataModel item) {
                                showAlertDialogForUnpaired(item);
                            }
                        });
                        listViewPaired.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                        boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
                        for (int i = 0; i < arrayListpaired.size(); i++) {
                            if (device.getAddress().equals(arrayListpaired.get(i).dadd)) {
//                            arrayListBluetoothDevices.remove(i);
                                flag = false;
                            }
                        }
                        if (flag) {
                            arrayListpaired.add(new DeviceDataModel(device.getName(), device.getAddress(), 0, device, devicetype));
                            arrayListPairedBluetoothDevices.add(new DeviceDataModel(device.getName(), device.getAddress(), 0, device, devicetype));
//                    Log.e("Tag", "weak Device Name: " + device.getName() + " " + device.getAddress() + " " + " rssi: " + rssi + "\n");
                            adapter.add(new DeviceDataModel(device.getName(), device.getAddress(), 0, device, devicetype));
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            }
        } else {
            bluetoothAdapter.enable();
            getPairedDevices();

        }
    }
    public void showAlertDialogForUnpaired(DeviceDataModel item) {
        bdDevice = item.device;

        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_unpaire, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.TransparentDialog);
//
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button btn_yes = dialogView.findViewById(R.id.btn_yes);
        Button btn_no = dialogView.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Boolean removeBonding = commonFunction.removeBond(bdDevice);
                    if (removeBonding) {
                        arrayListpaired.remove(item);
                        adapter.remove(item);
                        adapter.notifyDataSetChanged();
                    }
                    Log.i("Log", "Removed" + removeBonding);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                alertDialog.dismiss();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    class ButtonClicked implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backbutton:
                    finish();
                    break;
                default:
                    break;
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
                        eu_consent_Class.DoConsentProcess(this, PairedDeviceActivity.this);
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
                adView = new AdView(PairedDeviceActivity.this);
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
                adManagerAdView = new AdManagerAdView(PairedDeviceActivity.this);
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
