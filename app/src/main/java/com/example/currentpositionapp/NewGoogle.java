package com.example.currentpositionapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewGoogle extends FragmentActivity implements LocationListener
{

    private GoogleMap googleMap;
    private double latitude;
    private double longitude;
    private String date;
    private String time;
    private String vname;
    private String name,speed;
    MyDBHelper dbHelper;
    SQLiteDatabase db;
    String dat;
    String tim;
    String location;
    String lat;
    String lng;
    String sped;
    String lati,longi,datee,timee,locat,vcode;
    TextView textVeh,textDate,textTime,textSpeed,txtLocation;
    final Handler myHandler = new Handler();
    LatLng loc,loki;
    int markerNo=0;
    CountDownTimer _countDownTimer;
    String newDateString;
    private int mInterval = 2*60*1000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i("CPA","In the googlemap");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_google);
        dbHelper = new MyDBHelper(NewGoogle.this);
        db = dbHelper.getReadableDatabase();
        Log.e("locat","dbhelper reading db1");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Bundle bundle1=intent.getExtras();
        latitude = Double.parseDouble(bundle.getString("lat"));

        longitude = Double.parseDouble(bundle.getString("lng"));
        textVeh=(TextView)findViewById(R.id.vName);
        textDate=(TextView)findViewById(R.id.vDate);
        textTime=(TextView)findViewById(R.id.vTime);
        textSpeed=(TextView)findViewById(R.id.vSpeed);
        txtLocation=(TextView)findViewById(R.id.vLocation);
        date = bundle.getString("date");
        Log.e("newGoogle",date);
        time = bundle.getString("time");
        Log.e("newGoogle",time);
        vname = bundle.getString("vname");
        Log.e("newGoogle",","+vname+",");
        name=bundle.getString("location");
        Log.e("newGoogle",name);
        speed=bundle.getString("speed");
        Log.e("newGoogle",speed);



        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Cursor c1 =db.rawQuery("SELECT * FROM onlineInfo WHERE vNum LIKE '"+ vname +"'", null);
                c1.move(0);
                while (c1.moveToNext()) {
                    datee=c1.getString(2);
                    Log.e("datee","dat"+datee);
                    timee=c1.getString(3);
                    Log.e("timee","tim"+timee);
                    locat=c1.getString(6);
                    Log.e("locat","loc"+locat);
                    lati=c1.getString(4);
                    Log.e("lati",lati);
                    longi=c1.getString(5);
                    Log.e("longi",longi);
                    sped=c1.getString(8);
                    vcode=c1.getString(0);
                    c1.moveToFirst();
                    loc = new LatLng(Double.valueOf(lati),Double.valueOf(longi));

                    Log.e("vcode", String.valueOf(vcode));
                }
                Log.e("NewGoogle", "Timer executed");
            }
        }, 0, 30*1000);

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
                Log.e("NewGoogle", "Timer executed");
            }
        }, 0, 30*1000);



        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }
        else
        {
            googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.setTrafficEnabled(true);
            mHandler = new Handler();
            startRepeatingTask();

          /*  if(this._countDownTimer != null){
                this._countDownTimer.cancel();
            }
            this._countDownTimer = new CountDownTimer( 50*1000, 1000){

                @Override
                public void onFinish() {

                    isCounterRunning = false;
                    yourOperation();
                }

                @Override
                public void onTick(long millisUntilFinished) {
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions()
                            .position(loc)
                            .title(vname)
                            .snippet(date + "   "  +time)
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.one)));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                    Log.e("log.e", String.valueOf(loc));
                 //   googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                    Log.e("milisec", String.valueOf(millisUntilFinished));
                    // Enable / Disable Rotate gesture
                    googleMap.getUiSettings().setRotateGesturesEnabled(true);

                    // Enable / Disable zooming functionality
                    googleMap.getUiSettings().setZoomGesturesEnabled(true);



                }

            }.start();
*/
            //yourOperation();
            Log.e("timer restart","after n seconds");

            //move camera to location
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                   /* LinearLayout info = new LinearLayout(getApplicationContext());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(getApplicationContext());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(getApplicationContext());
                    snippet.setTextColor(Color.GRAY);
                    snippet.setGravity(Gravity.CENTER);
                    snippet.setText(newDateString+"  "+timee);


                    TextView snippet2 = new TextView(getApplicationContext());
                    snippet2.setTextColor(Color.GRAY);
                    snippet2.setGravity(Gravity.CENTER);
                    snippet2.setText(speed+"KMPH");

                    TextView snippet1 = new TextView(getApplicationContext());
                    snippet1.setTextColor(Color.GRAY);
                    snippet1.setText(locat);

                    info.addView(title);
                    info.addView(snippet);
                    info.addView(snippet2);
                    info.addView(snippet1);
*/
                    View view = getLayoutInflater()
                            .inflate(R.layout.custom_info_window, null);

                    TextView name_tv = (TextView) view.findViewById(R.id.vname);
                    TextView details_tv = (TextView) view.findViewById(R.id.vdate);
                    TextView food_tv = (TextView) view.findViewById(R.id.vtime);
                    TextView transport_tv = (TextView) view.findViewById(R.id.vLocation);
                    TextView speed = (TextView) view.findViewById(R.id.vSpeed);

                    name_tv.setText(marker.getTitle());
                    details_tv.setText(newDateString);
                    food_tv.setText(timee);
                    transport_tv.setText(locat);
                    speed.setText(sped+"KMPH");

                    return view;
                }
            });

        }

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(vname)
                        .snippet(date + "   "  +time)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.one)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 19));
                Log.e("log.e", String.valueOf(loc));
                //   googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
             //   Log.e("milisec", String.valueOf(millisUntilFinished));
                // Enable / Disable Rotate gesture
                googleMap.getUiSettings().setRotateGesturesEnabled(true);

                // Enable / Disable zooming functionality
                googleMap.getUiSettings().setZoomGesturesEnabled(true);


            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private boolean UpdateGUI() {
        //i++;
        //tv.setText(String.valueOf(i));
        myHandler.post(myRunnable);

        return true;
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            dbHelper = new MyDBHelper(NewGoogle.this);
            Log.e("newGoogle","dbhelper initialized");
            db = dbHelper.getReadableDatabase();
            Log.e("newGoogle","dbhelper reading db");
            // Cursor c = db.rawQuery("SELECT * FROM 'onlineInfo' WHERE vNum LIKE ' "+vname+" '", null);
            // Cursor c =db.rawQuery("SELECT * FROM onlineInfo WHERE vNum LIKE ' MH15 EF 0609 '", null);
            // Cursor c =db.rawQuery("SELECT * FROM onlineInfo WHERE vNum LIKE ' "+vname+" '", null);
            Cursor c =db.rawQuery("SELECT * FROM onlineInfo WHERE vNum LIKE '"+ vname +"'", null);

            Log.e("newGoogle",c.toString());
            c.move(0);
            while (c.moveToNext()) {
                dat=c.getString(2);
                Log.e("newGoogle","dat"+dat);
                tim=c.getString(3);
                Log.e("newGoogle","tim"+tim);
                location=c.getString(6);
                Log.e("newGoogle","loc"+location);
                lat=c.getString(4);
                lng=c.getString(5);
                sped=c.getString(8);
                c.moveToFirst();
                textVeh.setText(vname);
                Log.e("setting time",tim);
                textTime.setText(tim);
                textSpeed.setText(sped+"KMPH");
                txtLocation.setText(location);
                SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd ");
                Date newDate = null;
                try {
                    newDate = spf.parse(dat);
                    spf = new SimpleDateFormat("dd MMM yyyy");
                    newDateString = spf.format(newDate);
                    textDate.setText(newDateString);
                    Log.e("setting text",dat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
    };

    @Override
    public void onLocationChanged(Location location)
    {
        Log.i("CPA","GoogleMap: Onlocation changed");


        // Getting latitude of the current location
        double latitude = 15.763544;

        // Getting longitude of the current location
        double longitude = 18.346343;

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

    }

    @Override
    public void onProviderDisabled(String provider)
    {

        //TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider)
    {

        //TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

        // TODO Auto-generated method stub
    }

    boolean isCounterRunning  = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    private void yourOperation() {
        if( !isCounterRunning ){
            isCounterRunning = true;
            _countDownTimer.start();
            Log.e("timer restart","inside if");
        }
        else{
            _countDownTimer.cancel(); // cancel
            _countDownTimer.start();  // then restart
            Log.e("timer restart","inside else");

        }



    }

}