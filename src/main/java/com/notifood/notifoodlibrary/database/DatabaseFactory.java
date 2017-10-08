package com.notifood.notifoodlibrary.database;

import com.notifood.notifoodlibrary.utils.Declaration;

/**
 * Created by mrashno on 10/4/2017.
 */

public class DatabaseFactory {
    public DatabaseClasses getTable(Declaration.enmTables enm){
        DatabaseClasses result= null;
        switch (enm) {
            case enm_T_RESTAURANT:
                result = new RestaurantTBL();
                break;
        }
        return result;
    }
}
