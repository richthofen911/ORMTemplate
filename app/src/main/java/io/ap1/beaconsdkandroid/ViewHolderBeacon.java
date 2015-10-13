package io.ap1.beaconsdkandroid;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;

/**
 * Created by admin on 08/10/15.
 */
public class ViewHolderBeacon extends RecyclerView.ViewHolder{
    public TextView tv_beaconList_uuid;
    public TextView tv_beaconList_major;
    public TextView tv_beaconList_minor;
    public TextView tv_beaconList_inDatabase;

    public int selfPosition;

    public ViewHolderBeacon(View rootview){
        super(rootview);
        tv_beaconList_uuid = (TextView) rootview.findViewById(R.id.tv_beaconList_uuid);
        tv_beaconList_major = (TextView) rootview.findViewById(R.id.tv_beaconList_major);
        tv_beaconList_minor = (TextView) rootview.findViewById(R.id.tv_beaconList_minor);
        tv_beaconList_inDatabase = (TextView) rootview.findViewById(R.id.tv_beaconList_inDB);
    }
}
