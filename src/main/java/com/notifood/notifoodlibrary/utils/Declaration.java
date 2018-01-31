package com.notifood.notifoodlibrary.utils;

/**
 * Created by mrashno on 10/4/2017.
 */

public class Declaration {
    public static final String TAG = "Notifood";
    public static final String SHAREDPREFERENCES_NAME = "notifood_settings";

    // region shared preferences keys
    public static final String KEY_SETTINGS = "settingsKey";
    public static final String KEY_IS_ENABLED = "isEnabled";
    public static final String KEY_IS_DEBUG_ENABLED = "isDebugEnabled";
    public static final String KEY_PACKAGE_NAME = "packageName";
    public static final String KEY_GUID = "GUID";
    public static final String KEY_DEV_KEY = "devKey";
    // endregion

    public enum enmBeaconType {
        enm_BT_EDDYSTONE(1), enm_BT_IBEACON(2);

        private int code;

        enmBeaconType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public enum enmTables {

        enm_T_RESTAURANT(0);

        private int code;

        enmTables(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static enmTables fromString(String str) {
            if (str.equals("enm_T_RESTAURANT"))
                return enm_T_RESTAURANT;
            return null;
        }
    }

    public enum enmCustomBoolCondition {
        enm_CBC_FALSE(0), enm_CBC_TRUE(1), enm_CBC_DEFAULT(2);

        private int code;

        enmCustomBoolCondition(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public enum enmShamsiDays {
        enmDef(0), enmShanbe(1), enm1Shanbe(2), enm2Shanbe(3), enm3Shanbe(4), enm4Shanbe(5), enm5Shanbe(6), enmJome(7), enmHolidays(8), enmAllDays(9);
        private int code;

        enmShamsiDays(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
