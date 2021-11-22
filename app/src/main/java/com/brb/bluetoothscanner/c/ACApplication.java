
package com.brb.bluetoothscanner.c;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.appizona.yehiahd.fastsave.FastSave;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ACApplication extends MultiDexApplication {



	private static ACApplication fire_base_app;

	static {
		System.loadLibrary("native-lib");
	}

	public native String StringAdmobCode();

	public native String StringFBCode();
	public native String StringAPI();
	AppOpenManager appOpenManager;

	@Override
	public void onCreate() {

		super.onCreate();

		fire_base_app = this;

		AudienceNetworkAds.initialize(getApplicationContext());
		AudienceNetworkAds.isInAdsProcess(getApplicationContext());
		appOpenManager = new AppOpenManager(fire_base_app);
		// MobileAds.initialize(getApplicationContext(), getApplicationContext().getString(R.string.admob_app_id));
		MobileAds.initialize(this, initializationStatus -> { });
		FastSave.init(getApplicationContext());

		boolean check_gPlaystore_user = verifyInstallerId(getApplicationContext());
		FastSave.getInstance().saveBoolean(eu_consent_Helper.GOOGLE_PLAY_STORE_USER_KEY, true);
		eu_consent_Helper.ad_api = StringAPI();
		/*String admobid = StringAdmobCode();
		String[] split = admobid.split("::");

		eu_consent_Helper.admob_pub_id=split[0];
		eu_consent_Helper.admob_app_id=split[1];
		eu_consent_Helper.admob_banner_ad_unit=split[2];
		eu_consent_Helper.admob_interstial_ad_id=split[3];
		eu_consent_Helper.ad_mob_native_ad_id=split[4];
		eu_consent_Helper.MERCHANT_ID=split[5];
		eu_consent_Helper.Inapp_PublicKey=split[6];
		eu_consent_Helper.firebase_key=split[7];
		eu_consent_Helper.admob_rectangle_ad_unit=split[8];
		eu_consent_Helper.ad_mob_open_ad_id=split[9];

		String fbid = StringFBCode();
		String[] splitFB = fbid.split("::");
		eu_consent_Helper.fb_native_banner_id = splitFB[0];
		eu_consent_Helper.fb_native_id = splitFB[1];
		eu_consent_Helper.fb_interstitial_id = splitFB[2];

		FastSave.getInstance().saveString(eu_consent_Helper.banner_code, eu_consent_Helper.admob_banner_ad_unit);
		FastSave.getInstance().saveString(eu_consent_Helper.interstitial_code, eu_consent_Helper.admob_interstial_ad_id);
		FastSave.getInstance().saveString(eu_consent_Helper.native_code, eu_consent_Helper.ad_mob_native_ad_id);
		FastSave.getInstance().saveString(eu_consent_Helper.reward_code, "");
		FastSave.getInstance().saveString(eu_consent_Helper.open_code, eu_consent_Helper.ad_mob_open_ad_id);
		FastSave.getInstance().saveString(eu_consent_Helper.adtype, "2");
		FastSave.getInstance().saveString(eu_consent_Helper.pub_id_code, eu_consent_Helper.admob_pub_id);*/

	}



	public static boolean verifyInstallerId(Context context) {
		// A list with valid installers package name
		List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));
		// The package name of the app that has installed your app
		//final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());
		final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());
		// true if your app has been downloaded from Play Store
		return installer != null && validInstallers.contains(installer);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public static synchronized ACApplication getInstance() {
		return fire_base_app;
	}
}
