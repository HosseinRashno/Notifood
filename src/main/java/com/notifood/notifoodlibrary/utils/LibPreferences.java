package com.notifood.notifoodlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.notifood.notifoodlibrary.ApplicationClass;
import com.notifood.notifoodlibrary.models.SettingModel;

import java.lang.reflect.Type;

import static com.notifood.notifoodlibrary.utils.Declaration.SHAREDPREFERENCES_NAME;

/**
 * Created by mrashno on 10/7/2017.
 */

public class LibPreferences {
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

    public static void saveSerializable(String key, Object value){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String data = gson.toJson(value);

        saveStringObject(key, data);
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

    public static <T> T getSerializable(String key, Class<T> classOfT){
        return getSerializable(key, classOfT, null);
    }

    public static  <T> T getSerializable(String key, Type typeOfT){
        return getSerializable(key, null, typeOfT);
    }

    private static  <T> T getSerializable(String key, Class<T> classOfT, Type typeOfT){
        SharedPreferences sharedPref = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        if (sharedPref.contains(key)){
            String json = sharedPref.getString(key, "");

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            if (classOfT!=null)
                return gson.fromJson(json, classOfT);
            else
                return gson.fromJson(json, typeOfT);
        } else {
            return null;
        }
    }

    public static Declaration.enmCustomBoolCondition getCustomBoolPref(String prefKey){
        SharedPreferences sharedPreferences = ApplicationClass.getAppContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        int result = sharedPreferences.getInt(prefKey, Declaration.enmCustomBoolCondition.enm_CBC_DEFAULT.getCode());
        return Declaration.enmCustomBoolCondition.values()[result];
    }
}
