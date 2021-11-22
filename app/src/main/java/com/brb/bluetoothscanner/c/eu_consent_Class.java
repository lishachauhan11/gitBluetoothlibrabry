package com.brb.bluetoothscanner.c;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class eu_consent_Class
{
	private static String TAG = "eu_consent_Class : ";

	private static Context mContext;

	public static Typeface font_type;

	public static boolean is_online;

	public eu_consent_Class(Context ctx)
	{
		// TODO Auto-generated constructor stub
		mContext = ctx;
	}

	public static void RateApp(Context ctx)
	{
		// TODO Auto-generated method stub
		try
		{
			mContext = ctx;
			String rateUrl = eu_consent_Helper.rate_url + mContext.getPackageName();

			String dialog_header = "Rate Us";
			String dialog_message = "Please give your valuable feedback" + "\n" + "Show your support to us!";

			ConformRateDialog(mContext,rateUrl,dialog_header,dialog_message);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void GoToApp(Context ctx, String appUrl)
	{
		// TODO Auto-generated method stub
		try 
		{
			mContext = ctx;


			try
			{
				mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(appUrl)));
			}
			catch (ActivityNotFoundException e)
			{
				e.printStackTrace();
			}
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void ShareApp(Context ctx)
	{
		// TODO Auto-generated method stub
		try
		{
			mContext = ctx;
			String app_name = mContext.getResources().getString(R.string.app_name) + " :";
			String shareUrl = eu_consent_Helper.rate_url + mContext.getPackageName();

			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.putExtra(Intent.EXTRA_TEXT,app_name + "\n" + shareUrl);
			mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void MoreApps(Context ctx)
	{
		// TODO Auto-generated method stub
		try
		{
			mContext = ctx;
			Intent more_intent = new Intent(Intent.ACTION_VIEW);
			more_intent.setData(Uri.parse(eu_consent_Helper.more_url));
			mContext.startActivity(more_intent);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/*@SuppressWarnings("deprecation")
	public static void showSuperToast(Context mContext,String msg)
	{
		//font_type = Typeface.createFromAsset(mContext.getAssets(),RBHelper.app_font_name);

		final SuperToast superToast = new SuperToast(mContext);
		superToast.setAnimations(SuperToast.Animations.FLYIN);
		superToast.setTextColor(mContext.getResources().getColor(R.color.white));
		superToast.setDuration(3000);
		superToast.setBackground(R.color.toast_background_color);
		superToast.setTextSize(SuperToast.TextSize.MEDIUM);
		superToast.setText(msg);
		superToast.show();
	}*/

	public static void ShowInfoToast(Context mContext, String toast_message) {
		MDToast mdToast = MDToast.makeText(mContext, toast_message, MDToast.LENGTH_SHORT, MDToast.TYPE_INFO);
		mdToast.show();
	}

	public static boolean isOnline(Context mContext)
	{
		ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected())
		{
			is_online = true;
			return is_online;
		}
		else
		{
			is_online = false;
			return is_online;
		}
	}

	public static SimpleDateFormat df;
	public static SimpleDateFormat sdf;
	public static SimpleDateFormat sdt;

	public static Date current_datetime;

	public static String current_date_time;
	public static String current_date;
	public static String current_time;

	public static String GetCurrentDateTime()
	{
		// TODO Auto-generated method stub
		try
		{
			Calendar c = Calendar.getInstance();
			System.out.println("Current time => " + c.getTime());


			df = new SimpleDateFormat("dd-MMM-yyyy HH:mm aa");
			current_date_time = df.format(c.getTime());
			current_datetime = df.parse(current_date_time);

			sdf = new SimpleDateFormat("dd-MMM-yyyy");
			sdt = new SimpleDateFormat("HH:mm aa");

			current_date = sdf.format(current_datetime);
			current_time = sdt.format(current_datetime);

			String delegate = "hh:mm aaa";
			current_time = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return current_date_time;
	}

	public static void ShowSuccessToast(Context mContext, String toast_message)
	{
		MDToast mdToast = MDToast.makeText(mContext, toast_message, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
		mdToast.show();
	}

	public static void ShowErrorToast(Context mContext, String toast_message)
	{
		MDToast mdToast = MDToast.makeText(mContext, toast_message, MDToast.LENGTH_LONG, MDToast.TYPE_ERROR);
		mdToast.show();
	}

	public static void ConformRateDialog(final Context mContext, final String appUrl, final String header, final String message)
	{
		final Dialog conform_dialog = new Dialog(mContext,R.style.TransparentBackground);
		conform_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		conform_dialog.setContentView(R.layout.eu_consent_dialog_rate);

		Button conform_dialog_btn_yes = (Button) conform_dialog.findViewById(R.id.dialog_conform_btn_yes);
		Button conform_dialog_btn_no = (Button) conform_dialog.findViewById(R.id.dialog_conform_btn_no);

		TextView conform_dialog_txt_header = (TextView)conform_dialog.findViewById(R.id.dialog_conform_txt_header);
		TextView conform_dialog_txt_message = (TextView)conform_dialog.findViewById(R.id.dialog_conform_txt_message);

		font_type = Typeface.createFromAsset(mContext.getAssets(), eu_consent_Helper.roboto_font_path);

		conform_dialog_btn_yes.setTypeface(font_type);
		conform_dialog_btn_no.setTypeface(font_type);

		conform_dialog_txt_header.setTypeface(font_type);
		conform_dialog_txt_message.setTypeface(font_type);

		String conform_dialog_header = header;
		String conform_dialog_message = message;

		conform_dialog_txt_header.setText(conform_dialog_header);
		conform_dialog_txt_message.setText(conform_dialog_message);

		conform_dialog_btn_yes.setText("Rate Now");
		conform_dialog_btn_no.setText("Cancel");

		conform_dialog_btn_yes.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try
				{
					Uri uri = Uri.parse(appUrl);
					Intent view_intent = new Intent(Intent.ACTION_VIEW, uri);
					mContext.startActivity(view_intent);
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


	public static void ExitDialog(final Context mContext, final Activity mActivity)
	{
		final Dialog conform_dialog = new Dialog(mContext,R.style.TransparentBackground);
		conform_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		conform_dialog.setContentView(R.layout.eu_consent_dialog_rate);

		Button conform_dialog_btn_yes = (Button) conform_dialog.findViewById(R.id.dialog_conform_btn_yes);
		Button conform_dialog_btn_no = (Button) conform_dialog.findViewById(R.id.dialog_conform_btn_no);

		TextView conform_dialog_txt_header = (TextView)conform_dialog.findViewById(R.id.dialog_conform_txt_header);
		TextView conform_dialog_txt_message = (TextView)conform_dialog.findViewById(R.id.dialog_conform_txt_message);

		font_type = Typeface.createFromAsset(mContext.getAssets(), eu_consent_Helper.roboto_font_path);

		conform_dialog_btn_yes.setTypeface(font_type);
		conform_dialog_btn_no.setTypeface(font_type);

		conform_dialog_txt_header.setTypeface(font_type);
		conform_dialog_txt_message.setTypeface(font_type);

		String conform_dialog_header = "Exit";
		String conform_dialog_message = "Are you sure you want to exit from app?";

		conform_dialog_txt_header.setText(conform_dialog_header);
		conform_dialog_txt_message.setText(conform_dialog_message);

		conform_dialog_btn_yes.setText("Exit");
		conform_dialog_btn_no.setText("Cancel");

		conform_dialog_btn_yes.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				conform_dialog.dismiss();
				FinishApp(mActivity);
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

	protected static final int BUY_REQUEST_CODE = 10001;
	//private static IabHelper buyHelper;
	public static void DoConsentProcess(final Context mContext, final Activity mActivity)
	{
		/*buyHelper = new IabHelper(mContext, eu_consent_Helper.In_App_Public_Key);
		buyHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
		{
			@Override
			public void onIabSetupFinished(IabResult result)
			{
				if (!result.isSuccess())
				{
					Log.e(TAG, "In-app set up Failed: " + result);
				}
				else
				{
					Log.e(TAG, "In-app set up OK");
				}
			}
		});*/

		String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
		String deviceId = md5(android_id).toUpperCase();

		ConsentInformation consentInformation = ConsentInformation.getInstance(mContext);

		String[] publisherIds = {eu_consent_Helper.admob_pub_id};
		consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener()
		{
			@Override
			public void onConsentInfoUpdated(ConsentStatus consentStatus)
			{
				// User's consent status successfully updated.
				boolean is_user_eea = ConsentInformation.getInstance(mContext).isRequestLocationInEeaOrUnknown();
				if(is_user_eea)
				{
					Log.e(TAG,"User is from EEA!");
					if(consentStatus == ConsentStatus.PERSONALIZED)
					{
						Log.e(TAG,"User approve PERSONALIZED Ads!");
						ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.PERSONALIZED);

						FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,false);
						FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,false);
						FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY,true);
					}
					else if(consentStatus == ConsentStatus.NON_PERSONALIZED)
					{
						Log.e(TAG,"User approve NON_PERSONALIZED Ads!");
						ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.NON_PERSONALIZED);

						FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,false);
						FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,true);
						FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY,true);
					}
					else if(consentStatus == ConsentStatus.UNKNOWN)
					{
						Log.e(TAG,"User has neither granted nor declined consent!");
						ShowAdMobConsentDialog(mContext,mActivity,false);
					}
				}
				else
				{
					Log.e(TAG,"User is not from EEA!");
				}
			}

			@Override
			public void onFailedToUpdateConsentInfo(String errorDescription)
			{
				// User's consent status failed to update.
				Log.e(TAG,"Consent Status Failed :" + errorDescription);
				//DismissLoadingDialog();
			}
		});
	}

	public static void DoConsentProcessSetting(final Context mContext, final Activity mActivity)
	{
		/*buyHelper = new IabHelper(mContext, eu_consent_Helper.In_App_Public_Key);
		buyHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
		{
			@Override
			public void onIabSetupFinished(IabResult result)
			{
				if (!result.isSuccess())
				{
					Log.e(TAG, "In-app set up Failed: " + result);
				}
				else
				{
					Log.e(TAG, "In-app set up OK");
				}
			}
		});*/

		String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
		String deviceId = md5(android_id).toUpperCase();

		ConsentInformation consentInformation = ConsentInformation.getInstance(mContext);
        /*ConsentInformation.getInstance(mContext).addTestDevice(deviceId);
        ConsentInformation.getInstance(mContext).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);*/
		String[] publisherIds = {eu_consent_Helper.admob_pub_id};
		consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener()
		{
			@Override
			public void onConsentInfoUpdated(ConsentStatus consentStatus)
			{
				// User's consent status successfully updated.
				boolean is_user_eea = ConsentInformation.getInstance(mContext).isRequestLocationInEeaOrUnknown();
				if(is_user_eea)
				{
					Log.e(TAG,"User is from EEA!");
					if(consentStatus == ConsentStatus.PERSONALIZED)
					{
						Log.e(TAG,"User approve PERSONALIZED Ads!");
						ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.PERSONALIZED);

						FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,false);
						FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,false);
						FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY,true);

						ShowAdMobConsentDialog(mContext,mActivity,true);
					}
					else if(consentStatus == ConsentStatus.NON_PERSONALIZED)
					{
						Log.e(TAG,"User approve NON_PERSONALIZED Ads!");
						ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.NON_PERSONALIZED);

						FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,false);
						FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,true);
						FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY,true);

						ShowAdMobConsentDialog(mContext,mActivity,true);
					}
					else if(consentStatus == ConsentStatus.UNKNOWN)
					{
						Log.e(TAG,"User has neither granted nor declined consent!");
						ShowAdMobConsentDialog(mContext,mActivity,true);
					}
				}
				else
				{
					Log.e(TAG,"User is not from EEA!");
				}
			}

			@Override
			public void onFailedToUpdateConsentInfo(String errorDescription)
			{
				// User's consent status failed to update.
				Log.e(TAG,"Consent Status Failed :" + errorDescription);
			}
		});
	}

	public static void ShowAdMobConsentDialog(final Context mContext, final Activity mActivity, boolean showCancel)
	{
		final Dialog eu_consent_dialog = new Dialog(mContext,R.style.TransparentBackground);
		eu_consent_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		eu_consent_dialog.setContentView(R.layout.eu_consent_custom);

		eu_consent_dialog.setCancelable(showCancel);

		TextView txt_app_name = (TextView)eu_consent_dialog.findViewById(R.id.eudialog_txt_appname);
		TextView txt_care = (TextView)eu_consent_dialog.findViewById(R.id.eudialog_txt_care);
		TextView txt_ask_continue = (TextView)eu_consent_dialog.findViewById(R.id.eudialog_txt_askcontinue);
		TextView txt_desc = (TextView)eu_consent_dialog.findViewById(R.id.eudialog_txt_desc);
		TextView txt_learn_more = (TextView)eu_consent_dialog.findViewById(R.id.eudialog_lbl_learnmore);

		TextView lbl_continue_ads = (TextView)eu_consent_dialog.findViewById(R.id.eudialog_lbl_continue);
		TextView lbl_non_personalize = (TextView)eu_consent_dialog.findViewById(R.id.eudialog_lbl_irrelevant);
		TextView lbl_remove_ads = (TextView)eu_consent_dialog.findViewById(R.id.eudialog_lbl_removeads);
		TextView lbl_exit = (TextView)eu_consent_dialog.findViewById(R.id.eudialog_lbl_exit);

		Typeface roboto_font_type = Typeface.createFromAsset(mContext.getAssets(), eu_consent_Helper.roboto_font_path);
		txt_app_name.setTypeface(roboto_font_type);
		txt_care.setTypeface(roboto_font_type);
		txt_ask_continue.setTypeface(roboto_font_type);
		txt_desc.setTypeface(roboto_font_type);
		txt_learn_more.setTypeface(roboto_font_type);

		lbl_continue_ads.setTypeface(roboto_font_type);
		lbl_non_personalize.setTypeface(roboto_font_type);
		lbl_remove_ads.setTypeface(roboto_font_type);
		lbl_exit.setTypeface(roboto_font_type);

		RelativeLayout rel_continue_ad = eu_consent_dialog.findViewById(R.id.eudialog_rel_continue);
		RelativeLayout rel_irrelevant = eu_consent_dialog.findViewById(R.id.eudialog_rel_irrelevant);
		RelativeLayout rel_remove_ads = eu_consent_dialog.findViewById(R.id.eudialog_rel_removeads);
		RelativeLayout rel_exit = eu_consent_dialog.findViewById(R.id.eudialog_rel_exit);

		rel_remove_ads.setVisibility(View.GONE);
		rel_exit.setVisibility(View.VISIBLE);

		String appName = mContext.getResources().getString(R.string.app_name);
		/*String desc_data = "You can change your choice anytime for " + appName + " in the app settings.Our partners collect data and use a unique identifier on your device to show you ads.";
		String care_data = "We care about your privacy & data security.We keep this app free by showing ads.";
		String ask_continue_data = "Can we continue to use yor data to tailor ads for you?";
		String learn_more = "Privacy & Policy" + "\n" + "How App & our partners uses your data!";*/

		String desc_data = "If you wish to reverse your consent please purchase ad free version from app settings.";
		String care_data = "We care about your privacy & data security.We keep this app free by showing ads.";
		String ask_continue_data = "We have received your consent to use your data & serve you personalised ads.";
		String learn_more = "Privacy & Policy" + "\n" + "How App & our partners uses your data!";

		txt_app_name.setText(appName);
		txt_care.setText(care_data);
		txt_ask_continue.setText(ask_continue_data);
		txt_desc.setText(desc_data);
		txt_learn_more.setText(learn_more);

		rel_continue_ad.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				eu_consent_dialog.cancel();

				String toast_message = "Thank you for continue to see personalize ads!";
				ShowSuccessToast(mContext,toast_message);

				FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,false);
				FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,false);
				FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY,true);

				ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.PERSONALIZED);
			}
		});

		rel_irrelevant.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				eu_consent_dialog.cancel();

				String toast_message = "Thank you for continue to see non-personalize ads!";
				ShowSuccessToast(mContext,toast_message);

				FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,false);
				FastSave.getInstance().saveBoolean(eu_consent_Helper.SHOW_NON_PERSONALIZE_ADS_KEY,true);
				FastSave.getInstance().saveBoolean(eu_consent_Helper.ADS_CONSENT_SET_KEY,true);

				ConsentInformation.getInstance(mContext).setConsentStatus(ConsentStatus.NON_PERSONALIZED);
			}
		});



		rel_exit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				eu_consent_dialog.cancel();
				ExitApp(mActivity);
			}
		});

		txt_learn_more.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eu_consent_Helper.privacy_policy_url));
				mContext.startActivity(browserIntent);
			}
		});

		eu_consent_dialog.show();
	}

/*	public static void InappProductProcess(final Context mContext, Activity mActivity, final String item_id)
	{
		buyHelper.launchPurchaseFlow(mActivity, item_id, BUY_REQUEST_CODE, new IabHelper.OnIabPurchaseFinishedListener()
		{
			@Override
			public void onIabPurchaseFinished(IabResult result, Purchase purchase)
			{
				try
				{
					if (result.isSuccess())
					{
						String toast_message = "Ads removed successfully!";
						eu_consent_Class.ShowSuccessToast(mContext, toast_message);

						FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,true);
					}
					else if (result.getResponse() == 7) // Item owned response code
					{
						String toast_message = "Item already purchased.";
						eu_consent_Class.ShowSuccessToast(mContext, toast_message);

						FastSave.getInstance().saveBoolean(eu_consent_Helper.REMOVE_ADS_KEY,true);
					}
				}
				catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}, "");

		return;
	}*/


	public static final String md5(final String s)
	{
		try
		{
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
			{
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	private static Dialog loading_dialog;
	private static TextView loading_dialog_message;
	public static void LoadingDialog(final Context mContext, final String message)
	{
		loading_dialog = new Dialog(mContext,R.style.TransparentBackground);
		loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loading_dialog.setContentView(R.layout.eu_consent_dialog_loading);

		loading_dialog_message = (TextView)loading_dialog.findViewById(R.id.dialog_loading_txt_message);

		font_type = Typeface.createFromAsset(mContext.getAssets(), eu_consent_Helper.roboto_font_path);

		loading_dialog_message.setTypeface(font_type);

		loading_dialog_message.setText(message);

		loading_dialog.show();
	}

	private static void ShowLoadingDialog(Context mContext)
	{
		String message = "Fetching AdMob Consent";
		LoadingDialog(mContext,message);
	}

	private static void DismissLoadingDialog()
	{
		if(loading_dialog != null)
		{
			loading_dialog.dismiss();
		}
	}


	@SuppressLint("NewApi")
	public static void ExitApp(Activity mActivity)
	{
		mActivity.finishAndRemoveTask();
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
		System.exit(0);
	}

	public static void FinishApp(Activity mActivity)
	{
		mActivity.finish();
	}

}
