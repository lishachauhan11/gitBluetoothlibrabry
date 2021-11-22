package com.brb.bluetoothscanner.c;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class eu_consent_PrivacyPolicyActivity extends Activity
{

	//private static final String ENROLLMENT_URL = "file:///android_asset/PrivacyPolicy/privacy_policy.html";
	protected WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Remove title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.eu_consent_privacy_policy);

		webView = (WebView)findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);

		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);

		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);

		webView.loadUrl(eu_consent_Helper.privacy_policy_url);
		//refreshWebView(webView);
	}

	private class MyWebViewClient extends WebViewClient
	{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
			String message = "Fetching Privacy & Policy";
			ShowLoadingDialog(message);
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			DismissLoadingDialog();
		}
	}

	private static Dialog loading_dialog;
	private static TextView loading_dialog_message;
	public void LoadingDialog(final String message)
	{
		loading_dialog = new Dialog(eu_consent_PrivacyPolicyActivity.this, R.style.TransparentBackground);
		loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loading_dialog.setContentView(R.layout.eu_consent_dialog_loading);

		loading_dialog_message = (TextView)loading_dialog.findViewById(R.id.dialog_loading_txt_message);

		Typeface font_type = Typeface.createFromAsset(getAssets(), eu_consent_Helper.roboto_font_path);

		loading_dialog_message.setTypeface(font_type);

		loading_dialog_message.setText(message);

		loading_dialog.show();
	}

	private void ShowLoadingDialog(String message)
	{
		LoadingDialog(message);
	}

	private static void DismissLoadingDialog()
	{
		if(loading_dialog != null)
		{
			loading_dialog.dismiss();
		}
	}
	/*public void refreshWebView(View view)
	{
		webView.loadUrl(ENROLLMENT_URL);
	}*/

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		BackScreen();
	}

	void BackScreen()
	{
		finish();
	}
}
