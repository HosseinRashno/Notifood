package com.notifood.notifoodlibrary.models;

import com.google.gson.annotations.SerializedName;
import com.notifood.notifoodlibrary.utils.Declaration;

import java.util.ArrayList;

/**
 * Created by mrashno on 10/4/2017.
 */

public class RestaurantModel {
    @SerializedName("ID")
    private int ID;
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    @SerializedName("Lang")
    protected double longitude;
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @SerializedName("Lat")
    protected double latitude;
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @SerializedName("Image")
    private String Image;
    public String getImage() {
        return Image;
    }
    public void setImage(String image) {
        Image = image;
    }

    @SerializedName("Description")
    private String Description;
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

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

    @SerializedName("Rank")
    private double Rank;
    public double getRank() {
        return Rank;
    }
    public void setRank(double rank) {
        Rank = rank;
    }

    @SerializedName("Name")
    private String restaurantName;
    public String getRestaurantName() {
        return restaurantName;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @SerializedName("EdyInstance")
    private String eddystoneInstance;
    public String getEddystoneInstance() {
        return eddystoneInstance;
    }
    public void setEddystoneInstance(String eddystoneInstance) {
        this.eddystoneInstance = eddystoneInstance;
    }

    @SerializedName("IBMinorId")
    private int ibeaconMinor;
    public int getIbeaconMinor() {
        return ibeaconMinor;
    }
    public void setIbeaconMinor(int ibeaconMinor) {
        this.ibeaconMinor = ibeaconMinor;
    }

    @SerializedName("Address")
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

    @SerializedName("Contacts")
    private ArrayList<ContactObjectModel> Contacts;
    public ArrayList<ContactObjectModel> getContacts() {
        return Contacts;
    }
    public void setContacts(ArrayList<ContactObjectModel> contacts) {
        Contacts = contacts;
    }
}
