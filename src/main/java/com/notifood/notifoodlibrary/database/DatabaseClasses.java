package com.notifood.notifoodlibrary.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by mrashno on 10/4/2017.
 */

public abstract class DatabaseClasses {
    protected SQLiteDatabase currentDB;
    public SQLiteDatabase getCurrentDB() {
        return currentDB;
    }
    public void setCurrentDB(SQLiteDatabase currentDB) {
        this.currentDB = currentDB;
    }

    protected DatabaseClasses(){}

    protected void OpenForWrite(){
        DBHelper dbOpenHelper = new DBHelper();
        currentDB = dbOpenHelper.getWritableDatabase();
    }

    protected void OpenForRead(){
        DBHelper dbOpenHelper = new DBHelper();
        currentDB = dbOpenHelper.getReadableDatabase();
    }

    protected void CloseConnection(){
        currentDB.close();
    }

    abstract public ArrayList<Object> SelectAll();
    abstract public Object SelectById(int id);
    abstract public boolean InsertToTable(Object object);
    abstract public boolean DeleteById(int id);
}