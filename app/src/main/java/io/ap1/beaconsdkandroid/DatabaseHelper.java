package io.ap1.beaconsdkandroid;

import android.content.Context;
//import android.database.SQLException;
import java.sql.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    private Dao<Beacon, Integer> beaconDao = null;
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
        }
        catch(SQLException e){
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try
        {
            TableUtils.dropTable(connectionSource, Beacon.class, true);
        }
        catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    private Dao<Beacon, Integer> getBeaconDao() throws SQLException{
        if (beaconDao == null)
            beaconDao = getDao(Beacon.class);
        return beaconDao;
    }

    public RuntimeExceptionDao<Beacon, Integer> getBeaconRuntimeExceptionDao(){
        if (beaconRuntimeExceptionDao == null)
            beaconRuntimeExceptionDao = getRuntimeExceptionDao(Beacon.class);
        return beaconRuntimeExceptionDao;
    }

    public void saveABeacon(Beacon newBeacon){
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

    @Override
    public void close(){
        super.close();
        beaconRuntimeExceptionDao = null;
    }
}
