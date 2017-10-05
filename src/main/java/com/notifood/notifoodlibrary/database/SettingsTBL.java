package com.notifood.notifoodlibrary.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.notifood.notifoodlibrary.models.SettingModel;
import com.notifood.notifoodlibrary.models.SettingsObjectModel;
import com.notifood.notifoodlibrary.utils.Declaration;

import java.util.ArrayList;
import java.util.Locale;

import static com.notifood.notifoodlibrary.database.DBHelper.COL_INTEGER_VALUE;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_KEY_NAME;
import static com.notifood.notifoodlibrary.database.DBHelper.COL_TEXT_VALUE;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_BEACON_TYPE;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_EDDYSTONE_INSTANCE_ID_END;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_EDDYSTONE_INSTANCE_ID_START;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_EDDYSTONE_NAMESPACE;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_IBEACON_MAJOR;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_IBEACON_MINOR_END;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_IBEACON_MINOR_START;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_IBEACON_UUID;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_IS_ENABLED;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_UPDATE_PERIOD;
import static com.notifood.notifoodlibrary.database.DBHelper.TABLE_SETTINGS;
import static com.notifood.notifoodlibrary.utils.Utility.RemoveIllegalCharacterForSQLite;

/**
 * Created by mrashno on 10/4/2017.
 */

public class SettingsTBL extends DatabaseClasses {
    @Override
    public ArrayList<Object> SelectAll() {
        return null;
    }

    @Override
    public Object SelectById(int id) {
        return null;
    }

    public SettingsObjectModel SelectByColumnName(String columnName){
        SettingsObjectModel objValue = new SettingsObjectModel();
        objValue.setColumnName(columnName);
        OpenForRead();

        String query = String.format(Locale.US, "select * from %s where %s='%s';", TABLE_SETTINGS, COL_KEY_NAME, columnName);
        Cursor cursor =  currentDB.rawQuery(query, null);
        if (cursor!=null && cursor.moveToFirst()){
            objValue.setStringValue(cursor.getString(cursor.getColumnIndex(COL_TEXT_VALUE)));
            objValue.setIntegerValue(cursor.getInt(cursor.getColumnIndex(COL_INTEGER_VALUE)));
        }
        cursor.close();

        CloseConnection();
        return objValue;
    }

    @Override
    public boolean InsertToTable(Object object) {
        boolean result = true;
        OpenForWrite();
        SettingModel settingModel = (SettingModel)object;

        currentDB.beginTransaction();
        try{
            long resultTestValue = 1;
            // Beacon type
            ContentValues valuesBeaconType = new ContentValues();
            valuesBeaconType.put(COL_KEY_NAME, KEY_BEACON_TYPE);
            valuesBeaconType.put(COL_INTEGER_VALUE, settingModel.getBeaconType().getCode());
            resultTestValue *= insertOrUpdateSettingsColumn(valuesBeaconType, KEY_BEACON_TYPE);

            if (settingModel.getBeaconType() == Declaration.enmBeaconType.enm_BT_EDDYSTONE){
                // Eddystone namespace
                ContentValues valuesNamespace = new ContentValues();
                valuesNamespace.put(COL_KEY_NAME, KEY_EDDYSTONE_NAMESPACE);
                valuesNamespace.put(COL_TEXT_VALUE, settingModel.getEddystoneNamespace());
                resultTestValue *= insertOrUpdateSettingsColumn(valuesNamespace, KEY_EDDYSTONE_NAMESPACE);

                // Instance start
                ContentValues valuesInstanceStart = new ContentValues();
                valuesInstanceStart.put(COL_KEY_NAME, KEY_EDDYSTONE_INSTANCE_ID_START);
                valuesInstanceStart.put(COL_TEXT_VALUE, settingModel.getEddystoneInstanceStart());
                resultTestValue *= insertOrUpdateSettingsColumn(valuesInstanceStart, KEY_EDDYSTONE_INSTANCE_ID_START);

                // Instance end
                ContentValues valuesInstanceEnd = new ContentValues();
                valuesInstanceEnd.put(COL_KEY_NAME, KEY_EDDYSTONE_INSTANCE_ID_END);
                valuesInstanceEnd.put(COL_TEXT_VALUE, settingModel.getEddystoneInstanceEnd());
                resultTestValue *= insertOrUpdateSettingsColumn(valuesInstanceEnd, KEY_EDDYSTONE_INSTANCE_ID_END);
            } else if (settingModel.getBeaconType() == Declaration.enmBeaconType.enm_BT_IBEACON){
                // iBeacon UUID
                ContentValues valuesUUId = new ContentValues();
                valuesUUId.put(COL_KEY_NAME, KEY_IBEACON_UUID);
                valuesUUId.put(COL_TEXT_VALUE, settingModel.getiBeaconUUID());
                resultTestValue *= insertOrUpdateSettingsColumn(valuesUUId, KEY_IBEACON_UUID);

                // Major
                ContentValues valuesMajor = new ContentValues();
                valuesMajor.put(COL_KEY_NAME, KEY_IBEACON_MAJOR);
                valuesMajor.put(COL_INTEGER_VALUE, settingModel.getiBeaconMajor());
                resultTestValue *= insertOrUpdateSettingsColumn(valuesMajor, KEY_IBEACON_MAJOR);

                // Minor start
                ContentValues valuesMinorStart = new ContentValues();
                valuesMinorStart.put(COL_KEY_NAME, KEY_IBEACON_MINOR_START);
                valuesMinorStart.put(COL_INTEGER_VALUE, settingModel.getiBeaconMinorStart());
                resultTestValue *= insertOrUpdateSettingsColumn(valuesMinorStart, KEY_IBEACON_MINOR_START);

                // Minor end
                ContentValues valuesMinorEnd = new ContentValues();
                valuesMinorEnd.put(COL_KEY_NAME, KEY_IBEACON_MINOR_END);
                valuesMinorEnd.put(COL_INTEGER_VALUE, settingModel.getiBeaconMinorEnd());
                resultTestValue *= insertOrUpdateSettingsColumn(valuesMinorEnd, KEY_IBEACON_MINOR_END);
            }

            // Update period
            ContentValues valuesUpdatePeriod = new ContentValues();
            valuesUpdatePeriod.put(COL_KEY_NAME, KEY_UPDATE_PERIOD);
            valuesUpdatePeriod.put(COL_INTEGER_VALUE, settingModel.getUpdatePeriod());
            resultTestValue *= insertOrUpdateSettingsColumn(valuesUpdatePeriod, KEY_UPDATE_PERIOD);

            if (resultTestValue<0)
                result = false;

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

    public boolean InsertObjectToTable(String keyName, String textValue, Integer intValue){
        boolean result = true;
        OpenForWrite();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_KEY_NAME, keyName);
        if (textValue!=null){
            contentValues.put(COL_TEXT_VALUE, textValue);
        } else if (intValue!=null){
            contentValues.put(COL_INTEGER_VALUE, intValue);
        }

        long id = insertOrUpdateSettingsColumn(contentValues, keyName);
        if (id<0)
            result = false;

        CloseConnection();
        return result;
    }

    private long insertOrUpdateSettingsColumn(ContentValues contentValues, String keyName){
        long id = currentDB.insert(TABLE_SETTINGS, null, contentValues);
        if (id==-1) {
            String whereClause = String.format(Locale.US, "%s='%s'", COL_KEY_NAME, RemoveIllegalCharacterForSQLite(keyName));
            id = currentDB.update(TABLE_SETTINGS, contentValues, whereClause, null);
        }

        return id;
    }

    @Override
    public boolean DeleteById(int id) {
        return false;
    }
}
