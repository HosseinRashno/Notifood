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
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "dbNotifood";
    //endregion

    // region Table names
    public static final String TABLE_RESTAURANT = "tblRestaurant";
    public static final String TABLE_WORKING_HOURS_IN_DAY = "tblWorkingHoursInDays";
    public static final String TABLE_RESTAURANT_CONTACTS = "tblRestaurantsContacts";
    // endregion

    // region Table columns

    // region Shared columns
    public static final String COL_ID = "id";
    public static final String COL_KEY_RESTAURANT_ID = "restaurantId";
    // endregion

    // region Restaurant columns
    public static final String COL_LATITUDE = "restaurantLatitude";
    public static final String COL_LONGITUDE = "restaurantLongitude";
    public static final String COL_IMAGE = "restaurantImage";
    public static final String COL_DESCRIPTION = "restaurantDescription";
    public static final String COL_TYPE = "restaurantType";
    public static final String COL_RANK = "restaurantRank";
    public static final String COL_RESTAURANT_NAME = "restaurantName";
    public static final String COL_RESTAURANT_INSTANCE = "restaurantInstance";
    public static final String COL_RESTAURANT_MINOR = "restaurantMinor";
    public static final String COL_RESTAURANT_ADDRESS = "restaurantAddress";
    // endregion

    // region Working hours
    public static final String COL_WEEK_DAY_INDEX = "weekDayIndex";
    public static final String COL_WEEK_DAY_NAME = "weekDayName";
    public static final String COL_START_TIME_TEXT = "startTimeText";
    public static final String COL_START_TIME_REAL = "startTimeReal";
    public static final String COL_END_TIME_TEXT = "endTimeText";
    public static final String COL_END_TIME_REAL = "endTimeReal";
    // endregion

    // region Contacts
    public static final String COL_CONTACTS_INDEX = "contactsIndex";
    public static final String COL_CONTACTS_TITLE = "contactsTitle";
    public static final String COL_CONTACTS_VALUE = "contactsValue";
    // endregion

    // endregion

    // region Table queries

    private static final String QUERY_CREATE_RESTAURANT =
            "CREATE TABLE "+TABLE_RESTAURANT+" (" +
                    COL_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    COL_LATITUDE + " REAL," +
                    COL_LONGITUDE + " REAL," +
                    COL_IMAGE + " TEXT," +
                    COL_DESCRIPTION + " TEXT," +
                    COL_TYPE + " INTEGER," +
                    COL_RANK + " REAL," +
                    COL_RESTAURANT_INSTANCE + " TEXT," +
                    COL_RESTAURANT_MINOR + " INTEGER," +
                    COL_RESTAURANT_NAME + " TEXT," +
                    COL_RESTAURANT_ADDRESS + " TEXT" +
                    ");";

    private static final String QUERY_CREATE_WORKING_HOURS_IN_DAY =
            "CREATE TABLE "+TABLE_WORKING_HOURS_IN_DAY+" (" +
                    COL_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    COL_KEY_RESTAURANT_ID + " INTEGER," +
                    COL_WEEK_DAY_INDEX + " INTEGER," +
                    COL_WEEK_DAY_NAME + " TEXT," +
                    COL_START_TIME_TEXT + " TEXT," +
                    COL_START_TIME_REAL + " REAL," +
                    COL_END_TIME_TEXT + " TEXT," +
                    COL_END_TIME_REAL + " REAL" +
                    ");";

    private static final String QUERY_CREATE_RESTAURANTS_CONTACTS =
            "CREATE TABLE "+TABLE_RESTAURANT_CONTACTS+" (" +
                    COL_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    COL_KEY_RESTAURANT_ID + " INTEGER," +
                    COL_CONTACTS_INDEX + " INTEGER," +
                    COL_CONTACTS_TITLE + " TEXT," +
                    COL_CONTACTS_VALUE + " TEXT" +
                    ");";

    // endregion

    public DBHelper() {
        super(ApplicationClass.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE_RESTAURANT);
        db.execSQL(QUERY_CREATE_WORKING_HOURS_IN_DAY);
        db.execSQL(QUERY_CREATE_RESTAURANTS_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKING_HOURS_IN_DAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANT_CONTACTS);

        onCreate(db);
    }
}
