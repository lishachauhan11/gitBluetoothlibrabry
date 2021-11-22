package com.brb.bluetoothscanner.c;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

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
import com.brb.bluetoothscanner.c.ui.main.MainActivity;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity implements PurchasesUpdatedListener, PurchasesResponseListener {

    String TAG = "SplashActivity :";

    Typeface roboto_font_type;

    private static AppOpenManager appOpenManager;

    InterstitialAd admob_interstitial;
    AdRequest adRequest;

    private AdManagerInterstitialAd admanager_interstitial;
    AdManagerAdRequest interstitial_adRequest;
    boolean Ad_Show = false;

    //private BillingProcessor billing_process;
    boolean readyToPurchase = false;
    private static final String LOG_TAG = "iabv3";

    public String appName;
    public String desc_data;
    public String care_data;
    public String ask_continue_data;
    public String learn_more;

    boolean check = false;
    protected static final int BUY_REQUEST_CODE = 10001;
    private BillingClient mBillingClient;

    List<String> addressList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
    }

    private void setView() {
        // TODO Auto-generated method stub
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_splash);


        appName = getApplicationContext().getString(R.string.app_name);
        eu_consent_Helper.is_show_open_ad=false;
        //desc_data = "You can change your choice anytime for " + appName + " in the app settings.Our partners collect data and use a unique identifier on your device to show you ads.";
        desc_data = "Inline with the new Data protection and security service, we need your consent to serve ads tailored for you.";
        care_data = "We care about your privacy & data security.We keep this app free by showing ads.";
        ask_continue_data = "Can we continue to use yor data to tailor ads for you?";
        learn_more = "Privacy & Policy" + "\n" + "How App & our partners uses your data!";
        roboto_font_type = Typeface.createFromAsset(getAssets(), eu_consent_Helper.roboto_font_path);

        //mRequestQueue = Volley.newRequestQueue(this);





        //ProgressDialog pDialog = new ProgressDialog(this);
        //pDialog.setMessage("Loading...PLease wait");
        //pDialog.show();

        new Thread(new Runnable() {
            public void run() {
                addressList = getTextFromWeb(eu_consent_Helper.ad_api); // format your URL
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //update ui
                        // getTextFromWeb();
                        if (!eu_consent_Class.isOnline(SplashActivity.this)) {

                        } else {
                            if (addressList.size()==0)
                            {
                                FastSave.getInstance().saveString(eu_consent_Helper.banner_code, "");
                                FastSave.getInstance().saveString(eu_consent_Helper.interstitial_code, "");
                                FastSave.getInstance().saveString(eu_consent_Helper.native_code, "");
                                FastSave.getInstance().saveString(eu_consent_Helper.reward_code, "");
                                FastSave.getInstance().saveString(eu_consent_Helper.open_code, "");
                                FastSave.getInstance().saveString(eu_consent_Helper.adtype, "");
                                FastSave.getInstance().saveString(eu_consent_Helper.pub_id_code, "");
                            }else
                            {

                                String[] split = addressList.get(0).split("::");
                                if (split.length==0)
                                {

                                }else {
                                    eu_consent_Helper.admob_pub_id = "pub-6921395385780773";

                                    /*eu_consent_Helper.admob_banner_ad_unit = split[0];
                                    eu_consent_Helper.admob_interstial_ad_id = split[1];
                                    eu_consent_Helper.ad_mob_native_ad_id = split[2];
                                    eu_consent_Helper.ad_mob_reward_ad_id = split[3];
                                    eu_consent_Helper.ad_mob_open_ad_id = split[4];
                                    eu_consent_Helper.ad_type = split[5];

                                    eu_consent_Helper.admob_rectangle_ad_unit = split[0];
                                    FastSave.getInstance().saveString(eu_consent_Helper.banner_code, split[0]);
                                    FastSave.getInstance().saveString(eu_consent_Helper.interstitial_code, split[1]);
                                    FastSave.getInstance().saveString(eu_consent_Helper.native_code, split[2]);
                                    FastSave.getInstance().saveString(eu_consent_Helper.reward_code, split[3]);
                                    FastSave.getInstance().saveString(eu_consent_Helper.open_code, split[4]);
                                    FastSave.getInstance().saveString(eu_consent_Helper.adtype, split[5]);
                                    FastSave.getInstance().saveString(eu_consent_Helper.pub_id_code, eu_consent_Helper.admob_pub_id);*/
                                    if (split.length>=1)
                                    {
                                        eu_consent_Helper.admob_banner_ad_unit = split[0];
                                        eu_consent_Helper.admob_rectangle_ad_unit = split[0];
                                    }else
                                    {
                                        eu_consent_Helper.admob_banner_ad_unit="";
                                        eu_consent_Helper.admob_rectangle_ad_unit = "";
                                    }
                                    if (split.length>=2)
                                    {
                                        eu_consent_Helper.admob_interstial_ad_id = split[1];
                                    }else
                                    {
                                        eu_consent_Helper.admob_interstial_ad_id="";
                                    }
                                    if (split.length>=3)
                                    {
                                        eu_consent_Helper.ad_mob_native_ad_id = split[2];
                                    }else
                                    {
                                        eu_consent_Helper.ad_mob_native_ad_id="";
                                    }
                                    if (split.length>=4)
                                    {
                                        eu_consent_Helper.ad_mob_reward_ad_id = split[3];
                                    }else
                                    {
                                        eu_consent_Helper.ad_mob_reward_ad_id="";
                                    }
                                    if (split.length>=5)
                                    {
                                        eu_consent_Helper.ad_mob_open_ad_id = split[4];
                                    }else
                                    {
                                        eu_consent_Helper.ad_mob_open_ad_id="";
                                    }

                                    if (split.length>=6)
                                    {
                                        eu_consent_Helper.ad_type = split[5];
                                    }else
                                    {
                                        eu_consent_Helper.ad_type="";
                                    }
                                    // FastSave.getInstance().saveString(eu_consent_Helper.pub_id_code, eu_consent_Helper.admob_pub_id);
                                    FastSave.getInstance().saveString(eu_consent_Helper.banner_code, eu_consent_Helper.admob_banner_ad_unit);
                                    FastSave.getInstance().saveString(eu_consent_Helper.interstitial_code, eu_consent_Helper.admob_interstial_ad_id);
                                    FastSave.getInstance().saveString(eu_consent_Helper.native_code, eu_consent_Helper.ad_mob_native_ad_id);
                                    FastSave.getInstance().saveString(eu_consent_Helper.reward_code, eu_consent_Helper.ad_mob_reward_ad_id);
                                    FastSave.getInstance().saveString(eu_consent_Helper.open_code, eu_consent_Helper.ad_mob_open_ad_id);
                                    FastSave.getInstance().saveString(eu_consent_Helper.adtype, eu_consent_Helper.ad_type);
                                    FastSave.getInstance().saveString(eu_consent_Helper.pub_id_code, eu_consent_Helper.admob_pub_id);

                                    // appOpenManager = new AppOpenManager(SplashActivity.this);
                                }
                            }

                        }


                        mBillingClient = BillingClient.newBuilder(SplashActivity.this).enablePendingPurchases().setListener(SplashActivity.this::onPurchasesUpdated).build();
                        mBillingClient.startConnection(new BillingClientStateListener() {
                            @Override
                            public void onBillingSetupFinished(BillingResult billingResult) {
                                try {
                                    mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, new PurchasesResponseListener() {
                                        @Override
                                        public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
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
                                                        check = true;

                                                    }else
                                                    {
                                                        check = false;
                                                    }
                                                }
                                            }
                                        }
                                    });
                  /*  if (mBillingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList() != null) {
                        if (mBillingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList().toString().contains(eu_consent_Helper.remove_ads_sku)) {
                            //Toast.makeText(SplashActivity.this, "already purchased", Toast.LENGTH_SHORT).show();
                            check = true;
                        } else {
                            check = false;
                            //Toast.makeText(SplashActivity.this, "not purchased", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onBillingServiceDisconnected() {

                            }
                        });

                        queryPurchases();

                        Log.e("checkval", "" + check);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            // Using handler with postDelayed called runnable run method
                            @Override
                            public void run() {
                                if (check) {
                                    Log.e("clean12345", "yes");
                                    FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY, true);
                                    ContinueWithoutAdsProcess();
                                } else {
                                    Log.e("clean12345", "no");
                                    boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
                                    if (!is_hide_ads) {
                                        boolean is_online = eu_consent_Class.isOnline(SplashActivity.this);
                                        if (is_online) {
                                            boolean is_consent_set = FastSave.getInstance().getBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY, false);
                                            if (is_consent_set) {
                                                if (addressList.size()==0)
                                                {
                                                    ContinueWithoutAdsProcess();
                                                }else
                                                {
                                                    ContinueAdsProcess();
                                                }

                                            } else {
                                                DoConsentProcess();
                                            }
                                        } else {
                                            ContinueWithoutAdsProcess();
                                        }
                                    } else {
                                        ContinueWithoutAdsProcess();
                                    }
                                }
                            }
                        }, 5 * 1000);
                    }
                });
            }
        }).start();

        /*JsonObjectRequest
                jsonObjReq
                = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener() {

                    @Override
                    public void onResponse(Object response) {
                        Log.e("txtresponse", response.toString());
                        //pDialog.hide();
                        try {
                            JSONObject json= (JSONObject) new JSONTokener(response.toString()).nextValue();
                            JSONArray jsonArray=json.getJSONArray("circledata");
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            eu_consent_Helper.admob_pub_id=jsonObject.getString("app_id");
                            eu_consent_Helper.admob_app_id=jsonObject.getString("app_id");
                            eu_consent_Helper.admob_banner_ad_unit=jsonObject.getString("banner_id");
                            eu_consent_Helper.admob_interstial_ad_id=jsonObject.getString("interstitial_id");
                            eu_consent_Helper.ad_mob_native_ad_id=jsonObject.getString("native_id");
							*//*eu_consent_Helper.MERCHANT_ID=jsonObject.getString("app_id");
							eu_consent_Helper.Inapp_PublicKey=jsonObject.getString("app_id");
							eu_consent_Helper.firebase_key=jsonObject.getString("app_id");*//*
                            eu_consent_Helper.admob_rectangle_ad_unit=jsonObject.getString("rectangle_id");



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }


                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d("TAG", "Error: "
                                + error.getMessage());
                        //pDialog.hide();

                        mBillingClient = BillingClient.newBuilder(SplashActivity.this).enablePendingPurchases().setListener(SplashActivity.this::onPurchasesUpdated).build();
                        mBillingClient.startConnection(new BillingClientStateListener() {
                            @Override
                            public void onBillingSetupFinished(BillingResult billingResult) {
                                try {
                                    if (mBillingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList() != null) {
                                        if (mBillingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList().toString().contains(eu_consent_Helper.remove_ads_sku)) {
                                            //Toast.makeText(SplashActivity.this, "already purchased", Toast.LENGTH_SHORT).show();
                                            check = true;
                                        } else {
                                            check = false;
                                            //Toast.makeText(SplashActivity.this, "not purchased", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onBillingServiceDisconnected() {

                            }
                        });

                        queryPurchases();

                        Log.e("checkval", "" + check);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                        {
                            // Using handler with postDelayed called runnable run method
                            @Override
                            public void run() {
                                if (check)
                                {
                                    Log.e("clean12345","yes");
                                    FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,true);
                                    ContinueWithoutAdsProcess();
                                }
                                else
                                {
                                    Log.e("clean12345", "no");
                                    boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
                                    if (!is_hide_ads) {
                                        boolean is_online = eu_consent_Class.isOnline(SplashActivity.this);
                                        if (is_online) {
                                            boolean is_consent_set = FastSave.getInstance().getBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY, false);
                                            if (is_consent_set) {
                                                ContinueAdsProcess();
                                            } else {
                                                DoConsentProcess();
                                            }
                                        } else {
                                            ContinueWithoutAdsProcess();
                                        }
                                    } else {
                                        ContinueWithoutAdsProcess();
                                    }
                                }
                            }
                        }, 5 * 1000);
                    }
                }) {

            @Override
            protected Map getParams()
            {
                Map params = new HashMap();
                params.put("id", "1");


                return params;
            }

        };

        mRequestQueue.add(jsonObjReq);*/


    }


    private void DoConsentProcess() {
        //String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();

        ShowAdMobConsentDialog(false);

        /*ConsentInformation consentInformation = ConsentInformation.getInstance(this);

        String[] publisherIds = {eu_consent_Helper.admob_pub_id};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                boolean is_user_eea = ConsentInformation.getInstance(SplashActivity.this).isRequestLocationInEeaOrUnknown();
                if (is_user_eea) {
                    Log.e(TAG, "User is from EEA!");
                    FastSave.getInstance().saveBoolean(eu_consent_Helper.EEA_USER_KEY, true);

                    if (consentStatus == ConsentStatus.PERSONALIZED) {
                        Log.e("Consent Status :", "User approve PERSONALIZED Ads!");
                        ConsentInformation.getInstance(SplashActivity.this).setConsentStatus(ConsentStatus.PERSONALIZED);

                        FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
                        FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, false);
                        FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY, true);

                        ContinueAdsProcess();
                    } else if (consentStatus == ConsentStatus.NON_PERSONALIZED) {
                        Log.e(TAG, "User approve NON_PERSONALIZED Ads!");
                        ConsentInformation.getInstance(SplashActivity.this).setConsentStatus(ConsentStatus.NON_PERSONALIZED);

                        FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
                        FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, true);
                        FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY, true);

                        ContinueAdsProcess();
                    } else if (consentStatus == ConsentStatus.UNKNOWN) {
                        Log.e(TAG, "User has neither granted nor declined consent!");
                        ShowAdMobConsentDialog(false);
                    }
                } else {
                    Log.e(TAG, "User is not from EEA!");
                    FastSave.getInstance().saveBoolean(eu_consent_Helper.EEA_USER_KEY, false);
                    ContinueAdsProcess();
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
                Log.e("Consent Status :", "Status Failed :" + errorDescription);
                boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
                if (!is_hide_ads) {
                    ContinueAdsProcess();
                } else {
                    ContinueWithoutAdsProcess();
                }
            }
        });*/
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void ShowAdMobConsentDialog(boolean showCancel) {
        final Dialog eu_consent_dialog = new Dialog(SplashActivity.this, R.style.TransparentBackground);
        eu_consent_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        eu_consent_dialog.setContentView(R.layout.eu_consent_custom);

        eu_consent_dialog.setCancelable(showCancel);

        TextView txt_app_name = (TextView) eu_consent_dialog.findViewById(R.id.eudialog_txt_appname);
        TextView txt_care = (TextView) eu_consent_dialog.findViewById(R.id.eudialog_txt_care);
        TextView txt_ask_continue = (TextView) eu_consent_dialog.findViewById(R.id.eudialog_txt_askcontinue);
        TextView txt_desc = (TextView) eu_consent_dialog.findViewById(R.id.eudialog_txt_desc);
        TextView txt_learn_more = (TextView) eu_consent_dialog.findViewById(R.id.eudialog_lbl_learnmore);

        TextView lbl_continue_ads = (TextView) eu_consent_dialog.findViewById(R.id.eudialog_lbl_continue);
        TextView lbl_non_personalize = (TextView) eu_consent_dialog.findViewById(R.id.eudialog_lbl_irrelevant);
        TextView lbl_remove_ads = (TextView) eu_consent_dialog.findViewById(R.id.eudialog_lbl_removeads);
        TextView lbl_exit = (TextView) eu_consent_dialog.findViewById(R.id.eudialog_lbl_exit);

        txt_app_name.setTypeface(roboto_font_type);
        txt_care.setTypeface(roboto_font_type);
        // txt_ask_continue.setTypeface(roboto_font_type);
        txt_desc.setTypeface(roboto_font_type);
        txt_learn_more.setTypeface(roboto_font_type);

        lbl_continue_ads.setTypeface(roboto_font_type);
        lbl_non_personalize.setTypeface(roboto_font_type);
        lbl_remove_ads.setTypeface(roboto_font_type);
        lbl_exit.setTypeface(roboto_font_type);

        RelativeLayout rel_personalize = eu_consent_dialog.findViewById(R.id.eudialog_rel_continue);
        RelativeLayout rel_non_personalize = eu_consent_dialog.findViewById(R.id.eudialog_rel_irrelevant);
        RelativeLayout rel_remove_ads = eu_consent_dialog.findViewById(R.id.eudialog_rel_removeads);
        RelativeLayout rel_exit = eu_consent_dialog.findViewById(R.id.eudialog_rel_exit);

        txt_app_name.setText(appName);
        txt_care.setText(care_data);
        txt_ask_continue.setText(ask_continue_data);
        txt_desc.setText(desc_data);
        txt_learn_more.setText(learn_more);

        rel_personalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eu_consent_dialog.cancel();

                String toast_message = "Thank you for continue to see personalize ads!";
                eu_consent_Class.ShowSuccessToast(SplashActivity.this, toast_message);

                ConsentInformation.getInstance(SplashActivity.this).setConsentStatus(ConsentStatus.PERSONALIZED);

                FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
                FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, false);
                FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY, true);

                boolean is_g_user = FastSave.getInstance().getBoolean(eu_consent_Helper.GOOGLE_PLAY_STORE_USER_KEY, false);
                if (is_g_user) {
                    ContinueAdsProcess();
                } else {
                    ContinueWithoutAdsProcess();
                }
            }
        });

        rel_non_personalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eu_consent_dialog.cancel();

                String toast_message = "Thank you for continue to see non-personalize ads!";
                eu_consent_Class.ShowSuccessToast(SplashActivity.this, toast_message);

                ConsentInformation.getInstance(SplashActivity.this).setConsentStatus(ConsentStatus.NON_PERSONALIZED);

                FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
                FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, true);
                FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY, true);

                boolean is_g_user = FastSave.getInstance().getBoolean(eu_consent_Helper.GOOGLE_PLAY_STORE_USER_KEY, false);
                if (is_g_user) {
                    ContinueAdsProcess();
                } else {
                    ContinueWithoutAdsProcess();
                }
            }
        });

        rel_remove_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eu_consent_dialog.cancel();

                List<String> skuList = new ArrayList<>();
                skuList.add(eu_consent_Helper.remove_ads_sku);
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                mBillingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(BillingResult billingResult,
                                                             List<SkuDetails> skuDetailsList) {
                                // Process the result.

                                for (SkuDetails skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();
                                    if (eu_consent_Helper.remove_ads_sku.equals(sku)) {
                                        //premiumUpgradePrice = price;
                                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                .setSkuDetails(skuDetails)
                                                .build();
                                        int responseCode = mBillingClient.launchBillingFlow(SplashActivity.this, flowParams).getResponseCode();
                                    }
                                }

                            }
                        });
            }
        });

        rel_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eu_consent_dialog.cancel();
                ExitApp();
            }
        });

        txt_learn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eu_consent_Helper.privacy_policy_url));
                startActivity(browserIntent);
            }
        });

        eu_consent_dialog.show();

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (!billing_process.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String message)
    {
        eu_consent_Class.ShowInfoToast(this,message);
    }

    private void updateViews()
    {
        if (billing_process.isPurchased(eu_consent_Helper.REMOVE_ADS_KEY))
        {
            *//*String toast_message = "Ads removed successfully!";
            AngelClass.ShowSuccessToast(SplashActivity.this, toast_message);*//*

            FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,true);

            ContinueWithoutAdsProcess();
        }
    }*/

    private void ContinueAdsProcess() {
        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                if (Ad_Show == true) {
                    HomeScreen();
                }
            }
        }, 15 * 1000); // wait for 5 seconds

        try {
            if (eu_consent_Helper.ad_type.equals("1"))
            {
                LoadAdMobInterstitialAd();
            }else if (eu_consent_Helper.ad_type.equals("2"))
            {
                LoadAdXInterstitialAd();
            }
        }catch (Exception e)
        {

        }


    }

    private void ContinueWithoutAdsProcess() {
        // eu_consent_Helper.is_ad_closed = true;
        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                HomeScreen();
            }
        }, 3 * 1000); // wait for 5 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void LoadAdMobInterstitialAd() {
        // TODO Auto-generated method stub
        Bundle non_personalize_bundle = new Bundle();
        non_personalize_bundle.putString("npa", "1");

        boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, false);
        if (is_show_non_personalize) {
            adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
        } else {
            adRequest = new AdRequest.Builder().build();
            //interstitial_adRequest = (AdManagerAdRequest) new AdManagerAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
        }
        Ad_Show = true;

        //Interstitial Ad Start //
        InterstitialAd.load(this, eu_consent_Helper.admob_interstial_ad_id, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                admob_interstitial = interstitialAd;
                if (Ad_Show == true) {
                    if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        HomeScreenWithoutFinish();
                        Ad_Show = false;
                        DisplayInterstitialAd();
                        overridePendingTransition(0, 0);
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                admob_interstitial = null;
                eu_consent_Helper.is_ad_closed = true;
                new Handler().postDelayed(new Runnable() {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run() {
                        if (Ad_Show == true) {
                            HomeScreen();
                        }
                    }
                }, 3 * 1000); // wait for 5 seconds
            }
        });
        //Interstitial Ad End //

        /*ad_mob_interstitial = new InterstitialAd(this);
        ad_mob_interstitial.setAdUnitId(EUGeneralHelper.ad_mob_interstitial_ad_id);
        ad_mob_interstitial.loadAd(interstitial_adRequest);

        ad_mob_interstitial.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                // Change the button text and enable the button.
                if (ad_mob_interstitial.isLoaded())
                {
                    if (Ad_Show == true)
                    {
                        if(ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
                        {
                            HomeScreenWithoutFinish();
                            Ad_Show = false;
                            ad_mob_interstitial.show();
                            overridePendingTransition(0,0);
                        }
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError)
            {
                super.onAdFailedToLoad(loadAdError);
                EUGeneralHelper.is_ad_closed = true;
                new Handler().postDelayed(new Runnable()
                {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run()
                    {
                        if(Ad_Show == true)
                        {
                            HomeScreen();
                        }
                    }
                }, 3 * 1000); // wait for 5 seconds
            }

            @Override
            public void onAdClosed()
            {
                EUGeneralHelper.is_ad_closed = true;
                finish();
            }

            @Override
            public void onAdOpened()
            {
                // TODO Auto-generated method stub
                super.onAdOpened();
            }
        });*/
    }


    private void LoadAdXInterstitialAd() {
        // TODO Auto-generated method stub
        Bundle non_personalize_bundle = new Bundle();
        non_personalize_bundle.putString("npa", "1");

        boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY, false);
        if (is_show_non_personalize) {
            interstitial_adRequest = (AdManagerAdRequest) new AdManagerAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
        } else {
            interstitial_adRequest = new AdManagerAdRequest.Builder().build();
            //interstitial_adRequest = (AdManagerAdRequest) new AdManagerAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
        }
        Ad_Show = true;

        //Interstitial Ad Start //
        AdManagerInterstitialAd.load(this, eu_consent_Helper.admob_interstial_ad_id, interstitial_adRequest, new AdManagerInterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                admanager_interstitial = interstitialAd;
                if (Ad_Show == true) {
                    if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        HomeScreenWithoutFinish();
                        Ad_Show = false;
                        DisplayInterstitialAd();
                        overridePendingTransition(0, 0);
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                admanager_interstitial = null;
                eu_consent_Helper.is_ad_closed = true;
                new Handler().postDelayed(new Runnable() {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run() {
                        if (Ad_Show == true) {
                            HomeScreen();
                        }
                    }
                }, 3 * 1000); // wait for 5 seconds
            }
        });
        //Interstitial Ad End //

        /*ad_mob_interstitial = new InterstitialAd(this);
        ad_mob_interstitial.setAdUnitId(EUGeneralHelper.ad_mob_interstitial_ad_id);
        ad_mob_interstitial.loadAd(interstitial_adRequest);

        ad_mob_interstitial.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                // Change the button text and enable the button.
                if (ad_mob_interstitial.isLoaded())
                {
                    if (Ad_Show == true)
                    {
                        if(ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
                        {
                            HomeScreenWithoutFinish();
                            Ad_Show = false;
                            ad_mob_interstitial.show();
                            overridePendingTransition(0,0);
                        }
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError)
            {
                super.onAdFailedToLoad(loadAdError);
                EUGeneralHelper.is_ad_closed = true;
                new Handler().postDelayed(new Runnable()
                {
                    // Using handler with postDelayed called runnable run method
                    @Override
                    public void run()
                    {
                        if(Ad_Show == true)
                        {
                            HomeScreen();
                        }
                    }
                }, 3 * 1000); // wait for 5 seconds
            }

            @Override
            public void onAdClosed()
            {
                EUGeneralHelper.is_ad_closed = true;
                finish();
            }

            @Override
            public void onAdOpened()
            {
                // TODO Auto-generated method stub
                super.onAdOpened();
            }
        });*/
    }

    private void DisplayInterstitialAd() {
        if (eu_consent_Helper.ad_type.equals("1"))
        {
            if (admob_interstitial != null) {
                admob_interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.e("TAG", "The ad was dismissed.");
                        eu_consent_Helper.is_ad_closed = true;
                        finish();
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
                        admob_interstitial = null;
                        Log.e("TAG", "The ad was shown.");
                    }
                });
            }
            admob_interstitial.show(SplashActivity.this);
        }else if (eu_consent_Helper.ad_type.equals("2"))
        {
            if (admanager_interstitial != null) {
                admanager_interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.e("TAG", "The ad was dismissed.");
                        eu_consent_Helper.is_ad_closed = true;
                        finish();
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
                        admanager_interstitial = null;
                        Log.e("TAG", "The ad was shown.");
                    }
                });
            }
            admanager_interstitial.show(SplashActivity.this);
        }

    }

    private void HomeScreenWithoutFinish() {
        // TODO Auto-generated method stub
        Ad_Show = false;
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
    }

    private void HomeScreen() {
        // TODO Auto-generated method stub
        //eu_consent_Helper.is_ad_closed = true;
        Ad_Show = false;
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ExitApp();
    }

    private void ExitApp() {
        moveTaskToBack(true);
        finish();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
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
                        ContinueWithoutAdsProcess();
//Toast.makeText(SettingsActivity.this,"1",Toast.LENGTH_SHORT).show();
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
                    ContinueWithoutAdsProcess();
                }
            }
        }

    }

    private void queryPurchases() {

        //Method not being used for now, but can be used if purchases ever need to be queried in the future
        /*Purchase.PurchasesResult purchasesResult = */mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP,this);
        /*if (purchasesResult != null) {
            List<Purchase> purchasesList = purchasesResult.getPurchasesList();
            //Log.e("purchaselistsize",""+purchasesList.size());
            if (purchasesList == null) {
                return;
            }
            if (!purchasesList.isEmpty()) {
                for (Purchase purchase : purchasesList) {

                    if (purchase.getSkus().get(0).equals(eu_consent_Helper.remove_ads_sku)) {
                        FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY, true);
                    }
                }
            }
        }*/
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
            Toast.makeText(SplashActivity.this, "1", Toast.LENGTH_SHORT).show();
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d(TAG, "User Canceled" + billingResult.getResponseCode());
            Toast.makeText(SplashActivity.this, "1", Toast.LENGTH_SHORT).show();
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY, true);
            Toast.makeText(SplashActivity.this, "1", Toast.LENGTH_SHORT).show();
        } else {
            //Log.d(TAG, "Other code" + BillingClient.BillingResponseCode);
            Toast.makeText(SplashActivity.this, "1", Toast.LENGTH_SHORT).show();
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

                    }else
                    {

                    }
                }
            }
        }
    }

    public List<String> getTextFromWeb(String urlString) {
        URLConnection feedUrl;
        List<String> placeAddress = new ArrayList<>();

        try {
            feedUrl = new URL(urlString).openConnection();
            InputStream is = feedUrl.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;

            while ((line = reader.readLine()) != null) // read line by line
            {
                placeAddress.add(line); // add line to list
            }
            is.close(); // close input stream

            return placeAddress; // return whatever you need
        } catch (Exception e) {
            e.printStackTrace();
        }

        return placeAddress;
    }

}
