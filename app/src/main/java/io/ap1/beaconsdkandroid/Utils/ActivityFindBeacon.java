package io.ap1.beaconsdkandroid.Utils;

import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.perples.recosdk.RECOBeacon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.ap1.beaconsdkandroid.APICaller;
import io.ap1.beaconsdkandroid.AdapterBeacon;
import io.ap1.beaconsdkandroid.Beacon;
import io.ap1.beaconsdkandroid.DataStore;
import io.ap1.beaconsdkandroid.DatabaseHelper;
import io.ap1.beaconsdkandroid.MySingletonRequestQueue;

public class ActivityFindBeacon extends ActivityBeaconDetectionByRECO {

    final static String urlBase = "http://apex.apengage.io/Oscar";
    public APICaller apiCaller;
    public MySingletonRequestQueue mRequestQueue;
    protected SharedPreferences spHashValue;
    protected DatabaseHelper databaseHelper;
    public static AdapterBeacon adapterBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null)
            bluetoothAdapter.enable(); // if Bluetooth Adapter exists, force enabling it.
        else
            Toast.makeText(getApplicationContext(), "Bluetooth chip not found", Toast.LENGTH_SHORT).show();

        spHashValue = getApplication().getSharedPreferences("HashValue.sp", 0);

        databaseHelper = new DatabaseHelper(this);
        mRequestQueue = MySingletonRequestQueue.getInstance(this);
        apiCaller = APICaller.getInstance(this, mRequestQueue);

        adapterBeacon = new AdapterBeacon(DataStore.detectedBeaconList);

    }

    protected void getServerHash(){
        Map<String, String> postParams = new HashMap<>();
        final String currentLocalHashValue = spHashValue.getString("HashValue", "empty");
        if(currentLocalHashValue.equals("empty"))
            postParams.put("hash", "1");
        else
            postParams.put("hash", currentLocalHashValue);
        apiCaller.setAPI(urlBase, "/getAllBeacons.php", null, postParams, Request.Method.POST);
        apiCaller.execAPI(new APICaller.VolleyCallback() {
            @Override
            public void onDelivered(String result) {
                Log.e("resp getAllBeacons", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (!jsonObject.getString("hash").equals(currentLocalHashValue)) {
                        databaseHelper.deleteAllBeacons(ActivityFindBeacon.this); //drop the old beacon database and create a new one
                        JSONArray beaconSetRemote = jsonObject.getJSONArray("beacons");
                        updateBeaconSet(beaconSetRemote);
                    }
                } catch (JSONException e) {
                    Log.e("hash json error", e.toString());
                }
            }
        });
    }

    protected void updateBeaconSet(JSONArray newBeaconSet){
        Gson gson = new Gson(); //use Gson to parse a Beacon JSONObject to a Java Bean
        for(int i = 0; i < newBeaconSet.length(); i++){
            try{
                 databaseHelper.saveABeacon(gson.fromJson(newBeaconSet.getJSONObject(i).toString(), Beacon.class)); //add all the beacons to the new DB
            }catch (JSONException e) {
                Log.e("Beacons traversal error", e.toString());
            }
        }
    }

    protected void populateOneBeaconToDataStoreDetectedBeaconList(String uuid, String major, String minor, String rssi){
        Beacon newDetectedBeacon = new Beacon();
        newDetectedBeacon.setUuid(uuid);
        newDetectedBeacon.setMajor(major);
        newDetectedBeacon.setMinor(minor);
        newDetectedBeacon.setRssi(rssi);

        if(DataStore.detectedBeaconList.size() >= 2){
            addData(DataStore.detectedBeaconList.size() - 1, DataStore.detectedBeaconList.get(DataStore.detectedBeaconList.size() - 1));
            DataStore.detectedBeaconList.set(DataStore.detectedBeaconList.size() - 2, newDetectedBeacon);
            for(int i = 0; i < DataStore.detectedBeaconList.size(); i++){
                Log.e("new beacon detected", DataStore.detectedBeaconList.get(i).getMajor() + ": " + DataStore.detectedBeaconList.get(i).getMinor());
            }
        }else {
            addData(DataStore.detectedBeaconList.size(), newDetectedBeacon);
            for(int i = 0; i < DataStore.detectedBeaconList.size(); i++) {
                Log.e("new beacon detected", DataStore.detectedBeaconList.get(i).toString() + ": " + DataStore.detectedBeaconList.get(i).getMinor());
            }
        }
    }

    @Override
    protected void actionOnEnter(RECOBeacon recoBeacon) { //Called when the phone checks in with the assigned beacon region.
        populateOneBeaconToDataStoreDetectedBeaconList(recoBeacon.getProximityUuid(), String.valueOf(recoBeacon.getMajor()), String.valueOf(recoBeacon.getMinor()), String.valueOf(recoBeacon.getRssi()));
    }

    protected void addData(int position, Beacon newBeacon) {
        DataStore.detectedBeaconList.add(position, newBeacon);
        adapterBeacon.notifyItemInserted(position);
        if (position != DataStore.detectedBeaconList.size() - 1) {
            adapterBeacon.notifyItemRangeChanged(position, DataStore.detectedBeaconList.size() - position);
        }
    }
}
