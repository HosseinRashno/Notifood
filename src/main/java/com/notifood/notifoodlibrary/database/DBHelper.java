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
    public static final String TABLE_RESTAURANT = "tblRestaurant";
    public static final String TABLE_WORKING_HOURS = "workingHours";
    // endregion

    // region Table columns

    // region Shared columns
    public static final String COL_ID = "id";
    // endregion

    // region Restaurant columns
    public static final String COL_RESTAURANT_INSTANCE = "restaurantInstance";
    public static final String COL_RESTAURANT_MINOR = "restaurantMinor";
    public static final String COL_RESTAURANT_NAME = "restaurantName";
    public static final String COL_RESTAURANT_ADDRESS = "restaurantAddress";
    // endregion

    // region Working hours
    public static final String COL_RESTAURANT_ID = "restaurantId";
    public static final String COL_START_TIME = "startTime";
    public static final String COL_END_TIME = "endTime";
    // endregion

    // endregion

    // region Table queries

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
                    COL_RESTAURANT_ID + " INTEGER," +
                    COL_START_TIME + " TEXT," +
                    COL_END_TIME + " TEXT" +
                    ");";

    // endregion

    public DBHelper() {
        super(ApplicationClass.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE_RESTAURANT);
        db.execSQL(QUERY_CREATE_WORKING_HOURS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKING_HOURS);

        onCreate(db);
    }
}
