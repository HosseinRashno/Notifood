package com.notifood.notifoodlibrary.utils;

/**
 * Created by mrashno on 10/4/2017.
 */

public class Declaration {
    public static final String TAG = "Notifood";

    public enum enmBeaconType {
        enm_BT_EDDYSTONE(0), enm_BT_IBEACON(1);

        private int code;

        enmBeaconType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public enum enmTables {

        enm_T_SETTINGS(0), enm_T_RESTAURANT(1);

        private int code;

        enmTables(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static enmTables fromString(String str) {
            if (str.equals("enm_T_SETTINGS"))
                return enm_T_SETTINGS;
            else if (str.equals("enm_T_RESTAURANT"))
                return enm_T_RESTAURANT;
            return null;
        }
    }
}
