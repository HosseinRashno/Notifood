package com.notifood.notifoodlibrary;

import android.app.Application;
import android.content.Context;

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

    private static Context appContext;
    public static Context getAppContext() {
        return appContext;
    }
    public static void setAppContext(Context appContext) {
        ApplicationClass.appContext = appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this.getApplicationContext();

//        if (getIntegerPref(KEY_BEACON_TYPE)!= 0){
//            initializeBeaconDetection();
//        }
        // TODO : Check if enable
    }

    public void initializeBeaconDetection(){
//        beaconDetectedTable = new Hashtable<>();
//
//        mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
//        mBeaconManager.setBackgroundBetweenScanPeriod(getAppContext().getResources().getInteger(R.integer.int_beacon_delay_between_scan));
//        mBeaconManager.getBeaconParsers().clear();
//        mBeaconManager.getBeaconParsers().add(new BeaconParser().
//                setBeaconLayout(getAppContext().getString(R.string.str_beacon_ibeacon_scheme)));
//        Identifier uuid = Identifier.parse(getAppContext().getString(R.string.str_beacon_uuid));
//        Identifier majorId = Identifier.parse(getAppContext().getString(R.string.str_beacon_major));
//        String[] minors = getResources().getStringArray(R.array.str_beacon_minors);
//        ArrayList<Region> regions = new ArrayList<>();
//        for (String minor:minors){
//            Identifier minorId = Identifier.parse(minor);
//            Region region = new Region("hacoupian-region-"+minor, uuid, majorId, minorId);
//            regions.add(region);
//        }
//        regionBootstrap = new RegionBootstrap(this, regions);
//
//        backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    @Override
    public void didEnterRegion(Region region) {
        if (region.getId3()!=null)
            BeaconDetected(region.getId3().toString());
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        switch (i){
            case MonitorNotifier.INSIDE:
                if (region.getId3()!=null)
                    BeaconDetected(region.getId3().toString());
                break;
            case MonitorNotifier.OUTSIDE:
                break;
        }
    }

    private void BeaconDetected(String minor){
        // TODO : Notify other apps
//        if (minor.equals(""))
//            return;
//
//        if (beaconDetectedTable.containsKey(minor)){
//            Long lastElapsedTime = beaconDetectedTable.get(minor);
//            int time_between_notif = getAppContext().getResources().getInteger(R.integer.int_beacon_time_between_notifications);
//            if (elapsedRealtime() > (lastElapsedTime+time_between_notif)){
//                beaconDetectedTable.put(minor, elapsedRealtime());
//                ShowWelcomeNotification(minor);
//            }
//        } else {
//            beaconDetectedTable.put(minor, elapsedRealtime());
//            ShowWelcomeNotification(minor);
//        }
    }
}