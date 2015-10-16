package io.ap1.beaconsdkandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ActivityBeaconDetail extends Activity {

    private TextView tv_beacon_detail_uuid;
    private TextView tv_beacon_detail_major;
    private TextView tv_beacon_detail_minor;
    private TextView tv_beacon_detail_rssi;
    private TextView tv_beacon_detail_macAddress;
    private TextView tv_beacon_detail_nickName;
    private TextView tv_beacon_detail_lat;
    private TextView tv_beacon_detail_lng;
    private TextView tv_beacon_detail_x;
    private TextView tv_beacon_detail_y;
    private TextView tv_beacon_detail_z;
    private TextView tv_beacon_detail_unit;
    private TextView tv_beacon_detail_url;
    private TextView tv_beacon_detail_openUrl;
    private TextView tv_beacon_detail_cid;

    private EditText et_beacon_detail_uuid;
    private EditText et_beacon_detail_major;
    private EditText et_beacon_detail_minor;
    private EditText et_beacon_detail_rssi;
    private EditText et_beacon_detail_macAddress;
    private EditText et_beacon_detail_nickName;
    private EditText et_beacon_detail_lat;
    private EditText et_beacon_detail_lng;
    private EditText et_beacon_detail_x;
    private EditText et_beacon_detail_y;
    private EditText et_beacon_detail_z;
    private EditText et_beacon_detail_unit;
    private EditText et_beacon_detail_url;
    private EditText et_beacon_detail_cid;

    private APICaller apiCaller;
    private MySingletonRequestQueue mRequestQueue;

    Beacon thisBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_detail);

        thisBeacon = BeaconPassMiddleWare.popBeacon();
        Log.e("this beacon uuid", thisBeacon.getUuid());
        mRequestQueue = MySingletonRequestQueue.getInstance(this);
        apiCaller = APICaller.getInstance(this, mRequestQueue);

        tv_beacon_detail_uuid = (TextView) findViewById(R.id.tv_beacon_detail_uuid);
        tv_beacon_detail_major = (TextView) findViewById(R.id.tv_beacon_detail_major);
        tv_beacon_detail_minor = (TextView) findViewById(R.id.tv_beacon_detail_minor);
        tv_beacon_detail_rssi = (TextView) findViewById(R.id.tv_beacon_detail_rssi);
        tv_beacon_detail_macAddress = (TextView) findViewById(R.id.tv_beacon_detail_macAddress);
        tv_beacon_detail_nickName = (TextView) findViewById(R.id.tv_beacon_detail_nickName);
        tv_beacon_detail_lat = (TextView) findViewById(R.id.tv_beacon_detail_lat);
        tv_beacon_detail_lng = (TextView) findViewById(R.id.tv_beacon_detail_lng);
        tv_beacon_detail_x = (TextView) findViewById(R.id.tv_beacon_detail_x);
        tv_beacon_detail_y = (TextView) findViewById(R.id.tv_beacon_detail_y);
        tv_beacon_detail_z = (TextView) findViewById(R.id.tv_beacon_detail_z);
        tv_beacon_detail_unit = (TextView) findViewById(R.id.tv_beacon_detail_unit);
        tv_beacon_detail_url = (TextView) findViewById(R.id.tv_beacon_detail_url);
        tv_beacon_detail_openUrl = (TextView) findViewById(R.id.tv_beacon_detail_openUrl);
        tv_beacon_detail_cid = (TextView) findViewById(R.id.tv_beacon_detail_cID);

        et_beacon_detail_uuid = (EditText) findViewById(R.id.et_beacon_detail_uuid);
        et_beacon_detail_major = (EditText) findViewById(R.id.et_beacon_detail_major);
        et_beacon_detail_minor = (EditText) findViewById(R.id.et_beacon_detail_minor);
        et_beacon_detail_rssi = (EditText) findViewById(R.id.et_beacon_detail_rssi);
        et_beacon_detail_macAddress = (EditText) findViewById(R.id.et_beacon_detail_macAddress);
        et_beacon_detail_nickName = (EditText) findViewById(R.id.et_beacon_detail_nickName);
        et_beacon_detail_lat = (EditText) findViewById(R.id.et_beacon_detail_lat);
        et_beacon_detail_lng = (EditText) findViewById(R.id.et_beacon_detail_lng);
        et_beacon_detail_x = (EditText) findViewById(R.id.et_beacon_detail_x);
        et_beacon_detail_y = (EditText) findViewById(R.id.et_beacon_detail_y);
        et_beacon_detail_z = (EditText) findViewById(R.id.et_beacon_detail_z);
        et_beacon_detail_unit = (EditText) findViewById(R.id.et_beacon_detail_unit);
        et_beacon_detail_url = (EditText) findViewById(R.id.et_beacon_detail_url);
        et_beacon_detail_cid = (EditText) findViewById(R.id.et_beacon_detail_cID);

        et_beacon_detail_uuid.setText(thisBeacon.getUuid());
        et_beacon_detail_major.setText(thisBeacon.getMajor());
        et_beacon_detail_minor.setText(thisBeacon.getMinor());
        et_beacon_detail_rssi.setText(thisBeacon.getRssi());
        et_beacon_detail_macAddress.setText(thisBeacon.getMacAddress());
        et_beacon_detail_nickName.setText(thisBeacon.getNickName());
        et_beacon_detail_lat.setText(thisBeacon.getLat());
        et_beacon_detail_lng.setText(thisBeacon.getLng());
        et_beacon_detail_x.setText(thisBeacon.getX());
        et_beacon_detail_y.setText(thisBeacon.getY());
        et_beacon_detail_z.setText(thisBeacon.getZ());
        et_beacon_detail_unit.setText(thisBeacon.getUnit());
        et_beacon_detail_url.setText(thisBeacon.getURL());
        et_beacon_detail_cid.setText(thisBeacon.getCID());

        if(et_beacon_detail_url.getText().toString().startsWith("http")){
            tv_beacon_detail_openUrl.setText("@");
            tv_beacon_detail_openUrl.setTextColor(Color.parseColor("#00CC00"));
            tv_beacon_detail_openUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filePath = et_beacon_detail_url.getText().toString().replace(":", "");
                    filePath = filePath.replace("/", "");
                    filePath = filePath.replace(".", "");
                    filePath = getExternalCacheDir().getPath() + "/" + filePath + ".html";
                    startActivity(new Intent(ActivityBeaconDetail.this, ActivityUrlContent.class).putExtra("filePath", filePath));
                }
            });
        }

        findViewById(R.id.btn_beacon_detail_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBeaconToRemoteDB();
            }
        });

        findViewById(R.id.btn_beacon_detail_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeThisBeacon(thisBeacon.getId());
            }
        });
    }

    private void addNewBeaconToRemoteDB(){
        Map<String, String> postParams = new HashMap<>();
        Log.e("beacon id", "to be added " );
        postParams.put("uuid", et_beacon_detail_uuid.getText().toString());
        postParams.put("major", et_beacon_detail_major.getText().toString());
        postParams.put("minor", et_beacon_detail_minor.getText().toString());
        postParams.put("rssi", et_beacon_detail_macAddress.getText().toString());
        postParams.put("macAddress", et_beacon_detail_macAddress.getText().toString());
        postParams.put("nickName", et_beacon_detail_nickName.getText().toString());
        postParams.put("lat", et_beacon_detail_lat.getText().toString());
        postParams.put("lng", et_beacon_detail_lng.getText().toString());
        postParams.put("x", et_beacon_detail_x.getText().toString());
        postParams.put("y", et_beacon_detail_y.getText().toString());
        postParams.put("z", et_beacon_detail_z.getText().toString());
        postParams.put("unit", et_beacon_detail_unit.getText().toString());
        postParams.put("url", et_beacon_detail_url.getText().toString());
        postParams.put("cID", et_beacon_detail_cid.getText().toString());

        apiCaller.setAPI(DataStore.urlBase, "/addBeacon.php", null, postParams, Request.Method.POST);
        apiCaller.execAPI(new APICaller.VolleyCallback() {
            @Override
            public void onDelivered(String result) {
                Log.e("resp addOneBeacon", result);
            }
        });
        ActivityMain.ifCheckRemoteHash = true;
        finish();
    }

    private void removeThisBeacon(String beaconId){
        Map<String, String> postParams = new HashMap<>();
        Log.e("beacon id", "to be del " + beaconId);
        postParams.put("id", beaconId);
        apiCaller.setAPI(DataStore.urlBase, "/deleteBeacon.php", null, postParams, Request.Method.POST);
        apiCaller.execAPI(new APICaller.VolleyCallback() {
            @Override
            public void onDelivered(String result) {
                Log.e("resp delOneBeacon", result);
            }
        });
        ActivityMain.ifCheckRemoteHash = true;
        finish();
    }


}
