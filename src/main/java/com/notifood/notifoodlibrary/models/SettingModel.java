package com.notifood.notifoodlibrary.models;

import com.google.gson.annotations.SerializedName;
import com.notifood.notifoodlibrary.utils.Declaration;

import java.io.Serializable;

/**
 * Created by mrashno on 10/4/2017.
 */

public class SettingModel implements Serializable {
    @SerializedName("Type")
    private int beaconType;
    public Declaration.enmBeaconType getBeaconType() {
        for (Declaration.enmBeaconType enm:Declaration.enmBeaconType.values()){
            if (enm.getCode()==beaconType)
                return enm;
        }
        return null;
    }
    public void setBeaconType(Declaration.enmBeaconType beaconTypeEnm) {
        this.beaconType = beaconTypeEnm.getCode();
    }

    @SerializedName("EdyNamesapce")
    private String eddystoneNamespace;
    public String getEddystoneNamespace() {
        return eddystoneNamespace;
    }
    public void setEddystoneNamespace(String eddystoneNamespace) {
        this.eddystoneNamespace = eddystoneNamespace;
    }

    @SerializedName("EdyInstantStart")
    private String eddystoneInstanceStart;
    public String getEddystoneInstanceStart() {
        return eddystoneInstanceStart;
    }
    public void setEddystoneInstanceStart(String eddystoneInstanceStart) {
        this.eddystoneInstanceStart = eddystoneInstanceStart;
    }

    @SerializedName("EdyInstantEnd")
    private String eddystoneInstanceEnd;
    public String getEddystoneInstanceEnd() {
        return eddystoneInstanceEnd;
    }
    public void setEddystoneInstanceEnd(String eddystoneInstanceEnd) {
        this.eddystoneInstanceEnd = eddystoneInstanceEnd;
    }

    @SerializedName("UId")
    private String iBeaconUUID;

    public String getiBeaconUUID() {
        return iBeaconUUID;
    }

    public void setiBeaconUUID(String iBeaconUUID) {
        this.iBeaconUUID = iBeaconUUID;
    }

    @SerializedName("IMajor")
    private int iBeaconMajor;
    public int getiBeaconMajor() {
        return iBeaconMajor;
    }
    public void setiBeaconMajor(int iBeaconMajor) {
        this.iBeaconMajor = iBeaconMajor;
    }

    @SerializedName("IminorStart")
    private int iBeaconMinorStart;
    public int getiBeaconMinorStart() {
        return iBeaconMinorStart;
    }
    public void setiBeaconMinorStart(int iBeaconMinorStart) {
        this.iBeaconMinorStart = iBeaconMinorStart;
    }

    @SerializedName("IminorEnd")
    private int iBeaconMinorEnd;
    public int getiBeaconMinorEnd() {
        return iBeaconMinorEnd;
    }
    public void setiBeaconMinorEnd(int iBeaconMinorEnd) {
        this.iBeaconMinorEnd = iBeaconMinorEnd;
    }

    @SerializedName("DataUpdatePeriod")
    private int updatePeriod;
    public int getUpdatePeriod() {
        return updatePeriod;
    }
    public void setUpdatePeriod(int updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    @SerializedName("PersonID")
    private String personId;
    public String getPersonId() {
        return personId;
    }
    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
