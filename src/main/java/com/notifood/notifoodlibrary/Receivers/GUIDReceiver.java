package com.notifood.notifoodlibrary.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.notifood.notifoodlibrary.utils.Declaration.KEY_GUID;
import static com.notifood.notifoodlibrary.utils.GUIDHelper.GUID_EXTRA_PARAM;
import static com.notifood.notifoodlibrary.utils.GUIDHelper.GUID_RECEIVER_ACTION_NOTIFY_ME;
import static com.notifood.notifoodlibrary.utils.GUIDHelper.GUID_RECEIVER_ACTION_SAVE_GUID;
import static com.notifood.notifoodlibrary.utils.LibPreferences.getStringPref;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveStringObject;

/**
 * Created by mrashno on 10/9/2017.
 */

public class GUIDReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // If this app already have GUID, then send it with broadcast to other apps
        if (intent.getAction().equals(GUID_RECEIVER_ACTION_NOTIFY_ME)){
            String GUID = null;
            GUID = getStringPref(KEY_GUID);
            if (GUID!=null && !GUID.equals("")){
                Intent intentSave = new Intent();
                intentSave.setAction(GUID_RECEIVER_ACTION_SAVE_GUID);
                intentSave.putExtra(GUID_EXTRA_PARAM, GUID);
                context.sendBroadcast(intentSave);
            }
        }

        // Some app create the GUID, this app only need to save it
        if (intent.getAction().equals(GUID_RECEIVER_ACTION_SAVE_GUID)){
            String GUID = intent.getExtras().getString(GUID_EXTRA_PARAM);
            saveStringObject(KEY_GUID, GUID);
        }
    }
}
