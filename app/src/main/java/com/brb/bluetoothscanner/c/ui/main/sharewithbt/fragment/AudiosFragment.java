package com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.activity.ShareWithBTActivity;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.adapter.AudioAdapter;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.model.PictureFacer;
import com.brb.bluetoothscanner.c.utils.MarginDecoration;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class AudiosFragment extends Fragment {

    public Context context;
    ShareWithBTActivity shareWithBTActivity;

    public AudiosFragment(Context context, ShareWithBTActivity shareWithBTActivity,
                          ArrayList<PictureFacer> selectedItem) {
        this.context = context;
        this.shareWithBTActivity = shareWithBTActivity;
        this.selectedItem = selectedItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    RecyclerView imageRecycler;
    ArrayList<PictureFacer> allpictures;
    ArrayList<PictureFacer> selectedItem = new ArrayList<>();
    ProgressBar load;

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audios, container, false);

        allpictures = new ArrayList<>();
        imageRecycler = view.findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(context));
        imageRecycler.hasFixedSize();
        load = view.findViewById(R.id.loader);
        allpictures = getAllaudiosByFolder();

        if (allpictures.isEmpty()) {
        } else {
            Collections.sort(allpictures);
            Collections.reverse(allpictures);

            imageRecycler.setAdapter(new AudioAdapter(allpictures, selectedItem, this));
            load.setVisibility(View.GONE);
        }
        return view;
    }

    private ArrayList<PictureFacer> getAllaudiosByFolder() {
        ArrayList<PictureFacer> images = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                PictureFacer videoModel = new PictureFacer();
                videoModel.setPicturName(title);
                videoModel.setImageUri(data);
                videoModel.setPicturePath(data);
                float sizes = Integer.valueOf(size) / (1024f * 1024f);
                videoModel.setPictureSize(math(sizes) + " mb");
                File fileObject = new File(data);
                Long fileModified = fileObject.lastModified();
                videoModel.setDateTime(new Date(fileModified));
                images.add(videoModel);

            } while (cursor.moveToNext());
        }

        return images;
    }

    public static String math(float f) {
        DecimalFormat format = new DecimalFormat("0.##");
        return format.format(f);
    }

    public void onClick(PictureFacer file, boolean isselect) {

        if (isselect) {
            selectedItem.add(file);
            shareWithBTActivity.click(file, true);
        } else {
            selectedItem.remove(file);
            shareWithBTActivity.click(file, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        imageRecycler.setAdapter(new AudioAdapter(allpictures, selectedItem, this));
    }
}
