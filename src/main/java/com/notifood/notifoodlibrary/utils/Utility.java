package com.notifood.notifoodlibrary.utils;

import android.content.pm.PackageManager;
import android.database.DatabaseUtils;
import android.util.Log;

import com.notifood.notifoodlibrary.ApplicationClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
        NotifoodLog(message, Log.ERROR);
    }

    public static void NotifoodLog(String message, int priority){
        Declaration.enmCustomBoolCondition isDebugModeEnabled = ApplicationClass.getIsDebugMode();
        if (isDebugModeEnabled== Declaration.enmCustomBoolCondition.enm_CBC_DEFAULT || isDebugModeEnabled== Declaration.enmCustomBoolCondition.enm_CBC_TRUE){
            switch (priority){
                case Log.VERBOSE:
                    Log.v(Declaration.TAG, message);
                    break;
                case Log.DEBUG:
                    Log.d(Declaration.TAG, message);
                    break;
                case Log.INFO:
                    Log.i(Declaration.TAG, message);
                    break;
                case Log.WARN:
                    Log.w(Declaration.TAG, message);
                    break;
                case Log.ERROR:
                    Log.e(Declaration.TAG, message);
                    break;
            }
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

    public static boolean checkPermission(String permission)
    {
        int res = ApplicationClass.getAppContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static String readNumericContentOfFile(String directoryPath, String personFileName){
        String fileContent = null;

        // region read persn id from file
        File folder = new File(directoryPath);
        if(folder.isDirectory()) {
            File file = new File(directoryPath, personFileName);
            if(file.exists()) {
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();

                    fileContent = text.toString().replaceAll("[^\\d.]", "");
                }
                catch (IOException e) {
                    Utility.NotifoodLog("Can't read person Id, Reason:" + e.toString());
                }
            }
        }
        // endregion

        return fileContent;
    }
}
