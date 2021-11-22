package com.brb.bluetoothscanner.c;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

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

import java.util.ArrayList;
import java.util.List;

import de.psdev.licensesdialog.LicensesDialog;

@SuppressWarnings("deprecation")
public class SettingsActivity extends Activity implements PurchasesUpdatedListener, PurchasesResponseListener
{
	public static Activity info_activity = null;
	RelativeLayout rel_native_ad;

	AdRequest native_ad_request;
	NativeAd admobnativeAd;
	RelativeLayout rel_load;

	AdManagerAdRequest native_manager_request;

	AdManagerInterstitialAd adManagerInterstitialAd;
	AdManagerAdRequest interstitial_manager_request;

	InterstitialAd ad_mob_interstitial;
	AdRequest interstitial_adRequest;

	Typeface font_type;

	TextView lbl_version_name;
	TextView lbl_ad_free;
	TextView lbl_user_consent;
	TextView lbl_share_app;
	TextView lbl_rate_us;
	TextView lbl_privacy;
	TextView lbl_license;

	TextView txt_version_name;

	RelativeLayout rel_ad_free;
	RelativeLayout rel_user_consent;
	RelativeLayout rel_share_app;
	RelativeLayout rel_rate_us;
	RelativeLayout rel_privacy;
	RelativeLayout rel_license;
	RelativeLayout rel_update;

	View view_below_ad_free;
	View view_below_user_consent;

	boolean is_eea_user = false;

	private BillingClient mBillingClient;

	ImageView img_back;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setview();
	}

	private void setview()
	{
		// TODO Auto-generated method stub
		try
		{
			setContentView(R.layout.activity_info);


			eu_consent_Helper.is_show_open_ad=true;
			info_activity = SettingsActivity.this;
			rel_load = findViewById(R.id.rel_load);
			font_type = Typeface.createFromAsset(getAssets(), eu_consent_Helper.roboto_font_path);

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

			lbl_version_name = (TextView)findViewById(R.id.setting_lbl_version);
			lbl_ad_free = (TextView)findViewById(R.id.setting_lbl_adfree);
			lbl_user_consent = (TextView)findViewById(R.id.setting_lbl_admobconsent);
			lbl_share_app = (TextView)findViewById(R.id.setting_lbl_shareapp);
			lbl_rate_us = (TextView)findViewById(R.id.setting_lbl_rateus);
			lbl_privacy = (TextView)findViewById(R.id.setting_lbl_privacy);
			lbl_license = (TextView)findViewById(R.id.setting_lbl_license);

			txt_version_name = (TextView)findViewById(R.id.setting_txt_version);

			rel_ad_free = (RelativeLayout) findViewById(R.id.setting_rel_adfree);
			rel_user_consent = (RelativeLayout) findViewById(R.id.setting_rel_admobconsent);
			rel_share_app = (RelativeLayout) findViewById(R.id.setting_rel_shareapp);
			rel_rate_us = (RelativeLayout) findViewById(R.id.setting_rel_rateus);
			rel_privacy = (RelativeLayout) findViewById(R.id.setting_rel_privacy);
			rel_license = (RelativeLayout) findViewById(R.id.setting_rel_license);

			view_below_ad_free = (View) findViewById(R.id.setting_view_1);
			view_below_user_consent = (View) findViewById(R.id.setting_view_3);

			/*lbl_version_name.setTypeface(font_type);
			lbl_ad_free.setTypeface(font_type);
			lbl_user_consent.setTypeface(font_type);
			lbl_share_app.setTypeface(font_type);
			lbl_rate_us.setTypeface(font_type);
			lbl_privacy.setTypeface(font_type);
			lbl_license.setTypeface(font_type);

			txt_version_name.setTypeface(font_type);*/

			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			//String versionNumber = String.valueOf(pinfo.versionCode);
			String versionName = pinfo.versionName;

			txt_version_name.setText(versionName);

			/*is_eea_user = FastSave.getInstance().getBoolean(eu_consent_Helper.EEA_USER_KEY,false);
			if(!is_eea_user)
			{
				rel_user_consent.setVisibility(View.GONE);
				view_below_user_consent.setVisibility(View.GONE);
			}
			else
			{
				rel_user_consent.setVisibility(View.VISIBLE);
				view_below_user_consent.setVisibility(View.VISIBLE);
			}*/

			rel_ad_free.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ConformPurchaseDialog();
				}
			});

			rel_user_consent.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					/*boolean is_online = eu_consent_Class.isOnline(InfoActivity.this);
					if(is_online)
					{
						eu_consent_Class.DoConsentProcessSetting(InfoActivity.this,info_activity);
					}
					else
					{
						String toast_message = "Please enable your internet connection!";
						eu_consent_Class.ShowErrorToast(InfoActivity.this,toast_message);
					}*/

					startActivity(new Intent(SettingsActivity.this, ConsentActivity.class));

				}
			});

			rel_share_app.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					eu_consent_Class.ShareApp(SettingsActivity.this);
				}
			});

			rel_rate_us.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					eu_consent_Class.RateApp(SettingsActivity.this);
				}
			});

			rel_update=findViewById(R.id.setting_rel_update);
			rel_update.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//eu_consent_Class.RateApp(InfoActivity.this);
					final Dialog conform_dialog = new Dialog(SettingsActivity.this,R.style.TransparentBackground);
					conform_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					conform_dialog.setContentView(R.layout.eu_consent_dialog_rate);

					Button conform_dialog_btn_yes = (Button) conform_dialog.findViewById(R.id.dialog_conform_btn_yes);
					Button conform_dialog_btn_no = (Button) conform_dialog.findViewById(R.id.dialog_conform_btn_no);

					TextView conform_dialog_txt_header = (TextView)conform_dialog.findViewById(R.id.dialog_conform_txt_header);
					TextView conform_dialog_txt_message = (TextView)conform_dialog.findViewById(R.id.dialog_conform_txt_message);

					LottieAnimationView lottie_view = (LottieAnimationView) conform_dialog.findViewById(R.id.progress_lottie);
					lottie_view.setVisibility(View.GONE);

					String conform_dialog_header = "Check for update";
					String conform_dialog_message = "It will redirect to play store where you can update your app if avaialble.";

					conform_dialog_txt_header.setText(conform_dialog_header);
					conform_dialog_txt_message.setText(conform_dialog_message);

					conform_dialog_btn_yes.setText("Go");
					conform_dialog_btn_no.setText("Cancel");

					conform_dialog_btn_yes.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							try
							{
								String rateUrl = eu_consent_Helper.rate_url + SettingsActivity.this.getPackageName();
								Uri uri = Uri.parse(rateUrl);
								Intent view_intent = new Intent(Intent.ACTION_VIEW, uri);
								startActivity(view_intent);
							}
							catch (ActivityNotFoundException e)
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
			});

			rel_privacy.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					PrivacyPolicyScreen();
				}
			});

			rel_license.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					try
					{
						LicenseAgreement();
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			});

			/*img_back = findViewById(R.id.img_back);
			PushDownAnim.setPushDownAnimTo(img_back)
					.setScale(PushDownAnim.MODE_STATIC_DP, 10)
					.setDurationPush(PushDownAnim.DEFAULT_PUSH_DURATION)
					.setDurationRelease(PushDownAnim.DEFAULT_RELEASE_DURATION)
					.setInterpolatorPush(PushDownAnim.DEFAULT_INTERPOLATOR)
					.setInterpolatorRelease(PushDownAnim.DEFAULT_INTERPOLATOR)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (view.getId() == R.id.img_back) {
								onBackPressed();
							}
						}
					});*/

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
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

		LottieAnimationView lview =conform_dialog.findViewById(R.id.progress_lottie);
		lview.setVisibility(View.GONE);

		Typeface font_type = Typeface.createFromAsset(getAssets(), eu_consent_Helper.roboto_font_path);

		conform_dialog_btn_yes.setTypeface(font_type);
		conform_dialog_btn_no.setTypeface(font_type);

		conform_dialog_txt_header.setTypeface(font_type);
		conform_dialog_txt_message.setTypeface(font_type);

		String conform_dialog_header = "Are you sure you want pay for ad free app?";
		String conform_dialog_message = "With purchasing this feature all ads from application will be removed.";

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
									int responseCode = mBillingClient.launchBillingFlow(SettingsActivity.this, flowParams).getResponseCode();
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
						HideViews();
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
					HideViews();
				}
			}
		}

	}

	private void queryPurchases()
	{
		//Method not being used for now, but can be used if purchases ever need to be queried in the future
		mBillingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP,this );
		/*Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
		if (purchasesResult != null)
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

					if (purchase.getSkus().get(0).equals(eu_consent_Helper.remove_ads_sku))
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
			Toast.makeText(SettingsActivity.this,"3", Toast.LENGTH_SHORT).show();
			HideViews();
		}
		else
		{
			//Log.d(TAG, "Other code" + BillingClient.BillingResponseCode);
			//Toast.makeText(InfoActivity.this,"4",Toast.LENGTH_SHORT).show();
			// Handle any other error codes.
		}
	}

	private void PrivacyPolicyScreen()
	{
		Intent i = new Intent(SettingsActivity.this, eu_consent_PrivacyPolicyActivity.class);
		startActivity(i);
	}

	private void LicenseAgreement()
	{
		// TODO Auto-generated method stub
		new LicensesDialog.Builder(this)
				.setNotices(R.raw.notices)
				.build()
				.showAppCompat();
	}

	@Override
	protected void onResume()
	{
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

	private void AdMobConsent()
	{
		boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY,false);
		if(!is_hide_ads)
		{
			boolean is_online = eu_consent_Class.isOnline(this);
			if(is_online)
			{
				boolean is_eea_user = FastSave.getInstance().getBoolean(eu_consent_Helper.EEA_USER_KEY,false);
				if(is_eea_user)
				{
					boolean is_consent_set = FastSave.getInstance().getBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY,false);
					if (is_consent_set)
					{
						AdsProcess();
					}
					else
					{
						eu_consent_Class.DoConsentProcess(this,info_activity);
					}
				}
				else
				{
					AdsProcess();
				}
			}
			else
			{
				// Hide Ads
				HideViews();
			}
		}
		else
		{
			// Hide Ads
			HideViews();
		}
	}

	private void AdsProcess()
	{
		boolean is_g_user = FastSave.getInstance().getBoolean(eu_consent_Helper.GOOGLE_PLAY_STORE_USER_KEY, false);
		if (is_g_user)
		{
			/*Handler mHandler = new Handler() {
				public void handleMessage(Message msg) {


				}


			};
			Runnable runnable=new Runnable() {
				@Override
				public void run() {

					refreshAd();             }
			};
			mHandler.postDelayed(runnable,1000);*/
			refreshAd();

			//LoadAdMobNativeAd();
			if (eu_consent_Helper.ad_type.equals("1"))
			{
				LoadAdMobInterstitialAd();
			}else if (eu_consent_Helper.ad_type.equals("2"))
			{
				LoadAdManagerInterstitialAd();
			}
		}
		else
		{
			// Hide Ads
			HideViews();
		}
	}

	private void HideViews()
	{
		rel_native_ad = (RelativeLayout) findViewById(R.id.ad_layout);
		rel_native_ad.setVisibility(View.GONE);

		rel_ad_free.setVisibility(View.GONE);
		view_below_ad_free.setVisibility(View.GONE);

		rel_user_consent.setVisibility(View.GONE);
		view_below_user_consent.setVisibility(View.GONE);
	}

	/*private void LoadAdMobNativeAd()
	{
		AdLoader.Builder builder = new AdLoader.Builder(this, eu_consent_Helper.ad_mob_native_ad_id);
		builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener()
		{
			@Override
			public void onNativeAdLoaded(NativeAd nativeAd)
			{
				FrameLayout frameLayout = (FrameLayout) findViewById(R.id.native_ad_layout);
				NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.layout_native_ad, null);
				PopulateAdMobNativeAdView(nativeAd, adView);
				frameLayout.removeAllViews();
				frameLayout.addView(adView);
			}
		});

		VideoOptions videoOptions = new VideoOptions.Builder()
				.setStartMuted(true)
				.build();

		NativeAdOptions adOptions = new NativeAdOptions.Builder()
				.setVideoOptions(videoOptions)
				.build();

		builder.withNativeAdOptions(adOptions);

		AdLoader adLoader = builder.withAdListener(new AdListener()
		{
			@Override
			public void onAdFailedToLoad(LoadAdError loadAdError)
			{
				super.onAdFailedToLoad(loadAdError);
				Log.e("Unified Native:", "Failed to load native ad!");
			}
		}).build();

		Bundle non_personalize_bundle = new Bundle();
		non_personalize_bundle.putString("npa", "1");

		boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,false);
		if(is_show_non_personalize)
		{
			native_ad_request = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
		}
		else
		{
			native_ad_request = new AdRequest.Builder().build();
		}

		adLoader.loadAd(native_ad_request);

	}*/

/*	private void PopulateAdMobNativeAdView(NativeAd nativeAd, NativeAdView adView)
	{
		ad_mob_native_ad = nativeAd;

		View icon_view = adView.findViewById(R.id.ad_app_icon);
		View headline_view = adView.findViewById(R.id.ad_headline);
		View body_view = adView.findViewById(R.id.ad_body);
		View rating_view = adView.findViewById(R.id.ad_stars);
		View price_view = adView.findViewById(R.id.ad_price);
		View store_view = adView.findViewById(R.id.ad_store);
		View advertiser_view = adView.findViewById(R.id.ad_advertiser);
		View call_to_action_view = adView.findViewById(R.id.ad_call_to_action);

		adView.setIconView(icon_view);
		adView.setHeadlineView(headline_view);
		adView.setBodyView(body_view);
		adView.setStarRatingView(rating_view);
		adView.setPriceView(price_view);
		adView.setStoreView(store_view);
		adView.setAdvertiserView(advertiser_view);
		adView.setCallToActionView(call_to_action_view);

		MediaView mediaView = adView.findViewById(R.id.ad_media);
		adView.setMediaView(mediaView);

		((TextView) headline_view).setText(nativeAd.getHeadline());
		((TextView) body_view).setText(nativeAd.getBody());
		((Button) call_to_action_view).setText(nativeAd.getCallToAction());

		// check before trying to display them.
		if (nativeAd.getIcon() == null)
		{
			icon_view.setVisibility(View.GONE);
		}
		else
		{
			((ImageView) icon_view).setImageDrawable(nativeAd.getIcon().getDrawable());
			icon_view.setVisibility(View.VISIBLE);
		}

		if (nativeAd.getPrice() == null)
		{
			price_view.setVisibility(View.INVISIBLE);
		}
		else
		{
			price_view.setVisibility(View.VISIBLE);
			((TextView) price_view).setText(nativeAd.getPrice());
		}

		if (nativeAd.getStore() == null)
		{
			store_view.setVisibility(View.INVISIBLE);
		}
		else
		{
			store_view.setVisibility(View.VISIBLE);
			((TextView) store_view).setText(nativeAd.getStore());
		}

		if (nativeAd.getStarRating() == null)
		{
			rating_view.setVisibility(View.INVISIBLE);
		}
		else
		{
			((RatingBar) rating_view).setRating(nativeAd.getStarRating().floatValue());
			rating_view.setVisibility(View.VISIBLE);
		}

		if (nativeAd.getAdvertiser() == null)
		{
			advertiser_view.setVisibility(View.INVISIBLE);
		}
		else
		{
			((TextView) advertiser_view).setText(nativeAd.getAdvertiser());
			advertiser_view.setVisibility(View.VISIBLE);
		}

		//mediaView.setVisibility(View.VISIBLE);
		//mainImageView.setVisibility(View.VISIBLE);
		body_view.setVisibility(View.GONE);
		rating_view.setVisibility(View.VISIBLE);
		advertiser_view.setVisibility(View.VISIBLE);
		store_view.setVisibility(View.GONE);
		price_view.setVisibility(View.GONE);

		adView.setNativeAd(nativeAd);
	}*/

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

	private void LoadAdMobInterstitialAd()
	{
		// TODO Auto-generated method stub
		try
		{
			Bundle non_personalize_bundle = new Bundle();
			non_personalize_bundle.putString("npa", "1");

			boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,false);
			if(is_show_non_personalize)
			{
				interstitial_adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
			}
			else
			{
				interstitial_adRequest = new AdRequest.Builder().build();
			}

			//Interstitial Ad Start //
			InterstitialAd.load(this,eu_consent_Helper.admob_interstial_ad_id,interstitial_adRequest, new InterstitialAdLoadCallback()
			{
				@Override
				public void onAdLoaded(@NonNull InterstitialAd interstitialAd)
				{
					// The mInterstitialAd reference will be null until
					// an ad is loaded.
					ad_mob_interstitial = interstitialAd;
				}

				@Override
				public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
				{
					// Handle the error
					ad_mob_interstitial = null;
				}
			});
			//Interstitial Ad End //
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void LoadAdManagerInterstitialAd()
	{
		// TODO Auto-generated method stub
		try
		{
			Bundle non_personalize_bundle = new Bundle();
			non_personalize_bundle.putString("npa", "1");

			boolean is_show_non_personalize = FastSave.getInstance().getBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,false);
			if(is_show_non_personalize)
			{
				interstitial_manager_request = (AdManagerAdRequest) new AdManagerAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, non_personalize_bundle).build();
			}
			else
			{
				interstitial_manager_request = new AdManagerAdRequest.Builder().build();
			}

			//Interstitial Ad Start //
			AdManagerInterstitialAd.load(this,eu_consent_Helper.admob_interstial_ad_id,interstitial_manager_request, new AdManagerInterstitialAdLoadCallback()
			{
				@Override
				public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd)
				{
					// The mInterstitialAd reference will be null until
					// an ad is loaded.
					adManagerInterstitialAd = interstitialAd;
				}

				@Override
				public void onAdFailedToLoad(@NonNull LoadAdError loadAdError)
				{
					// Handle the error
					adManagerInterstitialAd = null;
				}
			});
			//Interstitial Ad End //
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		boolean is_hide_ads = FastSave.getInstance().getBoolean(eu_consent_Helper.REMOVE_ADS_KEY,false);
		if(!is_hide_ads)
		{
			boolean is_online = eu_consent_Class.isOnline(this);
			if (is_online)
			{
				if (eu_consent_Helper.ad_type.equals("1"))
				{
					ShowAdMobInterstitialAd();
				}else if (eu_consent_Helper.ad_type.equals("2"))
				{
					ShowAdMobInterstitialAd();
				}

			}
			else
			{
				BackScreen();
			}

		}
		else
		{
			BackScreen();
		}
	}

	private void ShowAdMobInterstitialAd()
	{
		// TODO Auto-generated method stub
		if (eu_consent_Helper.ad_type.equals("1"))
		{
			if (ad_mob_interstitial != null)
			{
				if(ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
				{
					DisplayInterstitialAd();
				}
				else
				{
					BackScreen();
				}
			}
			else
			{
				BackScreen();
			}
		}else if (eu_consent_Helper.ad_type.equals("2"))
		{
			if (adManagerInterstitialAd != null)
			{
				if(ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
				{
					DisplayInterstitialAd();
				}
				else
				{
					BackScreen();
				}
			}
			else
			{
				BackScreen();
			}
		}

	}

	private void DisplayInterstitialAd()
	{
		if (eu_consent_Helper.ad_type.equals("1"))
		{
			if(ad_mob_interstitial != null)
			{
				ad_mob_interstitial.setFullScreenContentCallback(new FullScreenContentCallback()
				{
					@Override
					public void onAdDismissedFullScreenContent()
					{
						// Called when fullscreen content is dismissed.
						Log.e("TAG", "The ad was dismissed.");
						BackScreen();
					}

					@Override
					public void onAdFailedToShowFullScreenContent(AdError adError)
					{
						// Called when fullscreen content failed to show.
						Log.e("TAG", "The ad failed to show.");
					}

					@Override
					public void onAdShowedFullScreenContent()
					{
						// Called when fullscreen content is shown.
						// Make sure to set your reference to null so you don't
						// show it a second time.
						ad_mob_interstitial = null;
						Log.e("TAG", "The ad was shown.");
					}
				});
			}
			ad_mob_interstitial.show(this);
			eu_consent_Helper.is_show_open_ad=false;
		}else if (eu_consent_Helper.ad_type.equals("2"))
		{
			if(adManagerInterstitialAd != null)
			{
				adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback()
				{
					@Override
					public void onAdDismissedFullScreenContent()
					{
						// Called when fullscreen content is dismissed.
						Log.e("TAG", "The ad was dismissed.");
						BackScreen();
					}

					@Override
					public void onAdFailedToShowFullScreenContent(AdError adError)
					{
						// Called when fullscreen content failed to show.
						Log.e("TAG", "The ad failed to show.");
					}

					@Override
					public void onAdShowedFullScreenContent()
					{
						// Called when fullscreen content is shown.
						// Make sure to set your reference to null so you don't
						// show it a second time.
						adManagerInterstitialAd = null;
						Log.e("TAG", "The ad was shown.");
					}
				});
			}
			adManagerInterstitialAd.show(this);
			eu_consent_Helper.is_show_open_ad=false;
		}

	}

	private void BackScreen()
	{
		// TODO Auto-generated method stub
		eu_consent_Helper.is_show_open_ad=true;
		finish();
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
		}
	}

}
