package com.notifood.notifoodlibrary.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mrashno on 10/4/2017.
 */

public class WorkingHoursModel {
    @SerializedName("s")
    private String startHourOfWork;
    public String getStartHourOfWork() {
        return startHourOfWork;
    }
    public void setStartHourOfWork(String startHourOfWork) {
        this.startHourOfWork = startHourOfWork;
    }

    @SerializedName("e")
    private String endHourOfWork;
    public String getEndHourOfWork() {
        return endHourOfWork;
    }
    public void setEndHourOfWork(String endHourOfWork) {
        this.endHourOfWork = endHourOfWork;
    }
}
