package com.notifood.notifoodlibrary.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by mrashno on 10/4/2017.
 */

public class WorkingHoursModel {
    @SerializedName("DayIndex")
    private int DayIndex;
    public int getDayIndex() {
        return DayIndex;
    }
    public void setDayIndex(int dayIndex) {
        DayIndex = dayIndex;
    }

    @SerializedName("weekDay")
    private String weekDay;
    public String getWeekDay() {
        return weekDay;
    }
    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    @SerializedName("Times")
    private ArrayList<TimeObjectModel> Times;
    public ArrayList<TimeObjectModel> getTimes() {
        if (Times==null)
            Times = new ArrayList<>();
        return Times;
    }
    public void setTimes(ArrayList<TimeObjectModel> times) {
        Times = times;
    }
}
