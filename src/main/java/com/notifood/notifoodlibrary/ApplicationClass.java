package com.notifood.notifoodlibrary;

import android.Manifest;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Declaration.enmBeaconType getBeaconType() {
        if (beaconType==null){
            SettingModel settingModel = LibPreferences.getSerializable(Declaration.KEY_SETTINGS, SettingModel.class);
            beaconType = settingModel.getBeaconType();
        }
        return beaconType;
    }

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
        processDetectedBeacon("0x0000000000F9");
    }

    public void initializeBeaconDetection(){
        Declaration.enmCustomBoolCondition isEnabled = LibPreferences.getCustomBoolPref(Declaration.KEY_IS_ENABLED);
        if (isEnabled==Declaration.enmCustomBoolCondition.enm_CBC_FALSE){
            Utility.NotifoodLog("Can't start detection, Beacon detection is disabled!");
            return;
        }

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
    }

    @Override
    public void didEnterRegion(Region region) {
        if (getBeaconType()==Declaration.enmBeaconType.enm_BT_EDDYSTONE && region.getId2()!=null)
            BeaconDetected(region.getId2().toString());
        else if (getBeaconType()==Declaration.enmBeaconType.enm_BT_IBEACON && region.getId3()!=null)
            BeaconDetected(region.getId3().toString());
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        switch (i){
            case MonitorNotifier.INSIDE:
                if (getBeaconType()==Declaration.enmBeaconType.enm_BT_EDDYSTONE && region.getId2()!=null)
                    BeaconDetected(region.getId2().toString());
                else if (getBeaconType()==Declaration.enmBeaconType.enm_BT_IBEACON && region.getId3()!=null)
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
                processDetectedBeacon(minorOrInstance);
                String correct = "";
            }
        } else {
            beaconDetectedTable.put(minorOrInstance, elapsedRealtime());
            processDetectedBeacon(minorOrInstance);
            String correct = "";
        }
    }

    private void processDetectedBeacon(final String minorOrInstance){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long millis = 0;
                    int range = getResources().getInteger(R.integer.int_random_delay_to_show_message);
                    Random random = new Random();
                    millis = (long)(random.nextDouble()*(long)range);
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Utility.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Calendar cal = Calendar.getInstance();
                    int hours = cal.get(Calendar.HOUR_OF_DAY);
                    int minutes = cal.get(Calendar.MINUTE);
                    long minutesOfDay = hours*60 + minutes;

                    String beaconFileInitialName = "." + minorOrInstance;

                    String directoryPath = Environment.getExternalStorageDirectory() + ApplicationClass.getAppContext().getString(R.string.app_folder_name);
                    File folder = new File(directoryPath);
                    File[] files = folder.listFiles();
                    Pattern patternLR = Pattern.compile("lr(.*?)hr");
                    Pattern patternHR = Pattern.compile("(?<=hr).*");

                    boolean canShowMessage = true;
                    for (File file: files){
                        String name = file.getName();
                        if (name.startsWith(beaconFileInitialName)){
                            Matcher matcherLR = patternLR.matcher(name);
                            Matcher matcherHR = patternHR.matcher(name);
                            if (matcherLR.find() && matcherHR.find())
                            {
                                int lr = Integer.valueOf(matcherLR.group(1));
                                int hr = Integer.valueOf(matcherHR.group(0));
                                if (minutesOfDay>=lr && minutesOfDay<=hr){
                                    canShowMessage = false;
                                    Utility.NotifoodLog("Can't show notification because some other apps already show the notification.\nBut you will still earn from this notification!", Log.WARN);
                                }
                            }

                        }
                    }

                    if (canShowMessage) {
                        createFileForDetectedBeacon(minorOrInstance, minutesOfDay, folder);
                        showNotification(minorOrInstance);
                    }
                } else {
                    Utility.NotifoodLog("Can't show notification because permission to android.permission.WRITE_EXTERNAL_STORAGE not granted!!!\nYou can't earn from this user unless the notifood have access to android.Manifest.permission.WRITE_EXTERNAL_STORAGE");
                }
            }
        }).start();
    }

    private void createFileForDetectedBeacon(String minorOrInstance, long minutesOfDay, File folder){
        try {
            String fileFormat = getString(R.string.detected_beacon_file_format);
            fileFormat = String.format(Locale.US, fileFormat,
                    minorOrInstance,
                    String.valueOf(minutesOfDay-3),
                    String.valueOf(minutesOfDay+3));
            if(!folder.isDirectory())
                folder.mkdir();
            File file = new File(folder, fileFormat);
            if(!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            Utility.NotifoodLog("Can't create beacon file, reason:"+e.toString());
        }
    }

    private void showNotification(String minorOrInstance){
        String title = "Title";
        String body = "Body";

        int mNotificationId = 0;
        if (getBeaconType()==Declaration.enmBeaconType.enm_BT_EDDYSTONE)
            mNotificationId = Integer.valueOf(minorOrInstance.substring(2, minorOrInstance.length()), 16);
        else if (getBeaconType()==Declaration.enmBeaconType.enm_BT_IBEACON)
            mNotificationId = Integer.valueOf(minorOrInstance);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notifood_msg)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }
}