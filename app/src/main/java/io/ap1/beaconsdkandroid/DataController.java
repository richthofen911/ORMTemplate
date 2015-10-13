package io.ap1.beaconsdkandroid;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 06/10/15.
 */
public class DataController extends OrmLiteBaseActivity<DatabaseHelper>{
    private static RuntimeExceptionDao<Beacon, Integer> mBeaconDao;
    SharedPreferences spHashValue;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mBeaconDao = getHelper().getBeaconRuntimeExceptionDao();

    }

    public DataController(){

    }

    public void deleteAll(){

    }

    public void deleteAllBeacons(){

    }

    public void deleteAllHash(){

    }

    public void deleteAllUrlContent(){

    }

    public void saveABeacon(Beacon aBeacon){

    }

    public boolean compareHashValue(){
        Map<String, String> postParams = new HashMap<>();
        postParams.put("hash", "1");
        ActivityMain.apiCaller.setAPI(DataStore.urlBase, "/getAllBeacons.php", null, postParams, Request.Method.POST);
        ActivityMain.apiCaller.execAPI(new APICaller.VolleyCallback() {
            @Override
            public void onDelivered(String result) {

            }
        });
        return true;
    }
}
