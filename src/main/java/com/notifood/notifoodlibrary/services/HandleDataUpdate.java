package com.notifood.notifoodlibrary.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;

import com.notifood.notifoodlibrary.ApplicationClass;
import com.notifood.notifoodlibrary.models.SettingModel;
import com.notifood.notifoodlibrary.utils.Declaration;
import com.notifood.notifoodlibrary.utils.HandleServiceCall;
import com.notifood.notifoodlibrary.utils.LibPreferences;

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

            HandleServiceCall handleServiceCall = new HandleServiceCall();
            handleServiceCall.GetNewValues(delegate);
        }

        HandleServiceCall.updateDelegate delegate = new HandleServiceCall.updateDelegate() {
            @Override
            public void updateCompletionResult() {
                long wakeUpMillis = 60 * 1000;
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

                stopSelf(msg.arg1);
            }
        };
    }

    @Override
    public void onCreate() {
        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }
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
