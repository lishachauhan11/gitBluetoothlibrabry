package com.brb.bluetoothscanner.c.ui.main.sharewithbt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.bind.AudioHolder;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.ImagesFragment;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.model.PictureFacer;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

public class ImagesAdapter extends RecyclerView.Adapter<AudioHolder> {

    private ArrayList<PictureFacer> pictureList;
    private ArrayList<PictureFacer> selectedItem;
    private ImagesFragment pictureContx;

    public ImagesAdapter(ArrayList<PictureFacer> pictureList,ArrayList<PictureFacer> selectedItem, ImagesFragment pictureContx) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.selectedItem = selectedItem;
    }

    @NonNull
    @Override
    public AudioHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_image_holder, container, false);
        return new AudioHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final AudioHolder holder, final int position) {
        final PictureFacer imagedata = pictureList.get(position);
        if (!selectedItem.isEmpty()) {
            for (int i = 0; i < selectedItem.size(); i++) {
                if (selectedItem.get(i).getPicturePath().equalsIgnoreCase(imagedata.getPicturePath())) {
                    holder.checkbox.setChecked(true);
                }
            }
        }
        if(position==0 || position%4==0){
            ViewGroup.LayoutParams layoutParams = holder.picture.getLayoutParams();
            layoutParams.height = 550;
            holder.picture.setLayoutParams(layoutParams);
        }
        if(position==1 || position%4==1){
            ViewGroup.LayoutParams layoutParams = holder.picture.getLayoutParams();
            layoutParams.height = 700;
            holder.picture.setLayoutParams(layoutParams);
        }
        if(position==2 || position%4==2){
            ViewGroup.LayoutParams layoutParams = holder.picture.getLayoutParams();
            layoutParams.height = 700;
            holder.picture.setLayoutParams(layoutParams);
        }
        if(position==3 || position%4==3){
            ViewGroup.LayoutParams layoutParams = holder.picture.getLayoutParams();
            layoutParams.height = 550;
            holder.picture.setLayoutParams(layoutParams);
        }
        Glide.with(pictureContx)
                .load(imagedata.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);

        setTransitionName(holder.picture, String.valueOf(position) + "_image");

        holder.picture.setOnClickListener(new View.OnClickListener() {
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

        holder.title.setVisibility(View.GONE);

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
