package com.brb.bluetoothscanner.c.ui.main.sharewithbt.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.appizona.yehiahd.fastsave.FastSave;
import com.brb.bluetoothscanner.c.BuildConfig;
import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.config.CommonFunction;
import com.brb.bluetoothscanner.c.eu_consent_Class;
import com.brb.bluetoothscanner.c.eu_consent_Helper;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.AppFragment;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.AudiosFragment;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.FileFragment;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.ImagesFragment;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.VideosFragment;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.model.Filedata;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.model.PictureFacer;
import com.facebook.ads.InterstitialAd;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.ak1.BubbleTabBar;
import io.ak1.OnBubbleClickListener;

public class ShareWithBTActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView backbutton;
    TextView title_type;
    BubbleTabBar bubbleTabBar;
    CommonFunction commonFunction;
    LinearLayout okbutton;
    TextView okbutton_text;
    LinearLayout loader;

    RelativeLayout rel_ad_layout;
    AdRequest banner_adRequest;
    AdManagerAdRequest banner_manager_request;
    AdView adView;
    AdManagerAdView adManagerAdView;


    AdManagerInterstitialAd adManagerInterstitialAd;
    AdManagerAdRequest interstitial_manager_request;
    InterstitialAd ad_mob_interstitial;
    AdRequest interstitial_adRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senddata);
        initobj();
        title_type.setText(R.string.share);

        loader = findViewById(R.id.loader);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bubbleTabBarsetup();
            }
        }, 1000);

    }

    private void bubbleTabBarsetup() {
        bubbleTabBar = findViewById(R.id.bubbleTabBar);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bubbleTabBar.setSelected(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int id) {
                switch (id) {
                    case R.id.images:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.videos:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.audios:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.apps:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.files:
                        viewPager.setCurrentItem(4);
                        break;
                }
            }
        });
        viewPager.setOffscreenPageLimit(4);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void initobj() {
        backbutton = findViewById(R.id.backbutton);
        title_type = findViewById(R.id.title_type);
        okbutton = findViewById(R.id.okbutton);
        okbutton_text = findViewById(R.id.okbutton_text);
        viewPager = findViewById(R.id.viewpager);
        backbutton.setOnClickListener(this);
        okbutton.setOnClickListener(this);
        commonFunction = new CommonFunction();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ImagesFragment(this, this, mediaselectedlist), "Images");
        adapter.addFragment(new VideosFragment(this, this, mediaselectedlist), "Videos");
        adapter.addFragment(new AudiosFragment(this, this, mediaselectedlist), "Audios");
        adapter.addFragment(new AppFragment(this, this, appselectedlist), "Apps");
        adapter.addFragment(new FileFragment(this, this), "Files");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbutton:
                finish();
                break;
            case R.id.okbutton:
                if(!itemList.isEmpty()) {
                    senddataviabt();
                }
                else {
                    Toast.makeText(this, "Select some files..", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public ArrayList<Uri> itemList = new ArrayList<>();
    public ArrayList<PictureFacer> mediaselectedlist = new ArrayList<>();
    public ArrayList<ApplicationInfo> appselectedlist = new ArrayList<>();

    public void click(PictureFacer selectedItem, boolean isadd) {
        File file = new File(selectedItem.getPicturePath());
        Log.e("Tag","File  --- "+file);
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
        if (isadd) {
            itemList.add(uri);
            mediaselectedlist.add(selectedItem);
            okbutton_text.setText("Send Files (" + itemList.size() + ")");
        } else {
            itemList.remove(uri);
            mediaselectedlist.remove(selectedItem);
            okbutton_text.setText("Send Files (" + itemList.size() + ")");
        }
        if (itemList.isEmpty()) {
            okbutton.setVisibility(View.GONE);
        } else {
            okbutton.setVisibility(View.VISIBLE);
        }
    }

    public void click(Filedata selectedItem, boolean isadd) {
        Uri uri = selectedItem.furi;
        if (isadd) {
            itemList.add(uri);
            okbutton_text.setText("Send Files (" + itemList.size() + ")");
        } else {
            itemList.remove(uri);
            okbutton_text.setText("Send Files (" + itemList.size() + ")");
        }
        btnvisible();
    }

    private void btnvisible() {
        if (itemList.isEmpty()) {
            okbutton.setVisibility(View.GONE);
        } else {
            okbutton.setVisibility(View.VISIBLE);
        }
    }

    public void click(ApplicationInfo selectedItem, boolean isadd) {
        try {
            File originalApk = new File(selectedItem.sourceDir);
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            tempFile = new File(tempFile.getPath() + "/" + commonFunction.GetAppName(selectedItem.packageName.replace(" ", ""), this) + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            Uri uri = FileProvider.getUriForFile(this,
                    BuildConfig.APPLICATION_ID + ".provider", tempFile);
            in.close();
            out.close();
            if (isadd) {
                itemList.add(uri);
                appselectedlist.add(selectedItem);
                okbutton_text.setText("Send Files (" + itemList.size() + ")");
            } else {
                itemList.remove(uri);
                appselectedlist.remove(selectedItem);
                okbutton_text.setText("Send Files (" + itemList.size() + ")");
            }
        } catch (Exception e) {
            Log.e("Tag", "Error -- " + e.getMessage());
        }
        btnvisible();
    }

    void senddataviabt() {
        Log.e("Tag", "item size -- " + itemList.size());
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        sharingIntent.setType("*/*");
        sharingIntent.setComponent(new ComponentName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
        sharingIntent.putExtra(Intent.EXTRA_STREAM, itemList);
        startActivity(sharingIntent);
        finish();
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
                        eu_consent_Class.DoConsentProcess(this, ShareWithBTActivity.this);
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
                adView = new AdView(ShareWithBTActivity.this);
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
                adManagerAdView = new AdManagerAdView(ShareWithBTActivity.this);
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