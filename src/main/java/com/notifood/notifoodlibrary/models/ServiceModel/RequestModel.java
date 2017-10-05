package com.notifood.notifoodlibrary.models.ServiceModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mrashno on 10/4/2017.
 */

public class RequestModel {
    @SerializedName("guid")
    private String GUID;
    public String getGUID() {
        return GUID;
    }
    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    @SerializedName("packageName")
    private String PackageName;
    public String getPackageName() {
        return PackageName;
    }
    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    @SerializedName("devKey")
    private String DevKey;
    public String getDevKey() {
        return DevKey;
    }
    public void setDevKey(String devKey) {
        DevKey = devKey;
    }
}
