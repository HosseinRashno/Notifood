package com.notifood.notifoodlibrary.utils;

import android.Manifest;
import android.os.Environment;

import com.notifood.notifoodlibrary.ApplicationClass;
import com.notifood.notifoodlibrary.R;
import com.notifood.notifoodlibrary.database.DatabaseFactory;
import com.notifood.notifoodlibrary.database.RestaurantTBL;
import com.notifood.notifoodlibrary.models.RestaurantModel;
import com.notifood.notifoodlibrary.models.ServiceModel.RequestModel;
import com.notifood.notifoodlibrary.models.ServiceModel.ResponseModel;
import com.notifood.notifoodlibrary.services.SettingsAndRestaurantsService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.notifood.notifoodlibrary.utils.Declaration.KEY_DEV_KEY;

/**
 * Created by mrashno on 1/25/2018.
 */

public class HandleServiceCall {
    updateDelegate callerDelegate;

    String directoryPath;
    String personFileName;

    String packageName;
    String devKey;
    String personId;



    public interface updateDelegate{
        public void updateCompletionResult();
    }

    public void GetNewValues(updateDelegate callerDelegate){
        this.callerDelegate = callerDelegate;
        new Thread(new Runnable() {
            @Override
            public void run() {
                directoryPath = Environment.getExternalStorageDirectory() + ApplicationClass.getAppContext().getString(R.string.app_folder_name);
                personFileName = ApplicationClass.getAppContext().getString(R.string.app_person_id_file);

                packageName = LibPreferences.getStringPref(Declaration.KEY_PACKAGE_NAME);
                devKey = LibPreferences.getStringPref(KEY_DEV_KEY);
                personId = null;

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

                            personId = text.toString().replaceAll("[^\\d.]", "");
                        }
                        catch (IOException e) {
                            Utility.NotifoodLog("Can't read person Id, Reason:" + e.toString());
                        }
                    }
                }
                // endregion

                String logPerson = "";
                if (personId!=null)
                    logPerson = personId.toString();
                Utility.NotifoodLog(String.format("call service:%s|%s|%s", packageName, devKey, logPerson));

                RequestModel requestModel = new RequestModel();
                requestModel.setPackageName(packageName);
                requestModel.setDeveloperKey(devKey);
                requestModel.setPersonId(personId);

                SettingsAndRestaurantsService service = new SettingsAndRestaurantsService(requestModel, delegate, ApplicationClass.getAppContext());
                service.execute();
            }
        }).start();
    }

    SettingsAndRestaurantsService.ServiceDelegate delegate = new SettingsAndRestaurantsService.ServiceDelegate() {
        @Override
        public void serviceCompletionResult(final ResponseModel response) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (response==null){
                        Utility.NotifoodLog("Service response is null");
                    } else if (response.getErrorCode()!=null){
                        Utility.NotifoodLog("Service ErrorCode:"+response.getErrorCode().toString());
                    } else {
                        if (response.getSetting()!=null){
                            // region save person id in folder
                            try{
                                File folder = new File(directoryPath);
                                if(!folder.isDirectory())
                                    folder.mkdir();
                                File file = new File(folder, personFileName);
                                if(!file.exists())
                                    file.createNewFile();
                                FileWriter writer = new FileWriter(file);
                                writer.append(response.getSetting().getPersonId());
                                writer.flush();
                                writer.close();
                            } catch (Exception ex){
                                Utility.NotifoodLog("Can't save person id, reason:"+ ex.toString());
                            }
                            // endregion



                            LibPreferences.saveSerializable(Declaration.KEY_SETTINGS, response.getSetting());
                        }

                        if (response.getRestaurants().size()>0 ){
                            RestaurantTBL restaurantTBL = (RestaurantTBL) new DatabaseFactory().getTable(Declaration.enmTables.enm_T_RESTAURANT);
                            restaurantTBL.insertRestaurantData(response.getRestaurants());
                        }
                    }

                    if (callerDelegate!=null)
                        callerDelegate.updateCompletionResult();
                }
            }).start();
        }
    };
}
