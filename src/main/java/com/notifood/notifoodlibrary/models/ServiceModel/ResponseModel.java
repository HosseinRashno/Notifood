package com.notifood.notifoodlibrary.models.ServiceModel;

import com.google.gson.annotations.SerializedName;
import com.notifood.notifoodlibrary.models.RestaurantModel;
import com.notifood.notifoodlibrary.models.SettingModel;

import java.util.ArrayList;

/**
 * Created by mrashno on 10/4/2017.
 */

public class ResponseModel {
    @SerializedName("setting")
    private SettingModel setting;
    public SettingModel getSetting() {
        if (setting==null)
            setting = new SettingModel();
        return setting;
    }
    public void setSetting(SettingModel setting) {
        this.setting = setting;
    }

    @SerializedName("Restaurants")
    private ArrayList<RestaurantModel> restaurants;
    public ArrayList<RestaurantModel> getRestaurants() {
        if (restaurants==null)
            restaurants = new ArrayList<>();
        return restaurants;
    }
    public void setRestaurants(ArrayList<RestaurantModel> restaurants) {
        this.restaurants = restaurants;
    }
}
