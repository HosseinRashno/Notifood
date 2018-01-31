package com.notifood.notifoodlibrary.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mrashno on 1/25/2018.
 */

public class ContactObjectModel {
    @SerializedName("cntIndex")
    private int cntIndex;
    public int getCntIndex() {
        return cntIndex;
    }
    public void setCntIndex(int cntIndex) {
        this.cntIndex = cntIndex;
    }

    @SerializedName("Title")
    private String Title;
    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }

    @SerializedName("value")
    private ArrayList<String> value;
    public ArrayList<String> getValue() {
        if (value==null)
            value = new ArrayList<>();
        return value;
    }
    public void setValue(ArrayList<String> value) {
        this.value = value;
    }
}
