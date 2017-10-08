package com.notifood.notifoodlibrary;

import android.content.Context;

import com.notifood.notifoodlibrary.database.DatabaseFactory;
import com.notifood.notifoodlibrary.database.RestaurantTBL;
import com.notifood.notifoodlibrary.models.ServiceModel.RequestModel;
import com.notifood.notifoodlibrary.models.ServiceModel.ResponseModel;
import com.notifood.notifoodlibrary.models.SettingsObjectModel;
import com.notifood.notifoodlibrary.services.SettingsAndRestaurantsService;
import com.notifood.notifoodlibrary.utils.Declaration;

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

    public void setDevKey(String devKey){
        saveStringObject(KEY_DEV_KEY, devKey);
    }

    public void initialize(Context context){
        String packageName = context.getPackageName();
        saveStringObject(KEY_PACKAGE_NAME, packageName);

        // TODO : Get or create GUID
        String GUID = UUID.randomUUID().toString();

        // Get development key
        String devKey = getStringPref(KEY_DEV_KEY);


        // TODO : if we already have the data than Start service for get new update with schedule, Else do this after call the service

        // TODO : After finalize the GUID process, You should call webservice to receive other setting data
        RequestModel requestModel = new RequestModel();
        requestModel.setGUID(GUID);
        requestModel.setPackageName(packageName);
        requestModel.setDevKey(devKey);
        SettingsAndRestaurantsService service = new SettingsAndRestaurantsService(requestModel, delegate, context);
        service.execute();


        // TODO : After above step, You should start beacon detection process
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
}
