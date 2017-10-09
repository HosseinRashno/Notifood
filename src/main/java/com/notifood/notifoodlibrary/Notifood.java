package com.notifood.notifoodlibrary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.notifood.notifoodlibrary.Receivers.ServiceScheduleReceiver;
import com.notifood.notifoodlibrary.database.DatabaseFactory;
import com.notifood.notifoodlibrary.database.RestaurantTBL;
import com.notifood.notifoodlibrary.models.ServiceModel.RequestModel;
import com.notifood.notifoodlibrary.models.ServiceModel.ResponseModel;
import com.notifood.notifoodlibrary.models.SettingsObjectModel;
import com.notifood.notifoodlibrary.services.SettingsAndRestaurantsService;
import com.notifood.notifoodlibrary.utils.Declaration;
import com.notifood.notifoodlibrary.utils.GUIDHelper;

import java.util.UUID;

import static android.content.Context.ALARM_SERVICE;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_DEV_KEY;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_IS_DEBUG_ENABLED;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_IS_ENABLED;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_PACKAGE_NAME;
import static com.notifood.notifoodlibrary.utils.LibPreferences.getStringPref;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveBooleanObject;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveCustomBoolObject;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveSettingObject;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveStringObject;

/**
 * Created by mrashno on 10/4/2017.
 */

public class Notifood {

    // TODO : You should add enough log in project to developers know what happening
    // TODO : Add enough comment to everyone can easily understand what this library doing
    // TODO : Create test cases

    public void setDevKey(String devKey){
        saveStringObject(KEY_DEV_KEY, devKey);
    }

    public void initialize(Context context){
        // Save application package name
        String packageName = context.getPackageName();
        saveStringObject(KEY_PACKAGE_NAME, packageName);

        // Do checking flow for GUID
        GUIDHelper guidHelper = new GUIDHelper();
        guidHelper.checkGUId(context);

        // schedule first call of service
        Intent intent = new Intent(context, ServiceScheduleReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 100, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        long timeToWakeup = SystemClock.elapsedRealtime()+(context.getResources().getInteger(R.integer.int_first_service_call_in_seconds)*1000);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                timeToWakeup
                , pendingIntent);
    }

    public void enableNotifood(){
        saveCustomBoolObject(KEY_IS_ENABLED, Declaration.enmCustomBoolCondition.enm_CBC_TRUE);

        // TODO : Start beacon detection
    }

    public void disableNotifood(){
        saveCustomBoolObject(KEY_IS_ENABLED, Declaration.enmCustomBoolCondition.enm_CBC_FALSE);

        // TODO : Stop beacon detection
    }

    public void enableDebugMode(){
        saveCustomBoolObject(KEY_IS_DEBUG_ENABLED, Declaration.enmCustomBoolCondition.enm_CBC_TRUE);
    }

    public void disableDebugMode(){
        saveCustomBoolObject(KEY_IS_DEBUG_ENABLED, Declaration.enmCustomBoolCondition.enm_CBC_FALSE);
    }
}
