package com.notifood.notifoodlibrary.utils;

import android.database.DatabaseUtils;

/**
 * Created by mrashno on 10/4/2017.
 */

public class Utility {
    public static String RemoveIllegalCharacterForSQLite(String input){
        if (input == null)
            return "";
        String result = "";
        result = DatabaseUtils.sqlEscapeString(input);
        result = result.substring(1, result.length()-1);
        return result;
    }

}
