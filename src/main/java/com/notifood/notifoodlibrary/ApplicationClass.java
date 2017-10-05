package com.notifood.notifoodlibrary;

import android.app.Application;
import android.content.Context;

/**
 * Created by mrashno on 10/4/2017.
 */

public class ApplicationClass extends Application {

    private static Context appContext;
    public static Context getAppContext() {
        return appContext;
    }
    public static void setAppContext(Context appContext) {
        ApplicationClass.appContext = appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this.getApplicationContext();
    }
}
