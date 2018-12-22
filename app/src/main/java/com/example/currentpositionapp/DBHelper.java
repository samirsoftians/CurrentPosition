package com.example.currentpositionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

/**
 * Created by ajinkya on 25/4/17.
 */

public class DBHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "fleetdb.db";
    static DBHelper dbHelper;

    public static synchronized DBHelper getInstance(Context context)
    {
        if (dbHelper == null)
        {
            dbHelper = new DBHelper(context.getApplicationContext());
        }
        return dbHelper;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists onlineInfo(vehicleCode text primary key,vNum text,date text,time text,lat text,lng text,location text,status text,speed text)");
        db.execSQL("create table if not exists vehicleInfo(vehicleCode integer,vehicleRegNum text primary key,active text)");
        db.execSQL("create table if not exists newvechiclelimit(id int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP table if exists onlineInfo");
        db.execSQL("DROP table if exists vechicleInfo");
        db.execSQL("DROP table if exists newvechiclelimit");
        onCreate(db);
    }

    public void updateLocation(String date, String time,String lat,String log,String location,String speed,String vehCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("lat", lat);
        contentValues.put("lng", log);
        contentValues.put("location", location);
        contentValues.put("speed", speed);

        db.update("onlineInfo", contentValues, "vehicleCode='" + vehCode + "' ", null);
    }

    public void insertVehicle(String name,String name2,String stat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("vehicleCode", name);
        values.put("vehicleRegNum", name2);
        values.put("active", stat);
        db.insert("vehicleInfo", null, values);
    }

    public void insertVehicleInfo(String name,String name2,String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put("vehicleCode", name);
        values1.put("vNum", name2);
        values1.put("date", "date");
        values1.put("time", "time");
        values1.put("lat", "54.5441");
        values1.put("lng", "78.5484");
        values1.put("location", "location");
        values1.put("speed", "0");
        values1.put("status", status);
        db.insert("onlineInfo", null, values1);
    }

}
