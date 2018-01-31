package com.notifood.notifoodlibrary.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.notifood.notifoodlibrary.models.ContactObjectModel;
import com.notifood.notifoodlibrary.models.RestaurantModel;
import com.notifood.notifoodlibrary.models.SettingModel;
import com.notifood.notifoodlibrary.models.TimeObjectModel;
import com.notifood.notifoodlibrary.models.WorkingHoursModel;
import com.notifood.notifoodlibrary.utils.Declaration;
import com.notifood.notifoodlibrary.utils.LibPreferences;
import com.notifood.notifoodlibrary.utils.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.notifood.notifoodlibrary.database.DBHelper.COL_CONTACTS_INDEX;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_CONTACTS_TITLE;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_CONTACTS_VALUE;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_DESCRIPTION;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_END_TIME_REAL;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_END_TIME_TEXT;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_ID;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_IMAGE;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_KEY_RESTAURANT_ID;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_LATITUDE;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_LONGITUDE;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RANK;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RESTAURANT_ADDRESS;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RESTAURANT_INSTANCE;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RESTAURANT_MINOR;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_RESTAURANT_NAME;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_START_TIME_REAL;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_START_TIME_TEXT;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_TYPE;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_WEEK_DAY_INDEX;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_WEEK_DAY_NAME;
import static com.notifood.notifoodlibrary.database.DBHelper.TABLE_RESTAURANT;
import static com.notifood.notifoodlibrary.database.DBHelper.TABLE_RESTAURANT_CONTACTS;
import static com.notifood.notifoodlibrary.database.DBHelper.TABLE_WORKING_HOURS_IN_DAY;
import static com.notifood.notifoodlibrary.utils.Utility.removeIllegalCharacterForSQLite;

/**
 * Created by mrashno on 10/4/2017.
 */

public class RestaurantTBL extends DatabaseClasses {
    public void insertRestaurantData(ArrayList<RestaurantModel> models){
        boolean result = true;
        OpenForWrite();

        try{
            currentDB.beginTransaction();

            // Get beacon type
            SettingModel settingModel = LibPreferences.getSerializable(Declaration.KEY_SETTINGS, SettingModel.class);

            // Delete all tables
            currentDB.delete(TABLE_RESTAURANT, null, null);
            currentDB.delete(TABLE_WORKING_HOURS_IN_DAY, null, null);
            currentDB.delete(TABLE_RESTAURANT_CONTACTS, null, null);

            // Insert restaurants
            for (RestaurantModel restaurant: models){
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_LATITUDE, restaurant.getLatitude());
                contentValues.put(COL_LONGITUDE, restaurant.getLongitude());
                contentValues.put(COL_IMAGE, restaurant.getImage());
                contentValues.put(COL_DESCRIPTION, restaurant.getDescription());
                contentValues.put(COL_TYPE, restaurant.getBeaconType().getCode());
                contentValues.put(COL_RANK, restaurant.getRank());
                contentValues.put(COL_RESTAURANT_NAME, removeIllegalCharacterForSQLite(restaurant.getRestaurantName()));
                contentValues.put(COL_RESTAURANT_ADDRESS, removeIllegalCharacterForSQLite(restaurant.getRawAddress()));

                if (settingModel.getBeaconType() == Declaration.enmBeaconType.enm_BT_EDDYSTONE){
                    contentValues.put(COL_RESTAURANT_INSTANCE, restaurant.getEddystoneInstance());
                } else if (settingModel.getBeaconType() == Declaration.enmBeaconType.enm_BT_IBEACON){
                    contentValues.put(COL_RESTAURANT_MINOR, restaurant.getIbeaconMinor());
                }

                long idOfRestaurant = currentDB.insert(TABLE_RESTAURANT, null, contentValues);

                // Insert working hours in day
                long insertedIds = 1;
                for (WorkingHoursModel workingHoursModel: restaurant.getWorkingHours()){
                    for (TimeObjectModel timeObjectModel:workingHoursModel.getTimes()) {
                        ContentValues contentValuesWorkHours = new ContentValues();
                        contentValuesWorkHours.put(COL_KEY_RESTAURANT_ID, idOfRestaurant);
                        contentValuesWorkHours.put(COL_WEEK_DAY_INDEX, workingHoursModel.getDayIndex());
                        contentValuesWorkHours.put(COL_WEEK_DAY_NAME, workingHoursModel.getWeekDay());
                        contentValuesWorkHours.put(COL_START_TIME_TEXT, timeObjectModel.getStart());
                        contentValuesWorkHours.put(COL_START_TIME_REAL, calculateRealFromTime(timeObjectModel.getStart()));
                        contentValuesWorkHours.put(COL_END_TIME_TEXT, timeObjectModel.getEnd());
                        contentValuesWorkHours.put(COL_END_TIME_REAL, calculateRealFromTime(timeObjectModel.getEnd()));
                        insertedIds *= currentDB.insert(TABLE_WORKING_HOURS_IN_DAY, null, contentValuesWorkHours);
                    }
                }

                // Insert contact numbers
                for (ContactObjectModel contactObjectModel:restaurant.getContacts()){
                    for (String value: contactObjectModel.getValue()){
                        ContentValues contentValuesContacts = new ContentValues();
                        contentValuesContacts.put(COL_KEY_RESTAURANT_ID, idOfRestaurant);
                        contentValuesContacts.put(COL_CONTACTS_INDEX, contactObjectModel.getCntIndex());
                        contentValuesContacts.put(COL_CONTACTS_TITLE, contactObjectModel.getTitle());
                        contentValuesContacts.put(COL_CONTACTS_VALUE, value);
                        insertedIds *= currentDB.insert(TABLE_RESTAURANT_CONTACTS, null, contentValuesContacts);
                    }
                }

                if ((insertedIds*idOfRestaurant)<0){
                    result = false;
                    break;
                }
            }


            if (result)
                currentDB.setTransactionSuccessful();
        } catch (Exception ex){
            Utility.NotifoodLog("Error while inserting restaurants, reason:"+ex.toString());
        } finally {
            currentDB.endTransaction();
        }
        CloseConnection();
    }

    public void deleteAllObjectAndRelated(){
        OpenForWrite();

        try{
            currentDB.beginTransaction();

            currentDB.delete(TABLE_RESTAURANT, null, null);
            currentDB.delete(TABLE_WORKING_HOURS_IN_DAY, null, null);
            currentDB.delete(TABLE_RESTAURANT_CONTACTS, null, null);

            currentDB.setTransactionSuccessful();
        } catch (Exception ex){
            Utility.NotifoodLog("Error while deleting restaurants, reason:"+ex.toString());
        } finally {
            currentDB.endTransaction();
        }


        CloseConnection();
    }

    public boolean canShowNotification(String minorOrInstance){
        boolean result = false;

        OpenForRead();

        // Get beacon type
        String beaconFieldName = "";
        SettingModel settingModel = LibPreferences.getSerializable(Declaration.KEY_SETTINGS, SettingModel.class);
        beaconFieldName = settingModel.getBeaconType()==Declaration.enmBeaconType.enm_BT_EDDYSTONE?COL_RESTAURANT_INSTANCE:COL_RESTAURANT_MINOR;

        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        double currentRealTime = hours + ((double)minutes/60d);
        int greDay = cal.get(Calendar.DAY_OF_WEEK);
        Declaration.enmShamsiDays day = Utility.mapGreDaysToShamsiDays(greDay);

        String query = String.format(Locale.US, "select rest.%s " +
                                                "from  %s as rest inner join %s as workHour " +
                                                "on rest.%s = workHour.%s " +
                                                "where (rest.%s='%s') and (%f>workHour.%s) and (%f<workHour.%s) and (workHour.%s=%d or workHour.%s=%d);",
                                                COL_RESTAURANT_NAME,
                                                TABLE_RESTAURANT,
                                                TABLE_WORKING_HOURS_IN_DAY,
                                                COL_ID,
                                                COL_KEY_RESTAURANT_ID,
                                                beaconFieldName,
                                                minorOrInstance,
                                                currentRealTime,
                                                COL_START_TIME_REAL,
                                                currentRealTime,
                                                COL_END_TIME_REAL,
                                                COL_WEEK_DAY_INDEX,
                                                day.getCode(),
                                                COL_WEEK_DAY_INDEX,
                                                Declaration.enmShamsiDays.enmAllDays.getCode()
                                                );

        Cursor cursor = currentDB.rawQuery(query, null);
        if (cursor!=null && cursor.getCount()>0)
            result = true;

        cursor.close();
        CloseConnection();
        return result;
    }

    public RestaurantModel getRestaurantDetails(String minorOrInstance){
        RestaurantModel model = new RestaurantModel();
        OpenForRead();

        // Get beacon type
        String beaconFieldName = "";
        SettingModel settingModel = LibPreferences.getSerializable(Declaration.KEY_SETTINGS, SettingModel.class);
        beaconFieldName = settingModel.getBeaconType()==Declaration.enmBeaconType.enm_BT_EDDYSTONE?COL_RESTAURANT_INSTANCE:COL_RESTAURANT_MINOR;

        // Get main details
        String queryMain = String.format(Locale.US, "select * " +
                                                "from  %s " +
                                                "where %s='%s';",
                                                TABLE_RESTAURANT,
                                                beaconFieldName,
                                                minorOrInstance
                                                );

        int restaurantId = 0;
        Cursor cursorMain = currentDB.rawQuery(queryMain, null);
        if (cursorMain !=null && cursorMain.moveToFirst()) {
            restaurantId = cursorMain.getInt(cursorMain.getColumnIndex(COL_ID));
            model.setLatitude(cursorMain.getDouble(cursorMain.getColumnIndex(COL_LATITUDE)));
            model.setLongitude(cursorMain.getDouble(cursorMain.getColumnIndex(COL_LONGITUDE)));
            model.setImage(cursorMain.getString(cursorMain.getColumnIndex(COL_IMAGE)));
            model.setDescription(cursorMain.getString(cursorMain.getColumnIndex(COL_DESCRIPTION)));
            model.setBeaconType(Declaration.enmBeaconType.values()[cursorMain.getInt(cursorMain.getColumnIndex(COL_TYPE))]);
            model.setRank(cursorMain.getDouble(cursorMain.getColumnIndex(COL_RANK)));
            model.setRestaurantName(cursorMain.getString(cursorMain.getColumnIndex(COL_RESTAURANT_NAME)));
            model.setRawAddress(cursorMain.getString(cursorMain.getColumnIndex(COL_RESTAURANT_ADDRESS)));
            if (settingModel.getBeaconType()== Declaration.enmBeaconType.enm_BT_EDDYSTONE)
                model.setEddystoneInstance(cursorMain.getString(cursorMain.getColumnIndex(COL_RESTAURANT_INSTANCE)));
            else
                model.setIbeaconMinor(cursorMain.getInt(cursorMain.getColumnIndex(COL_RESTAURANT_MINOR)));
        }
        cursorMain.close();

        // Get Contacts
        String queryContacts = String.format(Locale.US, "select * " +
                                                                "from %s " +
                                                                "where %s=%s;",
                                                                TABLE_RESTAURANT_CONTACTS,
                                                                COL_KEY_RESTAURANT_ID,
                                                                restaurantId);

        ArrayList<ContactObjectModel> contactList = new ArrayList<>();
        Cursor cursorContacts = currentDB.rawQuery(queryContacts, null);
        if (cursorContacts!=null){
            while (cursorContacts.moveToNext()){
                ContactObjectModel contactObjectModel = new ContactObjectModel();
                contactObjectModel.setCntIndex(cursorContacts.getInt(cursorContacts.getColumnIndex(COL_CONTACTS_INDEX)));
                contactObjectModel.setTitle(cursorContacts.getString(cursorContacts.getColumnIndex(COL_CONTACTS_TITLE)));
                contactObjectModel.getValue().add(cursorContacts.getString(cursorContacts.getColumnIndex(COL_CONTACTS_VALUE)));

                contactList.add(contactObjectModel);
            }
        }
        model.setContacts(contactList);
        cursorContacts.close();

        // Get working hours
        String queryWorkingHours = String.format(Locale.US, "select * " +
                                                                    "from %s " +
                                                                    "where %s=%s;",
                                                                    TABLE_WORKING_HOURS_IN_DAY,
                                                                    COL_KEY_RESTAURANT_ID,
                                                                    restaurantId);

        ArrayList<WorkingHoursModel> workingHoursList = new ArrayList<>();
        Cursor cursorWH = currentDB.rawQuery(queryWorkingHours, null);
        if (cursorWH!=null){
            while(cursorWH.moveToNext()){
                WorkingHoursModel wh = new WorkingHoursModel();
                wh.setDayIndex(cursorWH.getInt(cursorWH.getColumnIndex(COL_WEEK_DAY_INDEX)));
                wh.setWeekDay(cursorWH.getString(cursorWH.getColumnIndex(COL_WEEK_DAY_INDEX)));

                TimeObjectModel timeObj = new TimeObjectModel();
                timeObj.setStart(cursorWH.getString(cursorWH.getColumnIndex(COL_START_TIME_TEXT)));
                timeObj.setEnd(cursorWH.getString(cursorWH.getColumnIndex(COL_END_TIME_TEXT)));
                wh.getTimes().add(timeObj);

                workingHoursList.add(wh);
            }
        }
        model.setWorkingHours(workingHoursList);
        cursorWH.close();

        CloseConnection();
        return model;
    }

    private Double calculateRealFromTime(String timeText){
        Double result = null;

        String[] values = timeText.split(":");
        if (values.length>1){
            result = Double.valueOf(values[0]);
            double minutes = Double.valueOf(values[1]);
            result += (minutes/60d);
        } else if (values.length==1){
            result = Double.valueOf(values[0]);
        } else {
            Utility.NotifoodLog("Time value is incorrect:"+timeText);
        }

        return result;
    }
}
