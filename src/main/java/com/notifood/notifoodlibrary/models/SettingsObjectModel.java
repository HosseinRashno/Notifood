package com.notifood.notifoodlibrary.models;

/**
 * Created by mrashno on 10/5/2017.
 */

public class SettingsObjectModel {
    private String columnName;
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    private String stringValue;
    public String getStringValue() {
        return stringValue;
    }
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    private Integer integerValue;
    public Integer getIntegerValue() {
        return integerValue;
    }
    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }

}
