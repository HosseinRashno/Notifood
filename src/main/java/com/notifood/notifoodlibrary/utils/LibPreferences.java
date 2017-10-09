package com.notifood.notifoodlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.notifood.notifoodlibrary.ApplicationClass;
import com.notifood.notifoodlibrary.models.SettingModel;

import static com.notifood.notifoodlibrary.utils.Declaration.KEY_BEACON_TYPE;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_EDDYSTONE_INSTANCE_ID_END;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_EDDYSTONE_INSTANCE_ID_START;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_EDDYSTONE_NAMESPACE;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_IBEACON_MAJOR;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_IBEACON_MINOR_END;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_IBEACON_MINOR_START;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_IBEACON_UUID;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_UPDATE_PERIOD;
import static com.notifood.notifoodlibrary.utils.Declaration.SHAREDPREFERENCES_NAME;

/**
 * Created by mrashno on 10/7/2017.
 */

public class LibPreferences {

    public static void saveSettingObject(SettingModel settingModel){
        SharedPreferences sharedPref = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(KEY_BEACON_TYPE, settingModel.getBeaconType().getCode());
        editor.putInt(KEY_UPDATE_PERIOD, settingModel.getUpdatePeriod());
        if (settingModel.getBeaconType() == Declaration.enmBeaconType.enm_BT_EDDYSTONE){
            editor.putString(KEY_EDDYSTONE_NAMESPACE, settingModel.getEddystoneNamespace());
            editor.putString(KEY_EDDYSTONE_INSTANCE_ID_START, settingModel.getEddystoneInstanceStart());
            editor.putString(KEY_EDDYSTONE_INSTANCE_ID_END, settingModel.getEddystoneInstanceEnd());
        } else if (settingModel.getBeaconType() == Declaration.enmBeaconType.enm_BT_IBEACON){
            editor.putString(KEY_IBEACON_UUID, settingModel.getiBeaconUUID());
            editor.putInt(KEY_IBEACON_MAJOR, settingModel.getiBeaconMajor());
            editor.putInt(KEY_IBEACON_MINOR_START, settingModel.getiBeaconMinorStart());
            editor.putInt(KEY_IBEACON_MINOR_END, settingModel.getiBeaconMinorEnd());
        }

        editor.commit();
    }

    public static void saveStringObject(String prefKey, String prefValue){
        SharedPreferences sharedPref = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(prefKey, prefValue);
        editor.commit();
    }

    public static void saveIntObject(String prefKey, int prefValue){
        SharedPreferences sharedPref = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(prefKey, prefValue);
        editor.commit();
    }

    public static void saveBooleanObject(String prefKey, boolean prefValue){
        SharedPreferences sharedPref = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putBoolean(prefKey, prefValue);
        editor.commit();
    }

    public static void saveCustomBoolObject(String prefKey, Declaration.enmCustomBoolCondition prefValue){
        SharedPreferences sharedPref = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(prefKey, prefValue.getCode());
        editor.commit();
    }

    public static String getStringPref(String prefKey){
        SharedPreferences sharedPreferences = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(prefKey, "");
        return result;
    }

    public static Integer getIntegerPref(String prefKey){
        SharedPreferences sharedPreferences = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Integer result = sharedPreferences.getInt(prefKey, 0);
        return result;
    }

    public static boolean getBooleanPref(String prefKey){
        SharedPreferences sharedPreferences = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean(prefKey, false);
        return result;
    }

    public static Declaration.enmCustomBoolCondition getCustomBoolPref(String prefKey){
        SharedPreferences sharedPreferences = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        int result = sharedPreferences.getInt(prefKey, Declaration.enmCustomBoolCondition.enm_CBC_DEFAULT.getCode());
        return Declaration.enmCustomBoolCondition.values()[result];
    }
}
