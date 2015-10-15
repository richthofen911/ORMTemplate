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
        viewHolderBeacon.tv_beaconList_uuid.append(detectedBeaconList.get(position).getUuid());
        viewHolderBeacon.tv_beaconList_major.append(detectedBeaconList.get(position).getMajor());
        viewHolderBeacon.tv_beaconList_minor.append(detectedBeaconList.get(position).getMinor());
        //if the beacon is in the database, put yes, else, put no
        if(DatabaseHelper.isBeaconInLocalDB(detectedBeaconList.get(position))){
            viewHolderBeacon.tv_beaconList_inDatabase.append("YES");

            /*
            viewHolderBeacon.tv_beaconList_inDatabase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //put beacon content into ActivityMain's intent
                    BeaconPassMiddleWare.pushBeacon(detectedBeaconList.get(position));
                    Log.e("beacon pushed", "" + detectedBeaconList.get(position).getId());

                    //Bundle beaconData = new Bundle();
                    //beaconData.putString("uuid", detectedBeaconList.get(position).getUuid());
                    //beaconData.putString("major", detectedBeaconList.get(position).getMajor());
                    //beaconData.putString("minor", detectedBeaconList.get(position).getMinor());
                    //beaconData.putString("url", detectedBeaconList.get(position).getUrl());

                    ActivityMain.seeBeaconDetail();
                }
            });
            */
        }
        else
            viewHolderBeacon.tv_beaconList_inDatabase.append("NO");

        viewHolderBeacon.selfPosition = position;
    }

    @Override
    public int getItemCount() {
        return detectedBeaconList.size();
    }
}
