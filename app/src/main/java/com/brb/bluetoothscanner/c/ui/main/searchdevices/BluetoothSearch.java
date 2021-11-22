package com.brb.bluetoothscanner.c.ui.main.searchdevices;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.appizona.yehiahd.fastsave.FastSave;
import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.eu_consent_Class;
import com.brb.bluetoothscanner.c.eu_consent_Helper;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.activity.SearchBLEDevicesActivity;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.activity.SearchBTDeviceActivity;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class BluetoothSearch extends AppCompatActivity {

    LinearLayout buttonSearch, startScanningButton;
    private ButtonClicked clicked;
    TextView title_type;
    ImageView backbutton;

    //Ad
    RelativeLayout rel_native_ad;

    AdRequest native_ad_request;
    NativeAd admobnativeAd;
    RelativeLayout rel_load;

    AdManagerAdRequest native_manager_request;

    AdManagerInterstitialAd adManagerInterstitialAd;
    AdManagerAdRequest interstitial_manager_request;

    InterstitialAd ad_mob_interstitial;
    AdRequest interstitial_adRequest;
    String click;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_search);
        initObj();
        eu_consent_Helper.is_show_open_ad = true;
        rel_load = findViewById(R.id.rel_load);
        title_type.setText(R.string.search);
    }

    private void initObj() {
        title_type = findViewById(R.id.title_type);
        buttonSearch = findViewById(R.id.buttonSearch);
        startScanningButton = findViewById(R.id.startScanningButton);
        backbutton = findViewById(R.id.backbutton);
        clicked = new ButtonClicked();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        buttonSearch.setOnClickListener(clicked);
        startScanningButton.setOnClickListener(clicked);
        backbutton.setOnClickListener(clicked);
    }

    class ButtonClicked implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonSearch:
                    click="search";
                  /*  Intent intent = new Intent(BluetoothSearch.this, SearchBTDeviceActivity.class);
                    startActivity(intent);*/
                    boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
                    if (!is_hide_ads) {
                        boolean is_online = eu_consent_Class.isOnline(BluetoothSearch.this);
                        if (is_online) {
                            if (eu_consent_Helper.ad_type.equals("1")) {
                                ShowAdMobInterstitialAd();
                            } else if (eu_consent_Helper.ad_type.equals("2")) {
                                ShowAdMobInterstitialAd();
                            }


                        } else {
                            Nextscreen();
                        }

                    } else {
                        Nextscreen();
                    }
                    break;
                case R.id.startScanningButton:
                    click="ble";
                    boolean is_hide_ads1 = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
                    if (!is_hide_ads1) {
                        boolean is_online = eu_consent_Class.isOnline(BluetoothSearch.this);
                        if (is_online) {
                            if (eu_consent_Helper.ad_type.equals("1")) {
                                ShowAdMobInterstitialAd();
                            } else if (eu_consent_Helper.ad_type.equals("2")) {
                                ShowAdMobInterstitialAd();
                            }


                        } else {
                            Nextscreen();
                        }

                    } else {
                        Nextscreen();
                    }
                 /*   Intent intent1 = new Intent(BluetoothSearch.this, SearchBLEDevicesActivity.class);
                    startActivity(intent1);*/
                    break;
                case R.id.backbutton:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    //Ad
    @Override
    protected void onResume() {
        super.onResume();
        eu_consent_Helper.admob_banner_ad_unit = FastSave.getInstance().getString(eu_consent_Helper.banner_code, "");
        eu_consent_Helper.admob_interstial_ad_id = FastSave.getInstance().getString(eu_consent_Helper.interstitial_code, "");
        eu_consent_Helper.ad_mob_native_ad_id = FastSave.getInstance().getString(eu_consent_Helper.native_code, "");
        eu_consent_Helper.ad_mob_reward_ad_id = FastSave.getInstance().getString(eu_consent_Helper.reward_code, "");
        eu_consent_Helper.ad_mob_open_ad_id = FastSave.getInstance().getString(eu_consent_Helper.open_code, "");
        eu_consent_Helper.ad_type = FastSave.getInstance().getString(eu_consent_Helper.adtype, "");
        eu_consent_Helper.pub_id_code = FastSave.getInstance().getString(eu_consent_Helper.pub_id_code, "");
        AdMobConsent();
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
                        AdsProcess();
                    } else {
                        eu_consent_Class.DoConsentProcess(this, BluetoothSearch.this);
                    }
                } else {
                    AdsProcess();
                }
            } else {
                // Hide Ads
                HideViews();
            }
        } else {
            // Hide Ads
            HideViews();
        }
    }

    private void AdsProcess() {
        boolean is_g_user = FastSave.getInstance().getBoolean(eu_consent_Helper.GOOGLE_PLAY_STORE_USER_KEY, false);
        if (is_g_user) {
           /* Handler mHandler = new Handler() {
                public void handleMessage(Message msg) {


                }


            };
            Runnable runnable=new Runnable() {
                @Override
                public void run() {

                    refreshAd();             }
            };
            mHandler.postDelayed(runnable,100);*/
            refreshAd();

            //LoadAdMobNativeAd();
            if (eu_consent_Helper.ad_type.equals("1")) {
                LoadAdMobInterstitialAd();
            } else if (eu_consent_Helper.ad_type.equals("2")) {
                LoadAdManagerInterstitialAd();
            }
        } else {
            // Hide Ads
            HideViews();
        }
    }

    private void HideViews() {
        rel_native_ad = (RelativeLayout) findViewById(R.id.ad_layout);
        rel_native_ad.setVisibility(View.GONE);
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
     */
    private void refreshAd() {
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
                        FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
                        NativeAdView adView =
                                (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);
                        populateNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                        rel_load.setVisibility(View.GONE);
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

                                    }
                                })
                        .build();

        //adLoader.loadAd(new AdRequest.Builder().build());
        Bundle non_personalize_bundle = new Bundle();
        non_personalize_bundle.putString("npa", "1");

        boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, false);
        if (is_show_non_personalize) {
            if (eu_consent_Helper.ad_type.equals("1")) {
                native_ad_request = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
            } else if (eu_consent_Helper.ad_type.equals("2")) {
                native_manager_request = (AdManagerAdRequest) new AdManagerAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
            }

        } else {
            if (eu_consent_Helper.ad_type.equals("1")) {
                native_ad_request = new AdRequest.Builder().build();
            } else if (eu_consent_Helper.ad_type.equals("2")) {
                native_manager_request = new AdManagerAdRequest.Builder().build();
            }

        }
        if (eu_consent_Helper.ad_type.equals("1")) {
            adLoader.loadAd(native_ad_request);
        } else if (eu_consent_Helper.ad_type.equals("2")) {
            adLoader.loadAd(native_manager_request);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (admobnativeAd != null) {
            Log.e("Destroy :", "Native Ad destroyed...");
            admobnativeAd.destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (admobnativeAd != null) {
            Log.e("Pause :", "Native Ad paused...");
            admobnativeAd.destroy();
        }
    }

    private void LoadAdMobInterstitialAd() {
        // TODO Auto-generated method stub
        try {
            Bundle non_personalize_bundle = new Bundle();
            non_personalize_bundle.putString("npa", "1");

            boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, false);
            if (is_show_non_personalize) {
                interstitial_adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
            } else {
                interstitial_adRequest = new AdRequest.Builder().build();
            }

            //Interstitial Ad Start //
            InterstitialAd.load(this, eu_consent_Helper.admob_interstial_ad_id, interstitial_adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    ad_mob_interstitial = interstitialAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    ad_mob_interstitial = null;
                }
            });
            //Interstitial Ad End //
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void LoadAdManagerInterstitialAd() {
        // TODO Auto-generated method stub
        try {
            Bundle non_personalize_bundle = new Bundle();
            non_personalize_bundle.putString("npa", "1");

            boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, false);
            if (is_show_non_personalize) {
                interstitial_manager_request = (AdManagerAdRequest) new AdManagerAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
            } else {
                interstitial_manager_request = new AdManagerAdRequest.Builder().build();
            }

            //Interstitial Ad Start //
            AdManagerInterstitialAd.load(this, eu_consent_Helper.admob_interstial_ad_id, interstitial_manager_request, new AdManagerInterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    adManagerInterstitialAd = interstitialAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    adManagerInterstitialAd = null;
                }
            });
            //Interstitial Ad End //
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        click="back";
        // TODO Auto-generated method stub
        boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
        if (!is_hide_ads) {
            boolean is_online = eu_consent_Class.isOnline(this);
            if (is_online) {
                if (eu_consent_Helper.ad_type.equals("1")) {
                    ShowAdMobInterstitialAd();
                } else if (eu_consent_Helper.ad_type.equals("2")) {
                    ShowAdMobInterstitialAd();
                }


            } else {
                Nextscreen();
            }

        } else {
            Nextscreen();
        }
    }

    private void ShowAdMobInterstitialAd() {
        // TODO Auto-generated method stub
        if (eu_consent_Helper.ad_type.equals("1")) {
            if (ad_mob_interstitial != null) {
                if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    DisplayInterstitialAd();
                } else {
                    Nextscreen();
                }
            } else {
                Nextscreen();
            }
        } else if (eu_consent_Helper.ad_type.equals("2")) {
            if (adManagerInterstitialAd != null) {
                if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    DisplayInterstitialAd();
                } else {
                    Nextscreen();
                }
            } else {
                Nextscreen();
            }
        }

    }

    private void DisplayInterstitialAd() {
        if (eu_consent_Helper.ad_type.equals("1")) {
            if (ad_mob_interstitial != null) {
                ad_mob_interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.e("TAG", "The ad was dismissed.");
                        Nextscreen();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.e("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        ad_mob_interstitial = null;
                        Log.e("TAG", "The ad was shown.");
                    }
                });
            }
            ad_mob_interstitial.show(this);
            eu_consent_Helper.is_show_open_ad = false;
        } else if (eu_consent_Helper.ad_type.equals("2")) {
            if (adManagerInterstitialAd != null) {
                adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.e("TAG", "The ad was dismissed.");
                        Nextscreen();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.e("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        adManagerInterstitialAd = null;
                        Log.e("TAG", "The ad was shown.");
                    }
                });
            }
            adManagerInterstitialAd.show(this);
            eu_consent_Helper.is_show_open_ad = false;
        }

    }

    private void BackScreen() {
        // TODO Auto-generated method stub
        eu_consent_Helper.is_show_open_ad = true;
        finish();
    }

    public void Nextscreen() {
        if (click.equals("back")){
            BackScreen();
        }
        else if(click.equals("search")){
            Intent intent = new Intent(BluetoothSearch.this, SearchBTDeviceActivity.class);
            startActivity(intent);
        }
        else if(click.equals("ble")){
            Intent intent1 = new Intent(BluetoothSearch.this, SearchBLEDevicesActivity.class);
            startActivity(intent1);
        }
    }

}
