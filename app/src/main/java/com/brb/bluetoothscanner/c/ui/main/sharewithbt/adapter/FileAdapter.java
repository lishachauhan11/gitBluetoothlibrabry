package com.brb.bluetoothscanner.c.ui.main.sharewithbt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment.FileFragment;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.model.Filedata;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private ArrayList<Filedata> pictureList;
    private FileFragment pictureContx;


    public FileAdapter(ArrayList<Filedata> pictureList, FileFragment pictureContx) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView remove;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            title =  view.findViewById(R.id.title);
            remove =  view.findViewById(R.id.remove);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_file_holder, container, false);
        return new ViewHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        final Filedata imagedata = pictureList.get(position);

        holder.title.setText(imagedata.fname);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureContx.onClick(imagedata , position);
            }
        });

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
