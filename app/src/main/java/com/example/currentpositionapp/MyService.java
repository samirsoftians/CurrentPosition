package com.example.currentpositionapp;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.playlog.internal.LogEvent;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
// Service is used for the perfroming the long background operation

public class MyService extends Service {
    MyDBHelper dbHelper;
    // ArrayList<String> vehicles;
    ArrayList<String> vehicles = null;
    String[] arr = null;
    SQLiteDatabase db1 = null;
    SQLiteDatabase db = null;
    OnlineThread thread = null;
    String date = null, time = null, lat = null, log = null, location = null, vehiId;
    int k = 0, i = 0;
    int size = 0;
    String speed = " ";
    ArrayList<String> codes;
    MyDBHelper help;
    Cursor c;
    Vehical vehcle;
    private static Timer timer = new Timer();
    String dt1, dt2;
    SharedPreferences sharedPreferences;
    String newDate,newTime;
    // ProgressDialog progressDialog;
    @Override
    public void onStart(Intent intent, int startId) {

        // TODO Auto-generated method stub
        // super.onStart(intent, startId);
        // mt("inside service");
        Log.e("MyService ", "Called");
        sharedPreferences = getSharedPreferences("vehicle", MODE_PRIVATE);
        dt1=sharedPreferences.getString("codeVehicle",null);
        dt2=sharedPreferences.getString("nameVehicle",null);
        //dt1 = new String();
       // dt2 = new String();

        vehcle = (Vehical) getApplicationContext();

      //  dt1 = vehcle.getVehCodes();
        Log.e("dt1 : ",": " + dt1);
        dt1 = dt1.substring(0, dt1.length() - 1);
       // dt2 = vehcle.getVehNames();
        Log.e("dt2 : ",": " + dt2);
        dt2 = dt2.substring(0, dt2.length() - 1);
        Log.e("dt1 : " + dt1, "dt2 : " + dt2);

        vehicles = new ArrayList<String>();
        codes = new ArrayList<String>();
        Log.e("MyService", String.valueOf(codes));
        dbHelper = new MyDBHelper(MyService.this);
       // dbHelper = MyDBHelper.getInstance(context);
        help = new MyDBHelper(MyService.this);
        db = help.getWritableDatabase();


        // dbHelper = DBHelper.getInstance(context);

        String data1[] = dt1.split(" ");
        int len1 = data1.length;
        Log.e("Length ", " : " + len1);
        for (int i = 0; i < len1; i++) {
            codes.add(data1[i]);
        }

        Log.e("Codes Size ", " : " + codes.size());

        String data2[] = dt2.split(", ");
        int len2 = data2.length;
        Log.e("Length ", " : " + len2);
        for (int i = 0; i < len2; i++) {
            vehicles.add(data2[i]);
        }

        Log.e("Vehicles Size ", " : " + vehicles.size());

        timer.scheduleAtFixedRate(new mainTask(), 1, 2 * 60 * 1000);

        /*Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                SharedPreferences pref = getSharedPreferences("vList",
                        Activity.MODE_PRIVATE);
                int count = pref.getInt("count", -1);
                for (int i = 0; i < count; i++) {
                    vehicles.add(pref.getString("vehicle" + i, null));
                }
                getCode();

                boolean bool = isOnline();
                if (bool == true) {
                    // mDataLodingStatusMessageView.setText(R.string.data_fetching_progress_in);

                    for (int i = 0; i < vehicles.size(); i++) {
                        // mt("bool is true");
                        thread = new OnlineThread();
                        thread.execute((Void) null);

                    }
                    Log.e("MyService-if", String.valueOf(vehicles.size()));

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet connection.",
                            Toast.LENGTH_LONG).show();
                    Log.e("MyService", "inside else");
                }

                Log.e("MyService Timer", "Timer executed");
            }
        }, 0, 15000);*/


    }

    private class mainTask extends TimerTask {
        public void run() {
            taskHandler.sendEmptyMessage(0);
        }
    }

    private final Handler taskHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean bool = isOnline();
            if (bool == true) {
                // mDataLodingStatusMessageView.setText(R.string.data_fetching_progress_in);

                Log.e("2 MIN TIMER", " CALLED");

                thread = new OnlineThread();
                thread.execute((Void) null);

               /* for (int i = 0; i < vehicles.size(); i++) {
                    // mt("bool is true");
                    thread = new OnlineThread();
                    thread.execute((Void) null);

                }*/
                Log.e("MyService-if", String.valueOf(vehicles.size()));

            } else {
                Toast.makeText(getApplicationContext(), "No Internet connection.",
                        Toast.LENGTH_LONG).show();
                Log.e("MyService", "inside else");
            }

        }
    };

    private void getCode() {
        int x = vehicles.size();
        arr = new String[x];
        for (int i = 0; i < x; i++) {
            arr[i] = vehicles.get(i);
        }

        //dbHelper = new MyDBHelper(MyService.this);
        db1 = dbHelper.getReadableDatabase();
        for (int i = 0; i < vehicles.size(); i++) {
            String[] selectionArgs = {arr[i]};
            c = db1.query("onlineInfo", null, "vNum = ?",
                    selectionArgs, null, null, null);
            c.move(0);
            while (c.moveToNext()) {
                // mt(c.getString(1));
                codes.add(c.getString(0));
            }
        }
        //c.close();
        db1.close();
    }

    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        boolean result = false;
        if (ni != null) {
            if (ni.getState() == NetworkInfo.State.CONNECTED) {
                result = true;
            }
        }
        return result;
    }

    public class OnlineThread extends AsyncTask<Void, String, Boolean> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*
             * progressDialog = new ProgressDialog(getApplication());
			 * progressDialog.setTitle("Fleetview App");
			 * progressDialog.setMessage("Accessing data....");
			 * progressDialog.setIcon(R.drawable.transworldlogo1);
			 * progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			 * progressDialog.show();
			 */
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // try { // Simulate network access.
            Log.e("MyService doInback", "Called ");
            String response = "";
            Log.e("MyService doInback ", " : " + codes.size());
            try {

               // String url = "http://192.168.2.124:6060/FleetAndrProject/OnlineData?vehiclecode=" + dt1;
                String url = "http://103.241.181.36:8080/FleetAndrProjectTest/OnlineData?vehiclecode=" + dt1;
                //  codes.get(i);
                Log.e("Myservice url ", url);
                //  Log.e("MyService vehicle code", String.valueOf(codes));

                // i++;
                url = url.replaceAll(" ", "%20");
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(content));
                Log.e("MyService content", String.valueOf(content));

                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                    Log.e("MyService Response : ", response);
                }

            } catch (Exception e) {
                System.out.println("MyService Exception occured!!" + e);
                Log.e("Exception while ", "fetching data " + e.getMessage());
                return false;
            }

            if (!(response.equals("No_Data"))) {

                Log.e("MyService No Data ", "");

                try {
                    String[] rows = response.split("##");
                    for (int i = 0; i < rows.length; i++) {
                        String splittedVal = rows[i];
                        Log.e("MyService Splitted Val", " : " + splittedVal);
                    }
                    for (int m = 0; m < rows.length; m++) {
                        if (!rows[m].equals(" ")) {
                            String details[] =rows[m].split("\\$");
                            publishProgress(details);
                        }
                    }

                    //updateDB(rows);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Exception while ", "Splitting data " + e.getMessage());
                }
              //  Log.e("MyService-publish prog", "Inside if executed for calling update() ");
                return true;
            } else {
                Log.e("MyService-publish prog", "Inside else executed");
                return false;
            }
        }

        // TODO: register the new account here. //return true;

        @Override
        protected void onProgressUpdate(String... arg) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(arg);
            // name = values[0];

            Log.e("MyService onProgress", " UpdateCalled");
            try {

                date = arg[0];
                Log.e("MyService date", date);
                time = arg[1];
                Log.e("MyService time", time);
                lat = arg[2];
                Log.e("MyService lat", lat);
                log = arg[3];
                Log.e("MyService long", log);
                location = arg[4];
                Log.e("MyService location ", location);
                speed = arg[5];
                Log.e("MyService speed ", speed);
                vehiId = arg[6];
                Log.e("MyService Vehicle Id", vehiId);

                ContentValues values = new ContentValues();
                values.put("date", date);
                values.put("time", time);
                values.put("lat", lat);
                values.put("lng", log);
                values.put("location", location);
                values.put("speed", speed);

                Log.e("date",","+date+",");
                Log.e("time",","+time+",");
                Log.e("lat",","+lat+",");
                Log.e("lng",","+log+",");
                Log.e("loc",","+location+",");
                Log.e("speed",","+speed+",");


                vehiId = vehiId.substring(1);
                //vehiId=vehiId+" ";
                Log.e("vehicle", "Id :" + ","+vehiId+",");
                newDate = date.substring(0, date.length() - 1);
                newDate = newDate.substring(1);
                newDate=newDate+" ";
                Log.e("datemsg",","+newDate+",");
                newTime = time.substring(0, time.length() - 1);
                newTime = newTime.substring(1);
                newTime=newTime+" ";
                Log.e("timemsg",","+newTime+",");

                db.update("onlineInfo", values, "vehicleCode = ? AND date <= ? AND time < ? ", new String[]{vehiId,newDate,newTime});

                Log.e("Updated Successfully ",""+values);
            }catch (Exception e){
                Log.e("Exception ",""+e.getMessage());
            }
        }


        @Override
        protected void onPostExecute(final Boolean success) {

        }
    }

    public void updateData(String date,String time,String lat,String log,String location,String speed, String vehCode) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("date",date);
        contentValues.put("time",time);
        contentValues.put("lat",lat);
        contentValues.put("lng",log);
        contentValues.put("location",location);
        contentValues.put("speed",speed);
        db.update("onlineInfo", contentValues, "vehicleCode = ?",new String[] {vehCode});
        //db.update("ChallanItems", values2, "ItemNo = ?", new String[] { code })
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private void mt(String text) {
        Toast.makeText(MyService.this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.e("Service ","DEstroyed !!!");
//		db.close();
//		c.close();
//		db1.close();


    }



    public void UpdateRegStatus(String date, String time, String lat, String log, String location, String speed, String vehicleCode) {
        Log.e("database values", date + "," + time + "," + lat + "," + log + "," + location + "," + speed + "," + vehicleCode);

        db = help.getWritableDatabase();
        //String RegularStampsDatabase = Environment.getExternalStorageDirectory().getPath() + "/fleetdb.db";
        try {
            SQLiteDatabase database = openOrCreateDatabase("fleetdb.db", MODE_PRIVATE, null);
            database.execSQL("update onlineInfo set date='" + date + "',time='" + time + "',lat='" + lat + "',lng='" + log + "',location='" + location + "',speed='" + speed + "' where vehicleCode='" + vehicleCode + "'");

            database.close();
            Log.e("Updated Successfully", "@");
        } catch (Exception e) {
            Log.e("Exception in update", e.getMessage());

        }
    }

    public String retrieveStampsData(String vehId1) {

        Log.e("retrieveStampsData", " Called");

        String str2 = "";

        try {

            SQLiteDatabase database = openOrCreateDatabase("fleetdb.db", Context.MODE_PRIVATE, null);
            String stampsQuery = "select * from onlineInfo where vehicleCode='" + vehId1 + "'";
            Cursor stampCR = database.rawQuery(stampsQuery, null);
            stampCR.moveToFirst();
            while (!stampCR.isAfterLast()) {
                String s1 = stampCR.getString(stampCR.getColumnIndex("date"));
                String s2 = stampCR.getString(stampCR.getColumnIndex("time"));
                String s3 = stampCR.getString(stampCR.getColumnIndex("lat"));
                String s4 = stampCR.getString(stampCR.getColumnIndex("lng"));
                String s5 = stampCR.getString(stampCR.getColumnIndex("location"));
                String s6 = stampCR.getString(stampCR.getColumnIndex("speed"));

                //str2 = stampCR.getString(stampCR.getColumnIndex("RegularStamps"));
                str2 = s1 + ", " + s2 + ", " + s3 + ", " + s4 + ", " + s5 + ", " + s6;
                Log.e("Data from local", " database " + str2);
                stampCR.moveToNext();
            }

            stampCR.close();
            database.close();

        } catch (Exception e) {
            Log.e("not retrieve stamps", e.getMessage());

        }

        return str2;
    }

}
