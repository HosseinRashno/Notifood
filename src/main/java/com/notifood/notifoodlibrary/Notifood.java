package com.notifood.notifoodlibrary;

import android.content.Context;

import com.notifood.notifoodlibrary.database.DatabaseFactory;
import com.notifood.notifoodlibrary.database.RestaurantTBL;
import com.notifood.notifoodlibrary.models.ServiceModel.RequestModel;
import com.notifood.notifoodlibrary.models.ServiceModel.ResponseModel;
import com.notifood.notifoodlibrary.models.SettingsObjectModel;
import com.notifood.notifoodlibrary.services.SettingsAndRestaurantsService;
import com.notifood.notifoodlibrary.utils.Declaration;
import com.notifood.notifoodlibrary.utils.GUIDHelper;

import java.util.UUID;

import static com.notifood.notifoodlibrary.utils.Declaration.KEY_DEV_KEY;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_IS_ENABLED;
import static com.notifood.notifoodlibrary.utils.Declaration.KEY_PACKAGE_NAME;
import static com.notifood.notifoodlibrary.utils.LibPreferences.getStringPref;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveBooleanObject;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveSettingObject;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveStringObject;

/**
 * Created by mrashno on 10/4/2017.
 */

public class Notifood {

    // TODO : You should create a service to get new data in updatePeriod period
    // TODO : You should add enough log in project to developers know what happening
    // TODO : Add enough comment to everyone can easily understand what this library doing
    // TODO : Create test cases
    // TODO : Check if your content provider work well, and check if user need to add anything to manifest
    // TODO : Check if content provider need permissions

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

        // TODO : Create a service for first call and then call itself by schedule
    }

    private SettingsAndRestaurantsService.ServiceDelegate delegate = new SettingsAndRestaurantsService.ServiceDelegate() {
        @Override
        public void serviceCompletionResult(ResponseModel response) {
            if (response != null){
                // Save received data from service
                saveSettingObject(response.getSetting());
                RestaurantTBL dbClass = (RestaurantTBL)new DatabaseFactory().getTable(Declaration.enmTables.enm_T_RESTAURANT);
                dbClass.InsertToTable(response.getRestaurants());
            }
        }
    };

    public void enableNotifood(){
        saveBooleanObject(KEY_IS_ENABLED, true);

        // TODO : Start beacon detection
    }

    public void disableNotifood(){
        saveBooleanObject(KEY_IS_ENABLED, false);

        // TODO : Stop beacon detection
    }

    public void enableDebugMode(){
        // TODO : If this method called then log everything, Else do not log at all
    }

    public void disableDebugMode(){
        // TODO : If this method called then do not log at all, Else log everything
    }
}
