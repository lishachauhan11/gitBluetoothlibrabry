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
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.VideosFragment;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.model.PictureFacer;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

public class VideoAdapter  extends RecyclerView.Adapter<AudioHolder> {

    private ArrayList<PictureFacer> pictureList;
    private ArrayList<PictureFacer> selectedItem;
    private VideosFragment pictureContx;

    public VideoAdapter(ArrayList<PictureFacer> pictureList,ArrayList<PictureFacer> selectedItem, VideosFragment pictureContx) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.selectedItem = selectedItem;
    }

    @NonNull
    @Override
    public AudioHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_video_holder, container, false);
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
    holder.videos_icon.setVisibility(View.VISIBLE);
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
