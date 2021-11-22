package com.brb.bluetoothscanner.c.ui.main.sharewithbt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.bind.AudioHolder;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.AudiosFragment;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.model.PictureFacer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

public class AudioAdapter extends RecyclerView.Adapter<AudioHolder> {

    private ArrayList<PictureFacer> pictureList;
    private ArrayList<PictureFacer> selectedItem;
    private AudiosFragment pictureContx;



    public AudioAdapter(ArrayList<PictureFacer> pictureList,ArrayList<PictureFacer> selectedItem, AudiosFragment pictureContx) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.selectedItem = selectedItem;
    }

    @NonNull
    @Override
    public AudioHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_audio_holder, container, false);
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

        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        String today = dateFormat.format(imagedata.getDateTime());
        holder.date.setText(today);
        holder.size.setText(imagedata.getPictureSize());

            if(imagedata.getPicturName().length() > 15){
                String sub = imagedata.getPicturName().substring(0,14);
                holder.title.setText(sub+"..");
            }
            else{
                holder.title.setText(imagedata.getPicturName());
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
