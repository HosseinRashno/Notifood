package com.notifood.notifoodlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;

import com.notifood.notifoodlibrary.Receivers.GUIDReceiver;

import java.util.UUID;

import static com.notifood.notifoodlibrary.utils.Declaration.KEY_GUID;
import static com.notifood.notifoodlibrary.utils.LibPreferences.getStringPref;
import static com.notifood.notifoodlibrary.utils.LibPreferences.saveStringObject;

/**
 * Created by mrashno on 10/4/2017.
 */

public class Utility {
    public static String removeIllegalCharacterForSQLite(String input){
        if (input == null)
            return "";
        String result = "";
        result = DatabaseUtils.sqlEscapeString(input);
        result = result.substring(1, result.length()-1);
        return result;
    }

}
