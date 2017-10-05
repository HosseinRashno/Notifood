package com.notifood.notifoodlibrary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.notifood.notifoodlibrary.ApplicationClass;

/**
 * Created by mrashno on 10/4/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    //region Static definitions
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dbNotifood";
    //endregion

    // region Table names
    public static final String TABLE_SETTINGS = "tblSetting";
    public static final String TABLE_RESTAURANT = "tblRestaurant";
    public static final String TABLE_WORKING_HOURS = "workingHours";
    // endregion

    // region Table columns

    // region Shared columns
    public static final String COL_ID = "id";
    // endregion

    // region Setting columns
    public static final String COL_KEY_NAME = "KeyName";
    public static final String COL_TEXT_VALUE = "TextValue";
    public static final String COL_INTEGER_VALUE = "IntegerValue";
    // endregion

    // region Restaurant columns
    public static final String COL_RESTAURANT_INSTANCE = "restaurantInstance";
    public static final String COL_RESTAURANT_MINOR = "restaurantMinor";
    public static final String COL_RESTAURANT_NAME = "restaurantName";
    public static final String COL_RESTAURANT_ADDRESS = "restaurantAddress";
    // endregion

    // region Working hours
    public static final String COL_RESTAURANt_ID = "restaurantId";
    public static final String COL_START_TIME = "startTime";
    public static final String COL_END_TIME = "endTime";
    // endregion

    // endregion

    // region Setting keys
    public static final String KEY_BEACON_TYPE = "beaconType";
    public static final String KEY_EDDYSTONE_NAMESPACE = "eddystoneNamespace";
    public static final String KEY_EDDYSTONE_INSTANCE_ID_START = "eddystoneInstanceIdStart";
    public static final String KEY_EDDYSTONE_INSTANCE_ID_END = "eddystoneInstanceIdEnd";
    public static final String KEY_IBEACON_UUID = "ibeaconUUID";
    public static final String KEY_IBEACON_MAJOR = "ibeaconMajor";
    public static final String KEY_IBEACON_MINOR_START = "ibeaconMinorStart";
    public static final String KEY_IBEACON_MINOR_END = "ibeaconMinorEnd";
    public static final String KEY_IS_ENABLED = "isEnabled";
    public static final String KEY_PACKAGE_NAME = "packageName";
    public static final String KEY_GUID = "GUID";
    public static final String KEY_DEV_KEY = "devKey";
    public static final String KEY_UPDATE_PERIOD = "updatePeriod";
    // endregion

    // region Table queries
    private static final String QUERY_CREATE_SETTINGS =
            "CREATE TABLE "+TABLE_SETTINGS+" (" +
                    COL_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    COL_KEY_NAME + " TEXT,"+
                    COL_TEXT_VALUE + " TEXT,"+
                    COL_INTEGER_VALUE + " INTEGER"+
                    ");";

    private static final String QUERY_CREATE_RESTAURANT =
            "CREATE TABLE "+TABLE_RESTAURANT+" (" +
                    COL_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    COL_RESTAURANT_INSTANCE + " TEXT," +
                    COL_RESTAURANT_MINOR + " TEXT," +
                    COL_RESTAURANT_NAME + " TEXT," +
                    COL_RESTAURANT_ADDRESS + " TEXT" +
                    ");";

    private static final String QUERY_CREATE_WORKING_HOURS =
            "CREATE TABLE "+TABLE_WORKING_HOURS+" (" +
                    COL_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    COL_RESTAURANt_ID + " INTEGER," +
                    COL_START_TIME + " TEXT," +
                    COL_END_TIME + " TEXT" +
                    ");";

    // endregion

    public DBHelper() {
        super(ApplicationClass.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE_SETTINGS);
        db.execSQL(QUERY_CREATE_RESTAURANT);
        db.execSQL(QUERY_CREATE_WORKING_HOURS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKING_HOURS);

        onCreate(db);
    }
}
