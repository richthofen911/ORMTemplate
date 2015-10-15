package io.ap1.beaconsdkandroid;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.perples.recosdk.RECOBeacon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.ap1.beaconsdkandroid.Utils.ActivityBeaconDetectionByRECO;
import io.ap1.beaconsdkandroid.Utils.ActivityFindBeacon;
import io.ap1.beaconsdkandroid.Utils.ServiceBeaconDetection;

public class ActivityMain extends Activity {
//public class ActivityMain extends ActivityFindBeacon {

    final private String UUID_AprilBrother = "E2C56DB5-DFFB-48D2-B060-D0F5A71096E0";
    public static APICaller apiCaller;
    public static MySingletonRequestQueue mySingletonRequestQueue;
    public static AdapterBeacon adapterBeacon;

    private ServiceBeaconDetection.MyBinder myBinder;

    private static Context contextActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextActivityMain = getApplicationContext();
        adapterBeacon = new AdapterBeacon(DataStore.detectedBeaconList);
        mySingletonRequestQueue = MySingletonRequestQueue.getInstance(this);
        apiCaller = APICaller.getInstance(getApplicationContext(), mySingletonRequestQueue);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerViewBeaconList = (RecyclerView) findViewById(R.id.RecyclerView_beaconList); //set up RecyclerView
        recyclerViewBeaconList.setLayoutManager(linearLayoutManager);
        recyclerViewBeaconList.setHasFixedSize(true);
        recyclerViewBeaconList.setAdapter(adapterBeacon);

        bindServiceFindBeacon();

        //assignRegionArgs(UUID_AprilBrother, -65, true);

        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBinder.startToScan();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBinder.stopScanning();
            }
        });

        findViewById(R.id.btn_checkRemoteDB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBinder.getServerHash();
            }
        });

    }

    public static void seeBeaconDetail(){
        Toast.makeText(contextActivityMain, "Clicked", Toast.LENGTH_SHORT).show();
        contextActivityMain.startActivity(new Intent(contextActivityMain, ActivityBeaconDetail.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Service FindBeacon", "Connected");
            myBinder = (ServiceBeaconDetection.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("Service FindBeacon", "Disconnected");
        }
    };

    private void bindServiceFindBeacon(){
        bindService(new Intent(ActivityMain.this, ServiceBeaconDetection.class), conn, BIND_AUTO_CREATE);
    }

    private void unbindServiceFindBeacon(){
        unbindService(conn);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindServiceFindBeacon();
    }

}
