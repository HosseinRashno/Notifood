package com.notifood.notifoodlibrary.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.notifood.notifoodlibrary.R;
import com.notifood.notifoodlibrary.database.DatabaseFactory;
import com.notifood.notifoodlibrary.database.RestaurantTBL;
import com.notifood.notifoodlibrary.models.ServiceModel.RequestModel;
import com.notifood.notifoodlibrary.models.ServiceModel.ResponseModel;
import com.notifood.notifoodlibrary.services.SettingsAndRestaurantsService;
import com.notifood.notifoodlibrary.utils.Declaration;

import static android.content.Context.ALARM_SERVICE;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_DEV_KEY;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_GUID;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_PACKAGE_NAME;
import static com.notifood.notifoodlibrary.utils.LibPreferences.getStringPref;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveSettingObject;

/**
 * Created by mrashno on 10/9/2017.
 */

public class ServiceScheduleReceiver extends BroadcastReceiver {

    PendingResult pendingResult = null;
    Context contextOfBroadcast;

    @Override
    public void onReceive(Context context, Intent intent) {
        pendingResult = goAsync();
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        contextOfBroadcast = context;

        RequestModel requestModel = new RequestModel();
        requestModel.setGUID(getStringPref(KEY_GUID));
        requestModel.setPackageName(getStringPref(KEY_PACKAGE_NAME));
        requestModel.setDevKey(getStringPref(KEY_DEV_KEY));
        SettingsAndRestaurantsService service = new SettingsAndRestaurantsService(requestModel, delegate, context);
        service.execute();
    }

    SettingsAndRestaurantsService.ServiceDelegate delegate = new SettingsAndRestaurantsService.ServiceDelegate() {
        @Override
        public void serviceCompletionResult(ResponseModel response) {
            long nextCallTime = contextOfBroadcast.getResources().getInteger(R.integer.int_default_service_call_in_hours) * 60 * 60 * 1000;
            if (response != null){
                // Save received data from service
                saveSettingObject(response.getSetting());
                RestaurantTBL dbClass = (RestaurantTBL)new DatabaseFactory().getTable(Declaration.enmTables.enm_T_RESTAURANT);
                dbClass.InsertToTable(response.getRestaurants());

                nextCallTime = response.getSetting().getUpdatePeriod() * 60 * 60 * 1000;

                // TODO : If detection not start yet, than start it now
            }

            // schedule next call of service
            Intent intent = new Intent(contextOfBroadcast, ServiceScheduleReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    contextOfBroadcast, 100, intent, 0);
            AlarmManager alarmManager = (AlarmManager) contextOfBroadcast.getSystemService(ALARM_SERVICE);
            long timeToWakeup = SystemClock.elapsedRealtime()+(contextOfBroadcast.getResources().getInteger(R.integer.int_first_service_call_in_seconds)*1000);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    timeToWakeup
                    , pendingIntent);

            pendingResult.setResultCode(1);
            pendingResult.finish();
        }
    };
}
