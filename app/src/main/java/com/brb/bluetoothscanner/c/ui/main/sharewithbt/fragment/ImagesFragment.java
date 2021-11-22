package com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.activity.ShareWithBTActivity;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.adapter.ImagesAdapter;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.model.PictureFacer;
import com.brb.bluetoothscanner.c.utils.MarginDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ImagesFragment extends Fragment {

    RecyclerView imageRecycler;
    ArrayList<PictureFacer> allpictures;
    ArrayList<PictureFacer> selectedItem = new ArrayList<>();
    ProgressBar load;
    public Context context;
    ShareWithBTActivity shareWithBTActivity;


    public ImagesFragment(Context context, ShareWithBTActivity shareWithBTActivity,ArrayList<PictureFacer> selectedItem){
        this.context = context;
        this.shareWithBTActivity = shareWithBTActivity;
        this.selectedItem = selectedItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        imageRecycler = view.findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(context));
        imageRecycler.hasFixedSize();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        imageRecycler.setLayoutManager(layoutManager);
        imageRecycler.setItemAnimator(new DefaultItemAnimator());
        load = view.findViewById(R.id.loader);
        allpictures = new ArrayList<>();
        if (allpictures.isEmpty()) {
            allpictures = getAllImagesByFolder();
            Collections.sort(allpictures);
            Collections.reverse(allpictures);

            imageRecycler.setAdapter(new ImagesAdapter(allpictures,selectedItem, this));
            load.setVisibility(View.GONE);
        }
        return view;
    }

    private ArrayList<PictureFacer> getAllImagesByFolder() {
        ArrayList<PictureFacer> images = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                PictureFacer videoModel = new PictureFacer();
                videoModel.setPicturName(title);
                videoModel.setImageUri(data);
                videoModel.setPicturePath(data);
                videoModel.setPicturName(name);
                File fileObject = new File(data);
                Long fileModified = fileObject.lastModified();
                videoModel.setDateTime(new Date(fileModified));
                images.add(videoModel);

            } while (cursor.moveToNext());
        }


        return images;
    }

    public void onClick(PictureFacer file, boolean isselect) {
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
        imageRecycler.setAdapter(new ImagesAdapter(allpictures,selectedItem, this));
    }

}