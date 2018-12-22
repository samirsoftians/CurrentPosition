package com.example.currentpositionapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class VehicleInfoActivity extends AppCompatActivity {
    MyDBHelper dbHelper;
    SQLiteDatabase db;
    ListView infoList;
    String[] arr = null;
    ArrayList<String> vehicles;
    ArrayList<String> date;
    ArrayList<String> time;
    ArrayList<String> location;
    ArrayList<String> lat;
    ArrayList<String> lng;
    ArrayList<String> speed;
    ArrayList<Integer> statusArray;
    TextView tv_status,tv_refresh,tv_search;
    String vehDetails;
    final Handler myHandler = new Handler();
    int i=0;
    SharedPreferences pref;
    int count;
    CardView cardView;
    String s;
    Date currentdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("VehicleInfoActivity", "In the vehicleinfoactivity");

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_info);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();
        // sasmple();
        currentdate=new Date();

      /*  final SharedPreferences prefs = getSharedPreferences("vehdetails", MODE_PRIVATE);
        s = prefs.getString("veh",null);
        //String stoppedsince=datetime-s;
        Log.e("prefer",s);*/


        startService(new Intent(VehicleInfoActivity.this,MyService.class));
        Log.e("VehicleInfoActivity", "In the init of vehicleactivity");
        infoList = (ListView) findViewById(R.id.infoList);
        tv_refresh=(TextView)findViewById(R.id.tv_refresh);
       // tv_status=(TextView)findViewById(R.id.tv_status);
        tv_search=(TextView)findViewById(R.id.tv_search);
        initArray();
        pref = getSharedPreferences("vList", Activity.MODE_PRIVATE);
        count = pref.getInt("count", -1);
        Log.e("VehicleInfoActivity", String.valueOf(count));
        // mt(count+"");
        if (count <= 0) {
            mt("Please Select Vehicles...");
            startActivity(new Intent(VehicleInfoActivity.this, SearchActivity.class));
        }

        start();
        //stoppedSince();
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("vehDetails", vehDetails);
        editor.commit();
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
                Log.e("VehicleInfoActivity", "Timer executed");
            }
        }, 30*1000, 30*1000);

        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                finish();
                stopService(new Intent(VehicleInfoActivity.this,MyService.class));
                startService(new Intent(VehicleInfoActivity.this,MyService.class));
                startActivity(new Intent(VehicleInfoActivity.this,
                        VehicleInfoActivity.class));
            }
        });

      /*  tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(VehicleInfoActivity.this,StatusActivity.class));

            }
        });
*/
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VehicleInfoActivity.this,
                        SearchActivity.class));
                finish();
            }
        });

    }



    private void initArray() {

        Log.e("VehicleInfoActivity", "In the initarray");

        vehicles = new ArrayList<String>();
        date = new ArrayList<String>();
        time = new ArrayList<String>();
        location = new ArrayList<String>();
        lat = new ArrayList<String>();
        lng = new ArrayList<String>();
        speed=new ArrayList<String>();
        statusArray = new ArrayList<Integer>();
    }

    @SuppressLint("ResourceAsColor")
    private void start() {
        Log.i("VehicleInfoActivity", "In the start");
        for (int i = 0; i < count; i++) {

            vehicles.add(pref.getString("vehicle" + i, null));
            Log.e("VehicleInfoActivity", String.valueOf(vehicles));
            // mt(pref.getString("vehicle"+i, null));
        }
        dbHelper = new MyDBHelper(VehicleInfoActivity.this);
        db = dbHelper.getReadableDatabase();
        for (int i = 0; i < vehicles.size(); i++) {
            String[] selectionArgs = {vehicles.get(i)

            };
            Log.e("VehicleInfoActivity","selection args"+ String.valueOf(vehicles.get(i)));

            Cursor cursor = db.query("onlineInfo", null, "vNum=?", selectionArgs,
                    null, null, null);
            // mt("tushar");
            // mt("name" +cursor.getString(1));
            cursor.move(0);
            while (cursor.moveToNext()) {
                date.add(cursor.getString(2));
                time.add(cursor.getString(3));
                location.add(cursor.getString(6));
                lat.add(cursor.getString(4));
                lng.add(cursor.getString(5));
                speed.add(cursor.getString(8));
                statusArray.add(cursor.getInt(7));
                cursor.moveToFirst();
                // mt(cursor.getString(7) + " " +cursor.getString(8));
            }
            //cursor.close();
        }

		/*code begins from here*/

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(VehicleInfoActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(vehicles);
        editor.putString("key", json);
        editor.apply();

		/*ends here*/
        db.close();
        // db.close();
        // mt(date.get(2));
        VehicleInfoAdapter adapter = new VehicleInfoAdapter(this, vehicles,
                date, time, location, lat, lng, speed,statusArray);
        Log.e("VehicleInfoActivity", String.valueOf(adapter));
        infoList.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    private void mt(String text) {
        Toast.makeText(VehicleInfoActivity.this, text, Toast.LENGTH_SHORT).show();
    }


    private boolean UpdateGUI() {
        //i++;
        //tv.setText(String.valueOf(i));
        myHandler.post(myRunnable);

        return true;
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            //tv.setText(String.valueOf(i));
            //  Intent intent=new Intent(VehicleInfoActivity.this,MyService.class);
            //  startService(intent);
            //Toast.makeText(VehicleInfoActivity.this, "Updating...", Toast.LENGTH_SHORT).show();
            initArray();
            start();
        }
    };


	@Override
	public void onBackPressed()
	{

						/*startActivity(new Intent(VehicleInfoActivity.this,SearchActivity.class));
						finish();*/

        AlertDialog.Builder builder1 = new AlertDialog.Builder(VehicleInfoActivity.this);
        builder1.setTitle("Fleetview APP");
        builder1.setMessage("Are you sure want to exit");
        builder1.setIcon(R.drawable.fleetlogo);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                });

        builder1.setNegativeButton
                (
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert11 = builder1.create();
        alert11.show();


	}


}


