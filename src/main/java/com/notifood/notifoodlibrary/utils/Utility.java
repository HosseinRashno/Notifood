package com.notifood.notifoodlibrary.utils;

import android.database.DatabaseUtils;
import android.util.Log;

import java.util.Calendar;

import static com.notifood.notifoodlibrary.utils.Declaration.KEY_IS_DEBUG_ENABLED;
import static com.notifood.notifoodlibrary.utils.LibPreferences.getCustomBoolPref;

/**
 * Created by mrashno on 10/4/2017.
 */

public class Utility {
    public static String removeIllegalCharacterForSQLite(String input){
        if (input == null)
            return "";
        String result = "";
        result = DatabaseUtils.sqlEscapeString(input);
        result = result.substring(1, result.length()-1);
        return result;
    }

    public static void NotifoodLog(String message){
        Declaration.enmCustomBoolCondition isDebugModeEnabled = getCustomBoolPref(KEY_IS_DEBUG_ENABLED);
        if (isDebugModeEnabled== Declaration.enmCustomBoolCondition.enm_CBC_DEFAULT || isDebugModeEnabled== Declaration.enmCustomBoolCondition.enm_CBC_TRUE){
            Log.e(Declaration.TAG, message);
        }
    }

    public static Declaration.enmShamsiDays mapGreDaysToShamsiDays(int greDay){
        Declaration.enmShamsiDays enm;
        switch (greDay){
            case Calendar.SATURDAY:
                enm = Declaration.enmShamsiDays.enmShanbe;
                break;
            case Calendar.SUNDAY:
                enm = Declaration.enmShamsiDays.enm1Shanbe;
                break;
            case Calendar.MONDAY:
                enm = Declaration.enmShamsiDays.enm2Shanbe;
                break;
            case Calendar.TUESDAY:
                enm = Declaration.enmShamsiDays.enm3Shanbe;
                break;
            case Calendar.WEDNESDAY:
                enm = Declaration.enmShamsiDays.enm4Shanbe;
                break;
            case Calendar.THURSDAY:
                enm = Declaration.enmShamsiDays.enm5Shanbe;
                break;
            case Calendar.FRIDAY:
                enm = Declaration.enmShamsiDays.enmJome;
                break;
                default:
                    enm = Declaration.enmShamsiDays.enmDef;
        }

        return enm;
    }
}
