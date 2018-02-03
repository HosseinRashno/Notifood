package com.notifood.notifoodlibrary.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import com.notifood.notifoodlibrary.ApplicationClass;
import com.notifood.notifoodlibrary.R;
import com.notifood.notifoodlibrary.models.SettingModel;
import com.notifood.notifoodlibrary.utils.Declaration;
import com.notifood.notifoodlibrary.utils.HandleServiceCall;
import com.notifood.notifoodlibrary.utils.LibPreferences;
import com.notifood.notifoodlibrary.utils.Utility;

import java.io.File;
import java.util.Locale;

/**
 * Created by mrashno on 1/31/2018.
 */

public class HandleDataUpdate extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {
        Message msg;
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            this.msg = msg;

            Utility.NotifoodLog("Service is in handleMessage method", Log.DEBUG);

            HandleServiceCall handleServiceCall = new HandleServiceCall();
            handleServiceCall.GetNewValues(delegate);
        }

        HandleServiceCall.updateDelegate delegate = new HandleServiceCall.updateDelegate() {
            @Override
            public void updateCompletionResult() {
                long wakeUpMillis = getResources().getInteger(R.integer.int_default_service_call_in_hours) * 60 * 60 * 1000;
                SettingModel settingModel = LibPreferences.getSerializable(Declaration.KEY_SETTINGS, SettingModel.class);
                if (settingModel!=null){
                    wakeUpMillis = settingModel.getUpdatePeriod() * 60 * 60 * 1000;
                }

                AlarmManager alarmMgr = (AlarmManager)ApplicationClass.getAppContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(ApplicationClass.getAppContext(), HandleDataUpdate.class);
                PendingIntent alarmIntent = PendingIntent.getService(ApplicationClass.getAppContext(), 0, intent, 0);
                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() +
                                wakeUpMillis, alarmIntent);

                Utility.NotifoodLog(String.format(Locale.US, "Alarm set for %d millis later", wakeUpMillis), Log.DEBUG);

                // Delete all files related to detected beacon
                try{
                    if (Utility.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        String directoryPath = Environment.getExternalStorageDirectory() + ApplicationClass.getAppContext().getString(R.string.app_folder_name);
                        File folder = new File(directoryPath);
                        File[] files = folder.listFiles();

                        String personFileName = getString(R.string.app_person_id_file);

                        for (File file: files){
                            if (file.getName().equals(personFileName)==false){
                                file.delete();
                            }
                        }
                        Utility.NotifoodLog("Directory cleaned successfully.", Log.INFO);
                    }
                } catch (Exception ex){
                    Utility.NotifoodLog("Can't delete some beacon files, reason:"+ex.toString());
                }

                ((ApplicationClass)getApplication()).initializeBeaconDetection();
                stopSelf(msg.arg1);
            }
        };
    }

    @Override
    public void onCreate() {
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        Utility.NotifoodLog("Update service is running!", Log.DEBUG);

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
