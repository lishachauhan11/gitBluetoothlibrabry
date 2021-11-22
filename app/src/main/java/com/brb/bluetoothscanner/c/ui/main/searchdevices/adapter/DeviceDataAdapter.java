package com.brb.bluetoothscanner.c.ui.main.searchdevices.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brb.bluetoothscanner.c.R;
import com.brb.bluetoothscanner.c.ui.main.searchdevices.model.DeviceDataModel;

import java.util.List;

public class DeviceDataAdapter extends RecyclerView.Adapter<DeviceDataAdapter.ViewHolder> {


    private List<DeviceDataModel> devicedata;
    private final OnItemClickListener listener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_device_name,tv_device_add,tv_device_rssid,tv_device_notavailable;
        private RelativeLayout item_view;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tv_device_name =  view.findViewById(R.id.tv_device_name);
            tv_device_add =  view.findViewById(R.id.tv_device_add);
            tv_device_rssid =  view.findViewById(R.id.tv_device_rssid);
            tv_device_notavailable =  view.findViewById(R.id.tv_device_notavailable);
            item_view =  view.findViewById(R.id.item_view);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(DeviceDataModel item);
    }

    public void add(DeviceDataModel data){
        devicedata.add(data);
    }

    public void edit(DeviceDataModel data,int position){
        devicedata.remove(position);
        devicedata.add(position,data);
    }

    public void remove(DeviceDataModel data){
        devicedata.remove(data);
    }

    public DeviceDataAdapter(List<DeviceDataModel> devicedata, OnItemClickListener listener) {
        this.devicedata = devicedata;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_devicedata, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(devicedata.get(position).dname != null) {
            if(devicedata.get(position).dname.length() >14){
                viewHolder.tv_device_name.setText(devicedata.get(position).dname.substring(0,13)+"..");
            }else {
                viewHolder.tv_device_name.setText(devicedata.get(position).dname);
            }
            viewHolder.tv_device_notavailable.setVisibility(View.GONE);
        }
        else{
            viewHolder.tv_device_name.setText("N/A");
            viewHolder.tv_device_notavailable.setVisibility(View.VISIBLE);
            viewHolder.tv_device_notavailable.setTextColor(Color.RED);
        }
        viewHolder.tv_device_add.setText(devicedata.get(position).dadd);
        viewHolder.tv_device_rssid.setText(devicedata.get(position).rssid+"");
        viewHolder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(devicedata.get(position));
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devicedata.size();
    }
}
