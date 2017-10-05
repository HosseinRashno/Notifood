package com.notifood.notifoodlibrary.models;

import com.google.gson.annotations.SerializedName;
import com.notifood.notifoodlibrary.utils.Declaration;

/**
 * Created by mrashno on 10/4/2017.
 */

public class SettingModel {
    @SerializedName("type")
    private Declaration.enmBeaconType beaconType;
    public Declaration.enmBeaconType getBeaconType() {
        return beaconType;
    }
    public void setBeaconType(Declaration.enmBeaconType beaconType) {
        this.beaconType = beaconType;
    }

    @SerializedName("namesapce")
    private String eddystoneNamespace;
    public String getEddystoneNamespace() {
        return eddystoneNamespace;
    }
    public void setEddystoneNamespace(String eddystoneNamespace) {
        this.eddystoneNamespace = eddystoneNamespace;
    }

    @SerializedName("instanceStart")
    private String eddystoneInstanceStart;
    public String getEddystoneInstanceStart() {
        return eddystoneInstanceStart;
    }
    public void setEddystoneInstanceStart(String eddystoneInstanceStart) {
        this.eddystoneInstanceStart = eddystoneInstanceStart;
    }

    @SerializedName("instanceEnd")
    private String eddystoneInstanceEnd;
    public String getEddystoneInstanceEnd() {
        return eddystoneInstanceEnd;
    }
    public void setEddystoneInstanceEnd(String eddystoneInstanceEnd) {
        this.eddystoneInstanceEnd = eddystoneInstanceEnd;
    }

    @SerializedName("uuid")
    private String iBeaconUUID;

    public String getiBeaconUUID() {
        return iBeaconUUID;
    }

    public void setiBeaconUUID(String iBeaconUUID) {
        this.iBeaconUUID = iBeaconUUID;
    }

    @SerializedName("major")
    private int iBeaconMajor;
    public int getiBeaconMajor() {
        return iBeaconMajor;
    }
    public void setiBeaconMajor(int iBeaconMajor) {
        this.iBeaconMajor = iBeaconMajor;
    }

    @SerializedName("minorStart")
    private int iBeaconMinorStart;
    public int getiBeaconMinorStart() {
        return iBeaconMinorStart;
    }
    public void setiBeaconMinorStart(int iBeaconMinorStart) {
        this.iBeaconMinorStart = iBeaconMinorStart;
    }

    @SerializedName("minorEnd")
    private int iBeaconMinorEnd;
    public int getiBeaconMinorEnd() {
        return iBeaconMinorEnd;
    }
    public void setiBeaconMinorEnd(int iBeaconMinorEnd) {
        this.iBeaconMinorEnd = iBeaconMinorEnd;
    }

    @SerializedName("updatePeriod")
    private int updatePeriod;
    public int getUpdatePeriod() {
        return updatePeriod;
    }
    public void setUpdatePeriod(int updatePeriod) {
        this.updatePeriod = updatePeriod;
    }
}
