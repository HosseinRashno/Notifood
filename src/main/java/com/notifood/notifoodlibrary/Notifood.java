package com.notifood.notifoodlibrary;

import android.content.Context;

import com.notifood.notifoodlibrary.database.DatabaseFactory;
import com.notifood.notifoodlibrary.database.SettingsTBL;
import com.notifood.notifoodlibrary.utils.Declaration;
import com.notifood.notifoodlibrary.utils.Utility;

import static com.notifood.notifoodlibrary.database.DBHelper.KEY_DEV_KEY;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_IS_ENABLED;
import static com.notifood.notifoodlibrary.database.DBHelper.KEY_PACKAGE_NAME;

/**
 * Created by mrashno on 10/4/2017.
 */

public class Notifood {

    // TODO : You should create a service to get new data in updatePeriod period
    // TODO : You should add enough log in project to developers know what happening
    // TODO : Add enough comment to everyone can easily understand what this library doing

    public boolean setDevKey(String devKey){
        SettingsTBL dbClass = (SettingsTBL)new DatabaseFactory().getTable(Declaration.enmTables.enm_T_SETTINGS);
        boolean result = dbClass.InsertObjectToTable(KEY_DEV_KEY, devKey, null);
        return result;
    }

    public void initialize(Context context){
        String packageName = context.getPackageName();
        SettingsTBL dbClass = (SettingsTBL)new DatabaseFactory().getTable(Declaration.enmTables.enm_T_SETTINGS);
        boolean result = dbClass.InsertObjectToTable(KEY_PACKAGE_NAME, packageName, null);

        // TODO : Get or create GUID
        // TODO : After finalize the GUID process, You should call webservice to receive other setting data
        // TODO : After above step, You should start beacon detection process
    }

    public void enableNotifood(){
        SettingsTBL dbClass = (SettingsTBL)new DatabaseFactory().getTable(Declaration.enmTables.enm_T_SETTINGS);
        dbClass.InsertObjectToTable(KEY_IS_ENABLED, null, 1);

        // TODO : Start beacon detection
    }

    public void disableNotifood(){
        SettingsTBL dbClass = (SettingsTBL)new DatabaseFactory().getTable(Declaration.enmTables.enm_T_SETTINGS);
        dbClass.InsertObjectToTable(KEY_IS_ENABLED, null, 0);

        // TODO : Stop beacon detection
    }
}
