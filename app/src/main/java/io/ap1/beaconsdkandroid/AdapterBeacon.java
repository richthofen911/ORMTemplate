package io.ap1.beaconsdkandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.ap1.beaconsdkandroid.Utils.ServiceBeaconDetection;

/**
 * Created by admin on 08/10/15.
 */
public class AdapterBeacon extends RecyclerView.Adapter<ViewHolderBeacon> {

    public ArrayList<Beacon> detectedBeaconList;

    public AdapterBeacon(ArrayList<Beacon> beaconsDetected){
        detectedBeaconList = beaconsDetected;
    }

    @Override
    public ViewHolderBeacon onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beaconlist, viewGroup, false);
        return new ViewHolderBeacon(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderBeacon viewHolderBeacon, final int position){
        viewHolderBeacon.tv_beaconList_uuid.setText("UUID: " + detectedBeaconList.get(position).getUuid());
        viewHolderBeacon.tv_beaconList_major.setText("MAJOR: " + detectedBeaconList.get(position).getMajor());
        viewHolderBeacon.tv_beaconList_minor.setText("MINOR: " + detectedBeaconList.get(position).getMinor());
        //if the beacon is in the database, put yes, else, put no
        if(DatabaseHelper.isBeaconInLocalDB(detectedBeaconList.get(position))){
            viewHolderBeacon.tv_beaconList_inDatabase.setText("In Database: " + "YES");
        }
        else
            viewHolderBeacon.tv_beaconList_inDatabase.setText("In Database: " + "NO");

        viewHolderBeacon.selfPosition = position;
    }

    @Override
    public int getItemCount() {
        return detectedBeaconList.size();
    }
}
