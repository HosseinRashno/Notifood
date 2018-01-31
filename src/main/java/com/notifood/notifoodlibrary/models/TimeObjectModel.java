package com.notifood.notifoodlibrary.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mrashno on 1/25/2018.
 */

public class TimeObjectModel {
    @SerializedName("start")
    private String start;
    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }

    @SerializedName("end")
    private String end;
    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }
}
