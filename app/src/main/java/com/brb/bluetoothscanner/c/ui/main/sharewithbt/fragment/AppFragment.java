package com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.config.CommonFunction;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.activity.ShareWithBTActivity;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.adapter.AppAdapter;
import com.brb.bluetoothscanner.c.utils.MarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class AppFragment extends Fragment {

    public Context context;
    ShareWithBTActivity shareWithBTActivity;
    CommonFunction commonFunction;

    public AppFragment(Context context, ShareWithBTActivity shareWithBTActivity,
                       List<ApplicationInfo> selectedItem) {
        this.context = context;
        this.shareWithBTActivity = shareWithBTActivity;
        this.selectedItem = selectedItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private static final String TAG = "MainActivity";
    RecyclerView imageRecycler;
    List<ApplicationInfo> allpictures;
    List<ApplicationInfo> selectedItem = new ArrayList<>();
    ProgressBar load;

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app, container, false);
        commonFunction = new CommonFunction();
        allpictures = new ArrayList<>();
        imageRecycler = view.findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(context));
        imageRecycler.hasFixedSize();
        load = view.findViewById(R.id.loader);

        if (allpictures.isEmpty()) {
            load.setVisibility(View.VISIBLE);
            allpictures = addapps();

            imageRecycler.setAdapter(new AppAdapter(allpictures,selectedItem, this, context));
            load.setVisibility(View.GONE);
        }
        return view;
    }

    private List<ApplicationInfo> addapps() {
        List<ApplicationInfo> packagelist = new ArrayList<>();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            packagelist.add(activityInfo.applicationInfo);

        }

        return packagelist;
    }

    public void onClick(ApplicationInfo file, boolean isselect) {

        if (isselect) {
            selectedItem.add(file);
            shareWithBTActivity.click(file,true);
        } else {
            selectedItem.remove(file);
            shareWithBTActivity.click(file,false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        imageRecycler.setAdapter(new AppAdapter(allpictures,selectedItem, this, context));
    }
}
