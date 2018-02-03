package com.notifood.notifoodlibrary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.notifood.notifoodlibrary.models.SettingModel;
import com.notifood.notifoodlibrary.services.HandleDataUpdate;
import com.notifood.notifoodlibrary.utils.Declaration;
import com.notifood.notifoodlibrary.utils.HandleServiceCall;
import com.notifood.notifoodlibrary.utils.LibPreferences;
import com.notifood.notifoodlibrary.utils.Utility;

import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by mrashno on 10/4/2017.
 */

public class Notifood {

    // TODO : Add enough comment to everyone can easily understand what this library doing

    public void setDevKey(String devKey){
        LibPreferences.saveStringObject(Declaration.KEY_DEV_KEY, devKey);
        Utility.NotifoodLog(String.format(Locale.US, "DevKey is set to %s", devKey), Log.INFO);
    }

    public void initialize(Context context){
        Utility.NotifoodLog("Start to initialize the notifood library", Log.DEBUG);

        // Save application package name
        String packageName = context.getPackageName();

        LibPreferences.saveStringObject(Declaration.KEY_PACKAGE_NAME, packageName);

        String devKey = LibPreferences.getStringPref(Declaration.KEY_DEV_KEY);
        if (devKey==null || devKey.equals("")) {
            Utility.NotifoodLog("Notifood can't initialize without DevKey");
            return;
        }

        // Check if we already not scheduled a service, than create one
        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(context, HandleDataUpdate.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp)
        {
            long wakeUpMillis = ApplicationClass.getAppContext().getResources().getInteger(R.integer.int_first_service_call_in_seconds) * 1000;
            SettingModel settingModel = LibPreferences.getSerializable(Declaration.KEY_SETTINGS, SettingModel.class);
            if (settingModel!=null){
                wakeUpMillis = settingModel.getUpdatePeriod() * 60 * 60 * 1000;
            }

            AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, HandleDataUpdate.class);
            PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);
            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            wakeUpMillis, alarmIntent);

            Utility.NotifoodLog(String.format(Locale.US, "Alarm set for %d millis later", wakeUpMillis), Log.DEBUG);
        }
    }

    public void enableNotifood(){
        LibPreferences.saveCustomBoolObject(Declaration.KEY_IS_ENABLED, Declaration.enmCustomBoolCondition.enm_CBC_TRUE);
        ApplicationClass.getInstance().initializeBeaconDetection();
        Utility.NotifoodLog("Notifood enabled", Log.INFO);
    }

    public void disableNotifood(){
        LibPreferences.saveCustomBoolObject(Declaration.KEY_IS_ENABLED, Declaration.enmCustomBoolCondition.enm_CBC_FALSE);
        Utility.NotifoodLog("Notifood disabled", Log.INFO);
    }

    public void enableDebugMode(){
        LibPreferences.saveCustomBoolObject(Declaration.KEY_IS_DEBUG_ENABLED, Declaration.enmCustomBoolCondition.enm_CBC_TRUE);
        ApplicationClass.setIsDebugMode(Declaration.enmCustomBoolCondition.enm_CBC_TRUE);
        Utility.NotifoodLog("Notifood enabled", Log.INFO);
    }

    public void disableDebugMode(){
        LibPreferences.saveCustomBoolObject(Declaration.KEY_IS_DEBUG_ENABLED, Declaration.enmCustomBoolCondition.enm_CBC_FALSE);
        ApplicationClass.setIsDebugMode(Declaration.enmCustomBoolCondition.enm_CBC_FALSE);
        Utility.NotifoodLog("Notifood disabled", Log.INFO);
    }
}
