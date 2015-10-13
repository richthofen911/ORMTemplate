package io.ap1.beaconsdkandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.perples.recosdk.RECOBeaconRegion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.ap1.beaconsdkandroid.Utils.ActivityFindBeacon;
import io.ap1.beaconsdkandroid.Utils.ServiceBeaconDetection;

public class ServiceFindBeacon extends ServiceBeaconDetection {

    final private String UUID_AprilBrother = "E2C56DB5-DFFB-48D2-B060-D0F5A71096E0";
    final static String urlBase = "http://apex.apengage.io/Oscar";

    public ServiceFindBeacon() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);

        //assignRegionArgs(UUID_AprilBrother, -65, true);

        //throw new UnsupportedOperationException("Not yet implemented");
        return new MyBinder(definedRegions);
    }

}
