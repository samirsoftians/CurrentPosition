package com.example.currentpositionapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
public class StatusActivity extends AppCompatActivity
{
	ListView list;
	ArrayList<String> statusList;
	MyDBHelper helper;
	ArrayList<String> status;
	ArrayList<Integer> speed;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status_lay);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
		View view = getSupportActionBar().getCustomView();
		statusList = new ArrayList<String>();
		status = new ArrayList<String>();
		speed = new ArrayList<Integer>();
		helper = new MyDBHelper(StatusActivity.this);
		list = (ListView) findViewById(R.id.list);
		getList();
		StatusAdapter adapter = new StatusAdapter(this, statusList,status,speed);
		list.setAdapter(adapter);		
		list.setDividerHeight(5);		
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(StatusActivity.this, VehicleInfoActivity.class);
		startActivity(i);
		finish();
	}
	private void getList()
	{
		SQLiteDatabase data1;
		data1 = helper.getReadableDatabase();
		Cursor c = data1
				.query("onlineInfo", null, null, null, null, null, null);
		c.move(0);
		while (c.moveToNext())
		{
			// mt(c.getString(1));
			statusList.add(c.getString(1));
			status.add(c.getString(7));
			speed.add(c.getInt(8));
		}
		c.close();
		data1.close();
	}
}
