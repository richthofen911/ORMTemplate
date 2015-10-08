package io.ap1.beaconsdkandroid;

import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.perples.recosdk.RECOBeacon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityFindBeacon extends ActivityBeaconDetectionByRECO {

    final private String UUID_AprilBrother = "E2C56DB5-DFFB-48D2-B060-D0F5A71096E0";
    final static String urlBase = "http://apex.apengage.io/Oscar";
    public APICaller apiCaller;
    public MySingletonRequestQueue mRequestQueue;
    private SharedPreferences spHashValue;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_beacon);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null)
            bluetoothAdapter.enable(); // if Bluetooth Adapter exists, force enabling it.
        else
            Toast.makeText(getApplicationContext(), "Bluetoothas chip not found", Toast.LENGTH_SHORT).show();

        spHashValue = getApplication().getSharedPreferences("HashValue.sp", 0);

        databaseHelper = new DatabaseHelper(this);
        mRequestQueue = MySingletonRequestQueue.getInstance(this);
        apiCaller = APICaller.getInstance(this, mRequestQueue);

        assignRegionArgs(UUID_AprilBrother, -65);

    }

    private void getServerHash(){
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

    private void updateBeaconSet(JSONArray newBeaconSet){
        Gson gson = new Gson(); //use Gson to parse a Beacon JSONObject to a Java Bean
        for(int i = 0; i < newBeaconSet.length(); i++){
            try{
                 databaseHelper.saveABeacon(gson.fromJson(newBeaconSet.getJSONObject(i).toString(), Beacon.class)); //add all the beacons to the new DB
            }catch (JSONException e) {
                Log.e("Beacons traversal error", e.toString());
            }
        }
    }

    private Beacon populateOneBeaconToView(String uuid, String major, String minor, String rssi){
        Beacon oneDetectedBeacon = new Beacon();
        oneDetectedBeacon.setUuid(uuid);
        oneDetectedBeacon.setMajor(major);
        oneDetectedBeacon.setMinor(minor);
        oneDetectedBeacon.setRssi(rssi);
        return oneDetectedBeacon;
    }

    @Override
    protected void actionOnEnter(RECOBeacon recoBeacon) { //Called when the phone checks in with the assigned beacon region.
        /*
        Toast.makeText(getApplicationContext(), "beacon detected -- " +
                " :: " + recoBeacon.getProximityUuid() +
                " :: " + recoBeacon.getMajor() +
                " :: " + recoBeacon.getMinor() +
                " :: " + recoBeacon.getRssi()
                , Toast.LENGTH_SHORT).show();
        */
        populateOneBeaconToView(recoBeacon.getProximityUuid(), String.valueOf(recoBeacon.getMajor()), String.valueOf(recoBeacon.getMinor()), String.valueOf(recoBeacon.getRssi()));
    }
}
