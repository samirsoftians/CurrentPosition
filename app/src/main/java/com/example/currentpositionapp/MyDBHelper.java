package com.example.currentpositionapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

//import android.database.DatabaseErrorHandler;
//Local database used for the storing the onlineinfo of the vechile
public class MyDBHelper extends SQLiteOpenHelper
{
	public MyDBHelper(Context context)
	{
		super(context, "fleetdb", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		try
		{
			db.execSQL("create table if not exists onlineInfo(vehicleCode text primary key,vNum text,date text,time text,lat text,lng text,location text,status text,speed text)");
			db.execSQL("create table if not exists vehicleInfo(vehicleCode integer,vehicleRegNum text primary key,active text)");
			db.execSQL("create table if not exists newvechiclelimit(id int)");
		}
		catch(SQLiteException e)
		{



		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		 db.execSQL("DROP table if exists onlineInfo");
		 db.execSQL("DROP table if exists vechicleInfo");
		 db.execSQL("DROP table if exists newvechiclelimit");
		 //db.execSQL("DROP table vechiclelimit");
		   onCreate(db);
	}

}
