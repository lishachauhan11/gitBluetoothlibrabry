package com.brb.bluetoothscanner.c.ui.main;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.appizona.yehiahd.fastsave.FastSave;
import com.brb.bluetoothscanner.c.AppOpenManager;
import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.SettingsActivity;
import com.brb.bluetoothscanner.c.config.CommonFunction;
import com.brb.bluetoothscanner.c.dialog.DialogClass;
import com.brb.bluetoothscanner.c.eu_consent_Class;
import com.brb.bluetoothscanner.c.eu_consent_Helper;
import com.brb.bluetoothscanner.c.ui.main.devicesbt.activity.BluetoothDevices;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.BluetoothSearch;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.activity.ShareWithBTActivity;
import com.github.javiersantos.appupdater.AppUpdater;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener, PurchasesResponseListener {

    LinearLayout bt_devices, bt_search, bt_share;
    boolean isbton = false;
    private ButtonClicked clicked;
    BluetoothAdapter bluetoothAdapter = null;
    CommonFunction commonFunction;
    private BluetoothGatt mGatt;
    ImageView bt_settings;
    LinearLayout btnon_off;
    TextView onoff_text;
    ImageView onoff_img;

    //Ad
    RelativeLayout rel_ad_layout;
    AdRequest banner_adRequest;
    AdManagerAdRequest banner_manager_request;
    AdView adView;
    AdManagerAdView adManagerAdView;
    AppOpenManager appOpenManager;

    NativeAd admobnativeAd;
    AdRequest native_ad_request;
    AdManagerAdRequest native_manager_request;

    // Youtube App internet block by this app.Enable internet click here.
    private BillingClient mBillingClient;
    ConnectivityManager cm;
    ImageView iv_ads,info;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initObj();
        onBluetooth();
        requestPremisstion();
        btnon_off.setTag("true");
        btnon_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnon_off.getTag().toString().equalsIgnoreCase("true")) {
                    onoff_text.setTextColor(getResources().getColor(R.color.services_subtext));
                    onoff_text.setText(R.string.turn_on);
                    btnon_off.setTag("false");
                    offBluetooth();
                    onoff_text.setTextColor(getResources().getColor(R.color.green_on));
                    onoff_img.setImageResource(R.drawable.off);
                } else {
                    btnon_off.setTag("true");
                    onBluetooth();
                    onoff_text.setText(R.string.turn_off);
                    onoff_text.setTextColor(getResources().getColor(R.color.red_off));
                    onoff_img.setImageResource(R.drawable.on);
                }
            }
        });
        cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.start();

        mBillingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener()
        {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult)
            {
            }

            @Override
            public void onBillingServiceDisconnected()
            {

            }
        });

        queryPurchases();
        info = (ImageView) findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* new LicensesDialog.Builder(MainActivity.this)
                        .setNotices(R.raw.notices)
                        .build()
                        .showAppCompat();*/
                Intent iv=new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(iv);
            }
        });

    }

    private void initObj() {
        bt_search = findViewById(R.id.bt_search);
        bt_settings = findViewById(R.id.bt_settings);
        bt_devices = findViewById(R.id.bt_devices);
        onoff_img = findViewById(R.id.onoff_img);
        bt_share = findViewById(R.id.bt_share);
        btnon_off = findViewById(R.id.btnon_off);
        onoff_text = findViewById(R.id.onoff_text);
        commonFunction = new CommonFunction();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        clicked = new ButtonClicked();
    }

    private void requestPremisstion() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        bt_devices.setOnClickListener(clicked);
        bt_settings.setOnClickListener(clicked);
        bt_search.setOnClickListener(clicked);
        bt_share.setOnClickListener(clicked);
    }

    private Boolean connect(BluetoothDevice bdDevice) {
        Boolean bool = false;
        try {
            Log.i("Log", "service method is called ");
            Class cl = Class.forName("android.bluetooth.BluetoothDevice");
            Class[] par = {};
            Method method = cl.getMethod("createBond", par);
            Object[] args = {};
            bool = (Boolean) method.invoke(bdDevice);

        } catch (Exception e) {
            Log.i("Log", "Inside catch of serviceFromDevice Method");
            e.printStackTrace();
        }
        return bool.booleanValue();
    }

    class ButtonClicked implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_search:
                    if (!isbton) {
                        onBluetooth();
                    } else {
                        Intent intent = new Intent(MainActivity.this, BluetoothSearch.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.bt_settings:
                    DialogClass.showAlertDialogForSettings(MainActivity.this, MainActivity.this);
                    break;
                case R.id.bt_devices:
                    if (!isbton) {
                        onBluetooth();
                    } else {
                        Intent intent = new Intent(MainActivity.this, BluetoothDevices.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.bt_share:
                    if (!isbton) {
                        onBluetooth();
                    } else {
                        Intent intent3 = new Intent(MainActivity.this, ShareWithBTActivity.class);
                        startActivity(intent3);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void onBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
            isbton = true;
        } else {
            isbton = true;
            Toast.makeText(this, "Bluetooth is already ON", Toast.LENGTH_SHORT).show();
        }

    }

    private void offBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        } else {
            isbton = false;
            Toast.makeText(this, "Bluetooth is already OFF", Toast.LENGTH_SHORT).show();
        }
    }

   /* @Override
    public void onBackPressed() {
        DialogClass.showAlertDialogForExit(this, this);
    }*/

    //Ad
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
                        eu_consent_Class.DoConsentProcess(this, MainActivity.this);
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
                adView = new AdView(MainActivity.this);
            /*AdSize adSize = getAdSize();
            adView.setAdSize(adSize);*/
                adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
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
                adManagerAdView = new AdManagerAdView(MainActivity.this);
                /*AdSize adSize = getAdSize();
                adManagerAdView.setAdSize(adSize);*/
                adManagerAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                adManagerAdView.setAdUnitId(eu_consent_Helper.admob_rectangle_ad_unit);
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

    private AdSize getAdSize() {
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        /*boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
        if (!is_hide_ads) {
            boolean is_online = eu_consent_Class.isOnline(MainActivity.this);
            if (is_online) {
                // Call Exit Screen
                //MyExitView.OpenExitScreen();

                boolean is_eea_user = FastSave.getInstance().getBoolean(eu_consent_Helper.EEA_USER_KEY, false);
                boolean is_consent_set = FastSave.getInstance().getBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY, false);
                boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, false);
                MyExitView.OpenExitScreen(is_hide_ads, is_eea_user, is_consent_set, is_show_non_personalize, eu_consent_Helper.ad_mob_native_ad_id);
            }
            else {
                eu_consent_Class.ExitDialog(MainActivity.this, MainActivity.this);
            }
        } else {
            eu_consent_Class.ExitDialog(MainActivity.this, MainActivity.this);
        }*/

       // DialogClass.showAlertDialogForExit(this, this);
        ExitDialog();
    }

    RelativeLayout rel_dialog_native_ad;
    public void ExitDialog()
    {
        final Dialog exit_dialog = new Dialog(MainActivity.this, R.style.TransparentBackground);
        exit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exit_dialog.setContentView(R.layout.dialog_exit);

        Button exit_btn_yes = (Button) exit_dialog.findViewById(R.id.dialog_conform_btn_yes);
        Button exit_btn_no = (Button) exit_dialog.findViewById(R.id.dialog_conform_btn_no);
        Button exit_btn_rate = (Button) exit_dialog.findViewById(R.id.dialog_rate_btn_yes);

        TextView exit_txt_header = (TextView) exit_dialog.findViewById(R.id.dialog_conform_txt_header);
        TextView exit_txt_message = (TextView) exit_dialog.findViewById(R.id.dialog_conform_txt_message);

        Typeface font_type =  Typeface.createFromAsset(getAssets(), eu_consent_Helper.roboto_font_path);

        rel_dialog_native_ad = (RelativeLayout)exit_dialog.findViewById(R.id.rel_native_adlayout);
        rel_dialog_native_ad.setVisibility(View.VISIBLE);
     /*   Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {


            }


        };
        Runnable runnable=new Runnable() {
            @Override
            public void run() {

                refreshAd(exit_dialog);             }
        };
        mHandler.postDelayed(runnable,100);*/
        refreshAd(exit_dialog);

        exit_btn_yes.setTypeface(font_type);
        exit_btn_no.setTypeface(font_type);
        exit_btn_rate.setTypeface(font_type);

        exit_txt_header.setTypeface(font_type);
        exit_txt_message.setTypeface(font_type);

        String exit_dialog_header = "Do you want to exit from the app?";
        String exit_dialog_message = "Tell others what you think about this app";

        exit_txt_header.setText(exit_dialog_header);
        exit_txt_message.setText(exit_dialog_message);

        exit_btn_yes.setText("Yes");
        exit_btn_no.setText("No");

        exit_btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String rateUrl = eu_consent_Helper.rate_url + MainActivity.this.getPackageName();
                    Uri uri = Uri.parse(rateUrl);
                    Intent view_intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(view_intent);
                }
                catch (ActivityNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });

        exit_btn_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                eu_consent_Helper.is_show_open_ad = true;
                finish();

                exit_dialog.dismiss();
            }
        });

        exit_btn_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                exit_dialog.dismiss();
            }
        });

        exit_dialog.show();

    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.

                    super.onVideoEnd();
                }
            });
        } else {

        }
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private void refreshAd(Dialog exd) {
        //refresh.setEnabled(false);

        AdLoader.Builder builder = new AdLoader.Builder(this, eu_consent_Helper.ad_mob_native_ad_id);

        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    // OnLoadedListener implementation.
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        //				refresh.setEnabled(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = isDestroyed();
                        }
                        if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                            nativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (admobnativeAd != null) {
                            admobnativeAd.destroy();
                        }
                        admobnativeAd = nativeAd;
                        FrameLayout frameLayout = exd.findViewById(R.id.fl_adplaceholder);
                        NativeAdView adView =
                                (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);
                        populateNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                        //rel_load.setVisibility(View.GONE);
                    }
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        com.google.android.gms.ads.nativead.NativeAdOptions adOptions =
                new com.google.android.gms.ads.nativead.NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        //refresh.setEnabled(true);
                                        String error =
                                                String.format(
                                                        "domain: %s, code: %d, message: %s",
                                                        loadAdError.getDomain(),
                                                        loadAdError.getCode(),
                                                        loadAdError.getMessage());
                                        Toast.makeText(
                                                MainActivity.this,
                                                "Failed to load native ad with error " + error,
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                        .build();

        //adLoader.loadAd(new AdRequest.Builder().build());
        Bundle non_personalize_bundle = new Bundle();
        non_personalize_bundle.putString("npa", "1");

        boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,false);
        if(is_show_non_personalize)
        {
            if (eu_consent_Helper.ad_type.equals("1"))
            {
                native_ad_request = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
            }else if (eu_consent_Helper.ad_type.equals("2"))
            {
                native_manager_request = (AdManagerAdRequest) new AdManagerAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
            }

        }
        else
        {
            if (eu_consent_Helper.ad_type.equals("1"))
            {
                native_ad_request = new AdRequest.Builder().build();
            }else if (eu_consent_Helper.ad_type.equals("2"))
            {
                native_manager_request = new AdManagerAdRequest.Builder().build();
            }

        }
        if (eu_consent_Helper.ad_type.equals("1"))
        {
            adLoader.loadAd(native_ad_request);
        }else if (eu_consent_Helper.ad_type.equals("2"))
        {
            adLoader.loadAd(native_manager_request);
        }


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(admobnativeAd != null)
        {
            Log.e("Destroy :","Native Ad destroyed...");
            admobnativeAd.destroy();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(admobnativeAd != null)
        {
            Log.e("Pause :","Native Ad paused...");
            admobnativeAd.destroy();
        }
    }


    private void ConformPurchaseDialog()
    {
        final Dialog conform_dialog = new Dialog(this,R.style.TransparentBackground);
        conform_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        conform_dialog.setContentView(R.layout.eu_consent_dialog_rate);

        Button conform_dialog_btn_yes = (Button) conform_dialog.findViewById(R.id.dialog_conform_btn_yes);
        Button conform_dialog_btn_no = (Button) conform_dialog.findViewById(R.id.dialog_conform_btn_no);

        TextView conform_dialog_txt_header = (TextView)conform_dialog.findViewById(R.id.dialog_conform_txt_header);
        TextView conform_dialog_txt_message = (TextView)conform_dialog.findViewById(R.id.dialog_conform_txt_message);
        LottieAnimationView animationView = (LottieAnimationView)conform_dialog.findViewById(R.id.progress_lottie);
        animationView.setVisibility(View.GONE);

        Typeface font_type = Typeface.createFromAsset(getAssets(), eu_consent_Helper.roboto_font_path);

        conform_dialog_btn_yes.setTypeface(font_type);
        conform_dialog_btn_no.setTypeface(font_type);

        conform_dialog_txt_header.setTypeface(font_type);
        conform_dialog_txt_message.setTypeface(font_type);

        String conform_dialog_header = "Confirm Your In-App Purchase";
        String conform_dialog_message = "With purchasing this item all ads from application will be removed.";

        conform_dialog_txt_header.setText(conform_dialog_header);
        conform_dialog_txt_message.setText(conform_dialog_message);

        conform_dialog_btn_yes.setText("Buy");
        conform_dialog_btn_no.setText("Cancel");

        conform_dialog_btn_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
					/*billing_process.purchase(InfoActivity.this, eu_consent_Helper.REMOVE_ADS_PRODUCT_ID);
					updateViews();*/

                    List<String> skuList = new ArrayList<>();
                    skuList.add(eu_consent_Helper.remove_ads_sku);
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

                    mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener()
                    {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult,
                                                         List<SkuDetails> skuDetailsList)
                        {
                            // Process the result.
                            for (SkuDetails skuDetails : skuDetailsList)
                            {
                                String sku = skuDetails.getSku();

                                if (eu_consent_Helper.remove_ads_sku.equals(sku))
                                {
                                    //premiumUpgradePrice = price;
                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetails)
                                            .build();
                                    int responseCode = mBillingClient.launchBillingFlow(MainActivity.this, flowParams).getResponseCode();
                                }
                            }

                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                conform_dialog.dismiss();
            }
        });

        conform_dialog_btn_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                conform_dialog.dismiss();
            }
        });

        conform_dialog.show();
    }

    private void handlePurchase(Purchase purchase)
    {

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
// Grant entitlement to the user.
            AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
                @Override
                public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                    Log.e("result",""+billingResult.getResponseCode()+"::"+billingResult.getDebugMessage());

                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK )
                    {
                        FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,true);
                        Hide_Ad_Layout();
//Toast.makeText(InfoActivity.this,"1",Toast.LENGTH_SHORT).show();
                    }
                }
            };

// Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }else
            {
                if (purchase.getSkus().get(0).equals(eu_consent_Helper.remove_ads_sku))
                {
                    FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,true);
                    Hide_Ad_Layout();
                }
            }
        }

    }

    private void queryPurchases()
    {
        //Method not being used for now, but can be used if purchases ever need to be queried in the future
        // Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        /*Purchase.PurchasesResult purchasesResult = */mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP,this );
       /* if (purchasesResult != null)
        {
            List<Purchase> purchasesList = purchasesResult.getPurchasesList();
            if (purchasesList == null)
            {
                return;
            }
            if (!purchasesList.isEmpty())
            {
                for (Purchase purchase : purchasesList)
                {
                    if (purchase.getSkus().equals(eu_consent_Helper.remove_ads_sku))
                    {
                        FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,true);
                    }
                }
            }
        }*/

    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases)
    {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null)
        {
            for (Purchase purchase : purchases)
            {
                handlePurchase(purchase);
            }
            //Toast.makeText(InfoActivity.this,"1",Toast.LENGTH_SHORT).show();
        }
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED)
        {
            // Handle an error caused by a user cancelling the purchase flow.
            // Log.d(TAG, "User Canceled" + billingResult.getResponseCode());
            // Toast.makeText(InfoActivity.this,"2",Toast.LENGTH_SHORT).show();
        }
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED)
        {
            FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,true);
            //Toast.makeText(MainActivity.this,"3", Toast.LENGTH_SHORT).show();
            Hide_Ad_Layout();
        }
        else
        {
            //Log.d(TAG, "Other code" + BillingClient.BillingResponseCode);
            //Toast.makeText(InfoActivity.this,"4",Toast.LENGTH_SHORT).show();
            // Handle any other error codes.
        }
    }

    @Override
    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {

        if (billingResult != null)
        {

            if (list == null)
            {
                return;
            }
            if (!list.isEmpty())
            {
                for (Purchase purchase : list)
                {
                    if (purchase.getSkus().get(0).equals(eu_consent_Helper.remove_ads_sku))
                    {
                        FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,true);
                    }
                }
            }
        }  }


}