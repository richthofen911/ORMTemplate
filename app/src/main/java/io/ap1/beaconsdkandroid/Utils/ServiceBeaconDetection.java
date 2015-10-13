package io.ap1.beaconsdkandroid.Utils;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteBaseService;
    import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.ap1.beaconsdkandroid.APICaller;
import io.ap1.beaconsdkandroid.ActivityMain;
import io.ap1.beaconsdkandroid.Beacon;
import io.ap1.beaconsdkandroid.BeaconOperation;
import io.ap1.beaconsdkandroid.DataStore;
import io.ap1.beaconsdkandroid.DatabaseHelper;
import io.ap1.beaconsdkandroid.MySingletonRequestQueue;
import io.ap1.beaconsdkandroid.ServiceFindBeacon;

public class ServiceBeaconDetection extends OrmLiteBaseService<DatabaseHelper> implements RECOServiceConnectListener, RECORangingListener{

    final private String UUID_AprilBrother = "E2C56DB5-DFFB-48D2-B060-D0F5A71096E0";
    final static String urlBase = "http://apex.apengage.io/Oscar";
    public APICaller apiCaller;
    public MySingletonRequestQueue mRequestQueue;
    protected SharedPreferences spHashValue;
    protected DatabaseHelper databaseHelper;
    private Dao<Beacon, Integer> mBeaconDao;
    protected RuntimeExceptionDao<Beacon, Integer> mBeaconRuntimeExceptionDao;

    public ServiceBeaconDetection() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null){
            bluetoothAdapter.enable(); // if Bluetooth Adapter exists, force enabling it.
            Log.e("Bluetooth enabled", "");
        }else
            Toast.makeText(getApplicationContext(), "Bluetooth chip not found", Toast.LENGTH_SHORT).show();

        return new MyBinder(definedRegions);
    }

    private final boolean DISCONTINUOUS_SCAN = false;

    protected boolean entered = false;
    protected int exitCount = 0;
    protected boolean exited = false;
    protected int rssiBorder = 0;
    protected int currentMinor = 0;
    protected int previousCount = 0;
    protected boolean generalSearchMode = false;
    protected ArrayList<Beacon> detectedBeacons;

    protected RECOBeaconManager mRecoManager = RECOBeaconManager.getInstance(this, false, false);
    protected ArrayList<RECOBeaconRegion> definedRegions;
    protected boolean alreadyPlaying = false;

    protected void assignRegionArgs(String uuid, int borderValue, boolean useGeneralSearchMode){
        definedRegions = generateBeaconRegion(uuid);
        rssiBorder = borderValue;
        generalSearchMode = useGeneralSearchMode;
    }

    protected void assignRegionArgs(String uuid, int major, int borderValue, boolean useGeneralSearchMode){
        definedRegions = generateBeaconRegion(uuid, major);
        rssiBorder = borderValue;
        generalSearchMode = useGeneralSearchMode;
    }

    protected void assignRegionArgs(String uuid, int major, int minor, int borderValue, boolean useGeneralSearchMode){
        definedRegions = generateBeaconRegion(uuid, major, minor);
        rssiBorder = borderValue;
        generalSearchMode = useGeneralSearchMode;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        assignRegionArgs(UUID_AprilBrother, -65, true);
        mRecoManager.setRangingListener(this);
        mRecoManager.bind(this);
        Log.e("bind manager", "");
        detectedBeacons = new ArrayList<>();
        spHashValue = getApplication().getSharedPreferences("HashValue.sp", 0);
        mRequestQueue = MySingletonRequestQueue.getInstance(this);
        apiCaller = APICaller.getInstance(this, mRequestQueue);
        databaseHelper = new DatabaseHelper(this);
        try{
            mBeaconDao = getHelper().getBeaconDao();
        }catch (SQLException e){
            Log.e("sql exception", e.toString());
        }

        mBeaconRuntimeExceptionDao = getHelper().getBeaconRuntimeExceptionDao();
    }

    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //Checks if wifi is on first.
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mWifi.isConnected()) {
            Intent enableWifiIntent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
            startActivityForResult(enableWifiIntent, 1234);
        }
        return START_STICKY;
    }
    */

    public void start(ArrayList<RECOBeaconRegion> regions) {
        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.startRangingBeaconsInRegion(region);
                Log.e("start detecting", region.describeContents() + "");
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    public void stop(ArrayList<RECOBeaconRegion> regions) {
        Log.e("stop detecting", "...");
        for(RECOBeaconRegion region : regions) {
            try {
                mRecoManager.stopRangingBeaconsInRegion(region);
                entered = false;
            } catch (RemoteException e) {
                Log.i("RECORangingActivity", "Remote Exception");
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.i("RECORangingActivity", "Null Pointer Exception");
                e.printStackTrace();
            }
        }
    }

    private ArrayList<RECOBeaconRegion> generateBeaconRegion(String uuid) {
        ArrayList<RECOBeaconRegion> regions = new ArrayList<>();
        RECOBeaconRegion recoRegion;
        recoRegion = new RECOBeaconRegion(uuid, "Defined Region");
        regions.add(recoRegion);
        return regions;
    }

    private ArrayList<RECOBeaconRegion> generateBeaconRegion(String uuid, int major) {
        ArrayList<RECOBeaconRegion> regions = new ArrayList<>();
        RECOBeaconRegion recoRegion;
        recoRegion = new RECOBeaconRegion(uuid, major, "Defined Region");
        regions.add(recoRegion);
        return regions;
    }

    private ArrayList<RECOBeaconRegion> generateBeaconRegion(String uuid, int major, int minor) {
        ArrayList<RECOBeaconRegion> regions = new ArrayList<>();
        RECOBeaconRegion recoRegion;
        recoRegion = new RECOBeaconRegion(uuid, major, minor, "Defined Region");
        regions.add(recoRegion);
        return regions;
    }

    @Override
    public void onServiceConnect() {
        Log.e("RangingActivity", "onServiceConnect()");
        mRecoManager.setDiscontinuousScan(DISCONTINUOUS_SCAN);
        //start(definedRegions);
    }

    @Override
    public void onServiceFail(RECOErrorCode recoErrorCode) {
        Log.e("RECO service error:", recoErrorCode.toString());
    }

    protected void actionOnEnter(RECOBeacon recoBeacon){
        populateOneBeaconToDataStoreDetectedBeaconList(recoBeacon.getProximityUuid(), String.valueOf(recoBeacon.getMajor()), String.valueOf(recoBeacon.getMinor()), String.valueOf(recoBeacon.getRssi()));
    }

    protected void actionOnExit(RECOBeacon recoBeacon){}

    private void inOut(int theRssi, RECOBeacon recoBeacon){
        if(!generalSearchMode){
            if(theRssi > rssiBorder){ // if the beacon is detected and its rssi is stronger enough, which means it is the beacon for the specific location, not a random one
                if(!entered && !alreadyPlaying){ //if haven't entered, do it
                    exitCount = 0;
                    entered = true;
                    exited = false;
                    Log.e("put a checkin", " with beacon " + recoBeacon.getProximityUuid() + " :: " + recoBeacon.getMajor() + " :: " + recoBeacon.getMinor());
                    currentMinor = recoBeacon.getMinor();
                    actionOnEnter(recoBeacon);
                }else{
                    Log.e("entered already", ")");
                }
            }else{
                if(recoBeacon.getMinor() == currentMinor){
                    if(exitCount < 3){
                        exitCount++;
                    }else {
                        if(!exited){ // if haven't exited, do it
                            entered = false;
                            exited = true;
                            currentMinor = 0;
                            actionOnExit(recoBeacon);
                        }else {
                            Log.e("exited already", ")");
                        }
                    }
                }else{
                    Log.e("not this beacon", String.valueOf(recoBeacon.getMinor()));
                }
            }
        }else {
            Beacon newDetectedBeacon = new Beacon();
            newDetectedBeacon.setUuid(recoBeacon.getProximityUuid());
            newDetectedBeacon.setMajor(String.valueOf(recoBeacon.getMajor()));
            newDetectedBeacon.setMinor(String.valueOf(recoBeacon.getMinor()));

            boolean isNewDetected = true;
            for(Beacon detectedBeacon: detectedBeacons){
                if(BeaconOperation.equals(newDetectedBeacon, detectedBeacon)){
                    isNewDetected = false;
                    Log.e("beacon detected", "already");
                    break;
                }
            }
            if(isNewDetected){
                actionOnEnter(recoBeacon);
                Log.e("add new detected", "beacon");
                detectedBeacons.add(newDetectedBeacon);
            }
            /*
            if(!detectedBeacons.contains(newDetectedBeacon)){ // maybe need to use compare method
                actionOnEnter(recoBeacon);
                detectedBeacons.add(newDetectedBeacon);
            }
            */
        }
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoBeaconRegion) {
        synchronized (recoBeacons){
            for(RECOBeacon recoBeacon: recoBeacons){
                Log.e("beacon detected, rssi", String.valueOf(recoBeacon.getRssi()));
                inOut(recoBeacon.getRssi(), recoBeacon);
            }
        }
    }

    @Override
    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion recoBeaconRegion, RECOErrorCode recoErrorCode) {
        Log.e("RECO ranging error:", recoErrorCode.toString());
    }

    protected void getRemoteServerHash(){
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
                        databaseHelper.deleteAllBeacons(ServiceBeaconDetection.this); //drop the old beacon database and create a new one
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

    protected void addData(int position, Beacon newBeacon) {
        DataStore.detectedBeaconList.add(position, newBeacon);
        ActivityMain.adapterBeacon.notifyItemInserted(position);
        if (position != DataStore.detectedBeaconList.size() - 1) {
            ActivityMain.adapterBeacon.notifyItemRangeChanged(position, DataStore.detectedBeaconList.size() - position);
        }
    }

    @Override
    public boolean onUnbind(Intent intent){
        super.onUnbind(intent);
        try{
            mRecoManager.unbind();
            Log.e("Reco manager", "unbound");
        }catch (RemoteException e){
            Log.e("on destroy error", e.toString());
        }
        return false;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        /*
        try{
            mRecoManager.unbind();
        }catch (RemoteException e){
            Log.e("on destroy error", e.toString());
        }
        */
    }

    public class MyBinder extends Binder {
        ArrayList<RECOBeaconRegion> definedRegions;
        public MyBinder(ArrayList<RECOBeaconRegion> definedRegions){
            this.definedRegions = definedRegions;
        }

        public void startToScan(){
            start(definedRegions);
        }

        public void stopScanning(){
            stop(definedRegions);
        }

        public void getServerHash(){
            getRemoteServerHash();
        }
    }

}
