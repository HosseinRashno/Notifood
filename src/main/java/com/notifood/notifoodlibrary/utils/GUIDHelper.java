package com.notifood.notifoodlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.notifood.notifoodlibrary.R;

import java.util.UUID;

import static com.notifood.notifoodlibrary.utils.Declaration.KEY_GUID;
import static com.notifood.notifoodlibrary.utils.LibPreferences.getStringPref;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveStringObject;

/**
 * Created by mrashno on 10/9/2017.
 */

public class GUIDHelper {
    public static final String GUID_RECEIVER_ACTION_NOTIFY_ME = "com.notifood.notifoodlibrary.guidreceiver.notifyme";
    public static final String GUID_RECEIVER_ACTION_SAVE_GUID = "com.notifood.notifoodlibrary.guidreceiver.saveguid";
    public static final String GUID_EXTRA_PARAM = "GUIDExtra";

    public void checkGUId(final Context context){
        String GUID = null;

        GUID = getStringPref(KEY_GUID);
        if (GUID==null || GUID.equals("")){
            // Ask other apps if they have GUID than pass it to you
            final Intent intentNotify = new Intent();
            intentNotify.setAction(GUID_RECEIVER_ACTION_NOTIFY_ME);
            context.sendBroadcast(intentNotify);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String receivedGUID = getStringPref(KEY_GUID);
                    if (receivedGUID==null || receivedGUID.equals("")){
                        // No apps have GUID, so this app is in charge for create GUID and broadcast it
                        receivedGUID = UUID.randomUUID().toString();
                        Intent intentSaveIt = new Intent();
                        intentSaveIt.setAction(GUID_RECEIVER_ACTION_SAVE_GUID);
                        intentSaveIt.putExtra(GUID_EXTRA_PARAM, receivedGUID);
                        context.sendBroadcast(intentSaveIt);
                    }
                }
            }, context.getResources().getInteger(R.integer.int_delay_to_check_apps_answers));
        }
    }
}
