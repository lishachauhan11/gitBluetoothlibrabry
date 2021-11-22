package com.brb.bluetoothscanner.c.ui.main.sharewithbt.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.activity.ShareWithBTActivity;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.adapter.FileAdapter;
import com.brb.bluetoothscanner.c.ui.main.sharewithbt.model.Filedata;
import com.brb.bluetoothscanner.c.utils.MarginDecoration;

import java.io.File;
import java.util.ArrayList;

public class FileFragment extends Fragment {

    public Context context;
    ShareWithBTActivity shareWithBTActivity;
    RecyclerView recycler_selectedfile;
    ArrayList<Uri> selecteddata = new ArrayList<>();
    ArrayList<Filedata> selectedfiles = new ArrayList<>();

    public FileFragment(Context context, ShareWithBTActivity shareWithBTActivity) {
        this.context = context;
        this.shareWithBTActivity = shareWithBTActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FileAdapter fileAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_files, container, false);

        Button select_files;
        recycler_selectedfile = view.findViewById(R.id.recycler_selectedfile);
        recycler_selectedfile.addItemDecoration(new MarginDecoration(context));
        recycler_selectedfile.hasFixedSize();
        select_files = view.findViewById(R.id.select_files);
        if(selectedfiles.isEmpty()) {
            fileAdapter = new FileAdapter(selectedfiles, this);
            recycler_selectedfile.setAdapter(fileAdapter);
        }
        select_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addfiles();
            }
        });
        return view;
    }

    private void addfiles() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            ArrayList<Uri> currentUri = new ArrayList<>();

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 100) {
                    if (data != null) {
                        int count = data.getClipData().getItemCount();
                        int currentItem = 0;
                        while (currentItem < count) {
                            Uri uri = data.getClipData().getItemAt(currentItem).getUri();
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                            currentItem = currentItem + 1;
                            currentUri.add(uri);
                            String name = getFileName(uri);
                            File file = new File(uri.getPath());
                            int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                            Log.d("data_paths", "size --- " + file_size);
                            boolean isadd = true;
                            for (int i = 0; i < selecteddata.size(); i++) {
                                if (selecteddata.get(i).equals(uri)) {
                                    isadd = false;
                                }
                            }
                            if (isadd) {
                                selecteddata.add(uri);
                                selectedfiles.add(new Filedata(name,"0",uri));
                                fileAdapter.notifyDataSetChanged();
                                shareWithBTActivity.click(new Filedata(name,"0",uri),true);
                            }
                        }
                        Log.e("Paths", "Data -- " + selecteddata.size());
                    }
                }
            }
        } catch (Exception e) {
            Log.d("Paths", "Error --- " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void onClick(Filedata file, int position) {
        selecteddata.remove(position);
        selectedfiles.remove(position);
        fileAdapter.notifyDataSetChanged();
        shareWithBTActivity.click(file,false);
    }
}
