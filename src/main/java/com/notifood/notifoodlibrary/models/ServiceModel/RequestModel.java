package com.notifood.notifoodlibrary.models.ServiceModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mrashno on 10/4/2017.
 */

public class RequestModel {
    @SerializedName("personId")
    private String personId;
    public String getPersonId() {
        return personId;
    }
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @SerializedName("packageName")
    private String packageName;
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @SerializedName("DeveloperKey")
    private String DeveloperKey;
    public String getDeveloperKey() {
        return DeveloperKey;
    }
    public void setDeveloperKey(String developerKey) {
        DeveloperKey = developerKey;
    }
}
