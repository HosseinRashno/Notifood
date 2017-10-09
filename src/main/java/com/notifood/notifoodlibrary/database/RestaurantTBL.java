package com.notifood.notifoodlibrary.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.notifood.notifoodlibrary.models.RestaurantModel;
import com.notifood.notifoodlibrary.models.WorkingHoursModel;
import com.notifood.notifoodlibrary.utils.Declaration;

import java.util.ArrayList;
import java.util.Locale;

import static com.notifood.notifoodlibrary.database.DBHelper.COL_END_TIME;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_ID;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RESTAURANT_ADDRESS;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RESTAURANT_ID;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RESTAURANT_INSTANCE;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RESTAURANT_MINOR;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RESTAURANT_NAME;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_START_TIME;
import static com.notifood.notifoodlibrary.database.DBHelper.TABLE_RESTAURANT;
import static com.notifood.notifoodlibrary.database.DBHelper.TABLE_WORKING_HOURS;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_BEACON_TYPE;
import static com.notifood.notifoodlibrary.utils.LibPreferences.getIntegerPref;
import static com.notifood.notifoodlibrary.utils.Utility.removeIllegalCharacterForSQLite;

/**
 * Created by mrashno on 10/4/2017.
 */

public class RestaurantTBL extends DatabaseClasses {
    @Override
    public ArrayList<Object> SelectAll() {
        return null;
    }

    @Override
    public Object SelectById(int id) {
        return null;
    }

    public RestaurantModel selectRestaurantByMinorOrInstance(String minorOrInstance){
        OpenForRead();
        RestaurantModel restaurantModel = null;

        String query = String.format(Locale.US, "select rest.%s, rest.%s, workHour.%s, workHour.%s " +
                "from  %s as rest inner join %s as workHour " +
                "on res.%s = workHour.%s " +
                COL_RESTAURANT_NAME,
                COL_RESTAURANT_ADDRESS,
                COL_START_TIME,
                COL_END_TIME,
                TABLE_RESTAURANT,
                TABLE_WORKING_HOURS,
                COL_ID,
                COL_RESTAURANT_ID);
        Declaration.enmBeaconType type = Declaration.enmBeaconType.values()[getIntegerPref(KEY_BEACON_TYPE)];
        if (type == Declaration.enmBeaconType.enm_BT_EDDYSTONE){
            query += String.format(Locale.US,
                    "where %s='%s';",
                    COL_RESTAURANT_INSTANCE,
                    minorOrInstance);
        } else if (type == Declaration.enmBeaconType.enm_BT_IBEACON){
            query += String.format(Locale.US,
                    "where %s='%s';",
                    COL_RESTAURANT_MINOR,
                    minorOrInstance);
        }

        Cursor cursor = currentDB.rawQuery(query, null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                restaurantModel.setRestaurantName(cursor.getString(cursor.getColumnIndex(COL_RESTAURANT_NAME)));
                restaurantModel.setRawAddress(cursor.getString(cursor.getColumnIndex(COL_RESTAURANT_ADDRESS)));

                WorkingHoursModel workingHoursModel = new WorkingHoursModel();
                workingHoursModel.setStartHourOfWork(cursor.getString(cursor.getColumnIndex(COL_START_TIME)));
                workingHoursModel.setStartHourOfWork(cursor.getString(cursor.getColumnIndex(COL_END_TIME)));
                restaurantModel.getWorkingHours().add(workingHoursModel);
            }
        }
        cursor.close();

        CloseConnection();
        return restaurantModel;
    }

    @Override
    public boolean InsertToTable(Object object) {
        boolean result = true;
        OpenForWrite();
        ArrayList<RestaurantModel> restaurantModelArray = (ArrayList<RestaurantModel>) object;

        Declaration.enmBeaconType enmBeaconType = Declaration.enmBeaconType.values()[getIntegerPref(KEY_BEACON_TYPE)];

        currentDB.beginTransaction();
        try{
            // Delete all the stores before start
            currentDB.delete(TABLE_RESTAURANT, "1", null);

            // Insert restaurants
            for (RestaurantModel restaurant: restaurantModelArray){
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_RESTAURANT_NAME, removeIllegalCharacterForSQLite(restaurant.getRestaurantName()));
                contentValues.put(COL_RESTAURANT_ADDRESS, removeIllegalCharacterForSQLite(restaurant.getRawAddress()));
                if (enmBeaconType == Declaration.enmBeaconType.enm_BT_EDDYSTONE){
                    contentValues.put(COL_RESTAURANT_INSTANCE, restaurant.getEddystoneInsnace());
                } else if (enmBeaconType == Declaration.enmBeaconType.enm_BT_IBEACON){
                    contentValues.put(COL_RESTAURANT_MINOR, restaurant.getIbeaconMinor());
                }
                long idOfRestaurant = currentDB.insert(TABLE_RESTAURANT, null, contentValues);

                long multiplicationOfWorkingHours = 1;
                for (WorkingHoursModel workingHoursModel: restaurant.getWorkingHours()){
                    ContentValues contentValuesWorkHours = new ContentValues();
                    contentValuesWorkHours.put(COL_RESTAURANT_ID, idOfRestaurant);
                    contentValuesWorkHours.put(COL_START_TIME, workingHoursModel.getStartHourOfWork());
                    contentValuesWorkHours.put(COL_END_TIME, workingHoursModel.getEndHourOfWork());
                    multiplicationOfWorkingHours *= currentDB.insert(TABLE_WORKING_HOURS, null, contentValuesWorkHours);
                }

                if ((multiplicationOfWorkingHours*idOfRestaurant)<0){
                    result = false;
                    break;
                }
            }

            if (result)
                currentDB.setTransactionSuccessful();
        } catch (Exception ex){
            Log.e(Declaration.TAG, ex.getMessage());
        } finally {
            currentDB.endTransaction();
        }

        CloseConnection();
        return result;
    }

    @Override
    public boolean DeleteById(int id) {
        return false;
    }
}
