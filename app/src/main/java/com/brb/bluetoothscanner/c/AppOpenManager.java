package com.brb.bluetoothscanner.c;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.appizona.yehiahd.fastsave.FastSave;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks
{
    private static final String LOG_TAG = "AppOpenManager";

    private final ACApplication myApplication;

    private AppOpenAd app_open_ad = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private Activity currentActivity;

    private static boolean isShowingAd = false;
    private long loadTime = 0;

    /**
     * Constructor
     */
    public AppOpenManager(ACApplication application)
    {
        myApplication = application;
        myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    /**
     * Shows the ad if one isn't already showing.
     */
    public void showAdIfAvailable()
    {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY, false);
        if (!is_hide_ads)
        {
            boolean is_g_user = FastSave.getInstance().getBoolean(eu_consent_Helper.GOOGLE_PLAY_STORE_USER_KEY, false);
            if (is_g_user)
            {
                if (!isShowingAd && isAdAvailable())
                {
                    FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback()
                    {
                        @Override
                        public void onAdDismissedFullScreenContent()
                        {
                            // Set the reference to null so isAdAvailable() returns false.
                            app_open_ad = null;
                            isShowingAd = false;
                            fetchAd();
                           eu_consent_Helper.is_show_open_ad = true;
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError)
                        {

                        }

                        @Override
                        public void onAdShowedFullScreenContent()
                        {
                            isShowingAd = true;
                        }
                    };

                    if(eu_consent_Helper.is_show_open_ad)
                    {
                        app_open_ad.setFullScreenContentCallback(fullScreenContentCallback);
                        app_open_ad.show(currentActivity);
                    }
                    else
                    {
                        app_open_ad = null;
                        isShowingAd = false;
                        fetchAd();
                    }
                }
                else
                {
                    fetchAd();
                }
            }
            else
            {
                fetchAd();
            }
        }
        else
        {
            fetchAd();
        }

        /*if (!isShowingAd && isAdAvailable())
        {
            Log.d(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback()
            {
                @Override
                public void onAdDismissedFullScreenContent()
                {
                    // Set the reference to null so isAdAvailable() returns false.
                    app_open_ad = null;
                    isShowingAd = false;
                    fetchAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError)
                {
                }

                @Override
                public void onAdShowedFullScreenContent()
                {
                    isShowingAd = true;
                }
            };
            app_open_ad.setFullScreenContentCallback(fullScreenContentCallback);
            app_open_ad.show(currentActivity);
        }
        else
        {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
        }*/
    }

    /**
     * LifecycleObserver methods
     */
    @OnLifecycleEvent(ON_START)
    public void onStart()
    {
        showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }

    public void fetchAd()
    {
        // We will implement this below.
        if (isAdAvailable())
        {
            return;
        }

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback()
        {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd)
            {
                super.onAdLoaded(appOpenAd);
                app_open_ad = appOpenAd;
                loadTime = (new Date()).getTime();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
            {
                super.onAdFailedToLoad(loadAdError);
            }
        };
      eu_consent_Helper.ad_mob_open_ad_id = FastSave.getInstance().getString(eu_consent_Helper.open_code, "");
        AdRequest openAppAdRequest = getAdRequest();
        AppOpenAd.load(myApplication, eu_consent_Helper.ad_mob_open_ad_id, openAppAdRequest,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    /**
     * Request an ad
     */
    /*public void fetchAd()
    {
        // Have unused ad, no need to fetch another.
        if (isAdAvailable())
        {
            return;
        }

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback()
        {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd)
            {
                super.onAdLoaded(appOpenAd);
                AppOpenManager.this.appOpenAd = appOpenAd;
                AppOpenManager.this.loadTime = (new Date()).getTime();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
            {
                super.onAdFailedToLoad(loadAdError);
                Log.d(LOG_TAG, "failed to load");
            }

        };
        AdRequest request = getAdRequest();
        AppOpenAd.load(myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }*/

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest()
    {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours)
    {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable()
    {
        return app_open_ad != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState)
    {

    }

    @Override
    public void onActivityStarted(Activity activity)
    {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity)
    {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity)
    {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState)
    {

    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {
        currentActivity = null;
    }


}