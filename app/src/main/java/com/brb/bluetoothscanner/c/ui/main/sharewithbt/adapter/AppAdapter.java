package com.brb.bluetoothscanner.c.ui.main.sharewithbt.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brb.bluetoothscanner.c.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.brb.bluetoothscanner.c.config.CommonFunction;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.bind.PicHolder;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.AppFragment;

import java.util.List;

import static androidx.core.view.ViewCompat.setTransitionName;

public class AppAdapter extends RecyclerView.Adapter<PicHolder> {

    private List<ApplicationInfo> pictureList;
    private AppFragment pictureContx;
    Context context;
    CommonFunction commonFunction;
    List<ApplicationInfo> selectedItem;

    public AppAdapter(List<ApplicationInfo> pictureList, List<ApplicationInfo> selectedItem, AppFragment pictureContx, Context context) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.context = context;
        this.selectedItem = selectedItem;
        commonFunction = new CommonFunction();

    }

    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_app_cardview, container, false);
        return new PicHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, final int position) {

        final ApplicationInfo imagedata = pictureList.get(position);

        if (!selectedItem.isEmpty()) {
            for (int i = 0; i < selectedItem.size(); i++) {
                if (selectedItem.get(i).packageName.equalsIgnoreCase(imagedata.packageName)) {

                    holder.checkbox.setChecked(true);
                }
            }
        }
        Glide.with(pictureContx)
                .load(imagedata.loadIcon(context.getPackageManager()))
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);

        setTransitionName(holder.picture, String.valueOf(position) + "_image");

        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkbox.isChecked()) {
                    holder.checkbox.setChecked(false);
                    pictureContx.onClick(imagedata, false);
                } else {
                    holder.checkbox.setChecked(true);
                    pictureContx.onClick(imagedata, true);
                }
            }
        });

        holder.picture.setBackgroundColor(pictureContx.getResources().getColor(R.color.white));
        String name = commonFunction.GetAppName(imagedata.packageName, context);
        if (name.length() > 15) {
            String sub = name.substring(0, 13);
            holder.title.setText(sub + "..");
        } else {
            holder.title.setText(name);
        }
        holder.title.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
