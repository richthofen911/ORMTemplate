package io.ap1.beaconsdkandroid;

import android.content.Context;
//import android.database.SQLException;
import java.sql.SQLException;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by admin on 06/10/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "beacons.db";
    private static final int DATABASE_VERSION = 1;

    private static Dao<Beacon, Integer> beaconDao = null;
    private RuntimeExceptionDao<Beacon, Integer> beaconRuntimeExceptionDao = null;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource){
        try{
            TableUtils.createTable(connectionSource, Beacon.class);
            beaconDao = getBeaconDao();
            beaconRuntimeExceptionDao = getBeaconRuntimeExceptionDao();
        } catch(SQLException e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Beacon.class, true);
        }
        catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public Dao<Beacon, Integer> getBeaconDao() throws SQLException{
        if (beaconDao == null){
            beaconDao = getDao(Beacon.class);
            Log.e("beaconDao", "null but created");
        }
        return beaconDao;
    }

    public RuntimeExceptionDao<Beacon, Integer> getBeaconRuntimeExceptionDao(){
        if (beaconRuntimeExceptionDao == null){
            beaconRuntimeExceptionDao = getRuntimeExceptionDao(Beacon.class);
            Log.e("beaconRuntimeExDao", "null but created");
        }
        return beaconRuntimeExceptionDao;
    }

    public void saveABeacon(Beacon newBeacon){
        if(beaconDao == null){
            try{
                beaconDao = getBeaconDao();
                Log.e("beaconDao saveBeacon", "null but created");
            } catch(SQLException e){
                Log.e(TAG, e.toString());
            }
        }
        try {
            beaconDao.createIfNotExists(newBeacon);
        }catch (SQLException e){
            Log.e("save beacon error", e.toString());
        }
    }

    public void deleteAllBeacons(Context context){
        this.close();
        context.deleteDatabase(DATABASE_NAME);
        OpenHelperManager.releaseHelper();
        OpenHelperManager.setHelper(new DatabaseHelper(context));
    }

    public static List<Beacon> queryForAllBeacons(){
        List<Beacon> beaconsInLocalDB;
        try {
            beaconsInLocalDB = beaconDao.queryForAll();
            return beaconsInLocalDB;
        }catch (SQLException e){
            Log.e("query all error", e.toString());
            return null;
        }
    }

    public static boolean isBeaconInLocalDB(Beacon beaconDetected){
        List<Beacon> beaconsInLocalDB = queryForAllBeacons();
        if(beaconsInLocalDB != null){
            for (Beacon localBeacon: beaconsInLocalDB){
                if(BeaconOperation.equals(beaconDetected, localBeacon)){
                    return true;
                }
            }
            return false;
        }else{
            Log.e("queryLocalBeacon", "error");
            return false;
        }
    }

    @Override
    public void close(){
        super.close();
        beaconRuntimeExceptionDao = null;
    }
}
