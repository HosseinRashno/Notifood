package com.notifood.notifoodlibrary;

import android.app.Application;
import android.content.Context;

import com.notifood.notifoodlibrary.models.SettingModel;
import com.notifood.notifoodlibrary.utils.Declaration;
import com.notifood.notifoodlibrary.utils.LibPreferences;
import com.notifood.notifoodlibrary.utils.Utility;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.Hashtable;

import static android.os.SystemClock.elapsedRealtime;
import static com.notifood.notifoodlibrary.utils.LibPreferences.getIntegerPref;

/**
 * Created by mrashno on 10/4/2017.
 */

public class ApplicationClass extends Application implements BootstrapNotifier {

    private ArrayList<RegionBootstrap> regionBootstraps;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager mBeaconManager;
    private RegionBootstrap regionBootstrap;
    private Hashtable<String, Long> beaconDetectedTable;
    private Declaration.enmBeaconType beaconType;

    private static Context appContext;
    public static Context getAppContext() {
        return appContext;
    }
    public static void setAppContext(Context appContext) {
        ApplicationClass.appContext = appContext;
    }

    private static ApplicationClass instance;
    public static ApplicationClass getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appContext = getApplicationContext();
    }

    public void initializeBeaconDetection(){
        Declaration.enmCustomBoolCondition isEnabled = LibPreferences.getCustomBoolPref(Declaration.KEY_IS_ENABLED);
        if (isEnabled==Declaration.enmCustomBoolCondition.enm_CBC_DEFAULT || isEnabled==Declaration.enmCustomBoolCondition.enm_CBC_TRUE){
            SettingModel settingModel = LibPreferences.getSerializable(Declaration.KEY_SETTINGS, SettingModel.class);

            if (mBeaconManager==null && settingModel!=null){
                beaconDetectedTable = new Hashtable<>();

                mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
                mBeaconManager.getBeaconParsers().clear();

                ArrayList<Region> regions = new ArrayList<>();

                beaconType = settingModel.getBeaconType();
                if (beaconType == Declaration.enmBeaconType.enm_BT_IBEACON){
                    mBeaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(getAppContext().getString(R.string.str_beacon_ibeacon_scheme)));
                    Identifier uuid = Identifier.parse(settingModel.getiBeaconUUID());
                    Identifier majorId = Identifier.parse(String.valueOf(settingModel.getiBeaconMajor()));

                    for (int i=settingModel.getiBeaconMinorStart(); i<settingModel.getiBeaconMinorEnd(); i++){
                        String minor = String.valueOf(i);
                        Identifier minorId = Identifier.parse(minor);
                        Region region = new Region("notifood-region-"+minor, uuid, majorId, minorId);
                        regions.add(region);
                    }
                } else if (beaconType == Declaration.enmBeaconType.enm_BT_EDDYSTONE){
                    mBeaconManager.getBeaconParsers().add(new BeaconParser().
                            setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
                    Identifier myBeaconNamespaceId = Identifier.parse(settingModel.getEddystoneNamespace());
                    String startInsnatceStr = settingModel.getEddystoneInstanceStart().substring(2, settingModel.getEddystoneInstanceStart().length());
                    long startInstance = Long.parseLong(startInsnatceStr, 16);
                    String endInsnatceStr = settingModel.getEddystoneInstanceEnd().substring(2, settingModel.getEddystoneInstanceEnd().length());
                    long endInstance = Long.parseLong(endInsnatceStr, 16);

                    for (long i=startInstance; i<endInstance; i++){
                        String instance = Long.toHexString(i);
                        instance = "000000000000".substring(instance.length()) + instance;
                        instance = "0x"+instance;
                        Identifier myBeaconInstanceId = Identifier.parse(instance);
                        Region region = new Region("notifood-region-"+instance, myBeaconNamespaceId, myBeaconInstanceId, null);
                        regions.add(region);
                    }
                }

                regionBootstrap = new RegionBootstrap(this, regions);
                backgroundPowerSaver = new BackgroundPowerSaver(this);
            }
        } else {
            Utility.NotifoodLog("Can't start detection, Beacon detection is disabled!");
        }
    }

    @Override
    public void didEnterRegion(Region region) {
        if (beaconType==Declaration.enmBeaconType.enm_BT_EDDYSTONE && region.getId2()!=null)
            BeaconDetected(region.getId2().toString());
        else if (beaconType==Declaration.enmBeaconType.enm_BT_IBEACON && region.getId3()!=null)
            BeaconDetected(region.getId3().toString());
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        switch (i){
            case MonitorNotifier.INSIDE:
                if (beaconType==Declaration.enmBeaconType.enm_BT_EDDYSTONE && region.getId2()!=null)
                    BeaconDetected(region.getId2().toString());
                else if (beaconType==Declaration.enmBeaconType.enm_BT_IBEACON && region.getId3()!=null)
                    BeaconDetected(region.getId3().toString());
                break;
            case MonitorNotifier.OUTSIDE:
                break;
        }
    }

    private void BeaconDetected(String minorOrInstance){
        // TODO : Notify other apps
        if (minorOrInstance.equals(""))
            return;

        Declaration.enmCustomBoolCondition isEnabled = LibPreferences.getCustomBoolPref(Declaration.KEY_IS_ENABLED);
        if (isEnabled==Declaration.enmCustomBoolCondition.enm_CBC_FALSE)
            return;

        if (beaconDetectedTable.containsKey(minorOrInstance)){
            Long lastElapsedTime = beaconDetectedTable.get(minorOrInstance);
            int time_between_notif = getAppContext().getResources().getInteger(R.integer.int_beacon_time_between_notifications);
            if (elapsedRealtime() > (lastElapsedTime+time_between_notif)){
                beaconDetectedTable.put(minorOrInstance, elapsedRealtime());
//                ShowWelcomeNotification(minor);
                String correct = "";
            }
        } else {
            beaconDetectedTable.put(minorOrInstance, elapsedRealtime());
//            ShowWelcomeNotification(minor);
            String correct = "";
        }
    }
}