package com.notifood.notifoodlibrary.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mrashno on 10/4/2017.
 */

public class RestaurantModel {
    @SerializedName("instance")
    private String eddystoneInsnace;
    public String getEddystoneInsnace() {
        return eddystoneInsnace;
    }
    public void setEddystoneInsnace(String eddystoneInsnace) {
        this.eddystoneInsnace = eddystoneInsnace;
    }

    @SerializedName("minor")
    private String ibeaconMinor;
    public String getIbeaconMinor() {
        return ibeaconMinor;
    }
    public void setIbeaconMinor(String ibeaconMinor) {
        this.ibeaconMinor = ibeaconMinor;
    }

    @SerializedName("name")
    private String restaurantName;
    public String getRestaurantName() {
        return restaurantName;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @SerializedName("rawAddress")
    private String rawAddress;
    public String getRawAddress() {
        return rawAddress;
    }
    public void setRawAddress(String rawAddress) {
        this.rawAddress = rawAddress;
    }

    @SerializedName("workingHours")
    private ArrayList<WorkingHoursModel> workingHours;
    public ArrayList<WorkingHoursModel> getWorkingHours() {
        if (workingHours==null)
            workingHours = new ArrayList<>();
        return workingHours;
    }
    public void setWorkingHours(ArrayList<WorkingHoursModel> workingHours) {
        this.workingHours = workingHours;
    }
}
