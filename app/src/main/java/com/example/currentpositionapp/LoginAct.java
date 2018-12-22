
package com.example.currentpositionapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginAct extends AppCompatActivity {
    LoginUser loginThread;
    int validation = 0;
    EditText edtUsername, edtPassword;
    Button btnLogin, btnOfflineData;
    String name = "";
    String name2 = "";
    String status = "";
    SQLiteDatabase db;
    SQLiteDatabase db3;
    SQLiteDatabase db1;
    SQLiteDatabase db5;
    //SQLiteDatabase db4;
    int k = 0;
    SharedPreferences pref;
    SharedPreferences sharedPreferences;
    ArrayList<String> names = null;
    ArrayList<String> code = null;
    String str = "Active";
    String url = "http://twtech.in:8080/FleetViewProject/MyServlet";
    StringBuffer vehCode, vehName;
    Vehical vehcle;
    SharedPreferences sp;
    String newDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        vehcle = (Vehical) getApplicationContext();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("name") && sharedPreferences.contains("password")) {
            if (getArrayList("key").equals(null)) {
                Log.e("inside shared pref1", "name" + sharedPreferences.toString());
                startActivity(new Intent(LoginAct.this, SearchActivity.class));
            } else {
                Log.e("inside else", "else");
                startActivity(new Intent(LoginAct.this, VehicleInfoActivity.class));
            }
        } else {

            MyDBHelper helper = new MyDBHelper(LoginAct.this);
            db5 = helper.getWritableDatabase();
            db1 = helper.getWritableDatabase();
            init();
            Resources r = getResources();
        }
    }

    public ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginAct.this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
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

    private void init() {
        //Initilisation of all component
        names = new ArrayList<String>();
        code = new ArrayList<String>();
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtUsername.setHint("Enter your Username");
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPassword.setHint("Enter your password");
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnOfflineData = (Button) findViewById(R.id.btnUpdateData);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Request that a given application service be stopped. abstract void,
                validation = 0;
                loginValidation();
                if (validation == 0) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", edtUsername.getText().toString());
                    Log.e("username", edtUsername.getText().toString());
                    editor.putString("password", edtPassword.getText().toString());
                    Log.e("password", edtPassword.getText().toString());
                    editor.commit();

                    boolean cancel = false;

                    if (cancel) {
                        // There was an error; don't attempt login and focus the
                        // first
                        // form field with an error.
                        // focusView.requestFocus();
                    } else {
                        // Show a progress spinner, and kick off a background task
                        // to
                        // perform the user login attempt.
                        boolean bool = isOnline();
                        if (bool == true) {
                            loginValidation();
                            // mDataLodingStatusMessageView.setText(R.string.data_fetching_progress_in);
                            // showProgress(true);
                            MyDBHelper helper = new MyDBHelper(LoginAct.this);
                            db = helper.getWritableDatabase();
                            db3 = helper.getWritableDatabase();
                            db.delete("vehicleInfo", null, null);
                            db3.delete("onlineInfo", null, null);

                            // mt("bool is true");
                            loginThread = new LoginUser();
                            loginThread.execute((Void) null);
                            db.close();
                            db3.close();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No Internet connection.", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                } else {
                    Toast.makeText(LoginAct.this, "Please enter details in all fields!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //AsyncTask is designed to be a helper class around Thread and Handler
    //An asynchronous task is defined by a computation that runs on a background thread and whose result is published on the UI thread.
    // An asynchronous task is defined by 3 generic types, called Params, Progress and Result, and 4 steps, called onPreExecute, doInBackground, onProgressUpdate and onPostExecute.

    public class LoginUser extends AsyncTask<Void, String, Boolean> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginAct.this);
            progressDialog.setTitle("FVTracking");
            progressDialog.setMessage("Accessing data....");
            ;
            progressDialog.setIcon(R.drawable.transworldlogo1);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            // try { // Simulate network access.

            String nameOfUser = "";
            //103.241.181.36
            try { // Ssimulate network access.

                Log.i("CPA", "In the bgprocess");

                String url = "http://103.241.181.36:8080/AndrFleetApp/LoginServlet?username="
                        + edtUsername.getText().toString()
                        + "&password="
                        + edtPassword.getText().toString();
                /*String url ="http://twtech.in:8080/FleetViewProject/MyServlet?name="
						+ edtUsername.getText().toString()
						+ "&password="
						+ edtPassword.getText().toString();*/
                Log.e("url", url.toString());
                Log.i("CPA", "Send request to 36");

                // TCIxxx&password=tci
                // +edtUsername.getText().toString()+"&password="+edtPassword.getText().toString();

                //
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(content));

                Log.i("CPA", "Http Request ");


                String s = "";
                while ((s = buffer.readLine()) != null) {
                    nameOfUser += s;
                    Log.e("name of user", nameOfUser);
                }
                // publishProgress(nameOfUser);
                System.out.println("The response =>" + nameOfUser);

                Log.i("CPA", "LOG IN REQUEST GO TO THE SERVLET ");

                Thread.sleep(2000);
            } catch (InterruptedException e) {

                Log.i("CPA", "EXCEPTION " + e);
                // return false;
            } catch (Exception e) {
                System.out.println("Exception occured!!" + e);

                Log.i("CPA", "EXCEPTION " + e);

                // return false;
            }

            if (!(nameOfUser.equals("Not_OK")) && nameOfUser != null) {
                String response = "";
                Log.e("response", response);
                try {

                    Log.i("CPA", "RESOPNSE ");
                    String url = "http://103.241.181.36:8080/AndrFleetApp/CurrentPosition?typevalue="
                            + nameOfUser;
                    Log.e("second url", url.toString());
                    url = url.replaceAll(" ", "%20");

                    //Get the data from the specified resource
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);

                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader
                            (
                                    new InputStreamReader(content));

                    Log.i("CPA", "RESOPNSE FROM SERVLET ");

                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        try {
                            vehCode = new StringBuffer();
                            vehName = new StringBuffer();
                            response += s;
                            Log.e("response from server ", "Vehicles Details : " + response.toString());
                            String reposnseServer = response.toString();
                            Log.e("response new ", ": " + reposnseServer);
                            String data[] = reposnseServer.split(" ## ");
                            Log.e("Data ", "Length : " + data.length);
                            int len = data.length;

                            for (int k = 0; k < len; k++) {
                                String infoData[] = data[k].split("\\$");
                                //String dt[] = data[k].split(" $ ");
                                vehCode.append(infoData[0] + ",");
                                vehName.append(infoData[1] + ",");
                                //Log.e("vehicle Code : ",data[k]);
								/*String dt = data[k];
								Log.e("dt :",data[k]);

								String nData[] = dt.split("\\$");

								Log.e("data 1 : "+nData[0],"data 2 : "+nData[1]);
								String codeVehicle = vehCode.toString();
								String nameVehicle = vehName.toString();*/
                            }

                            String codeVehicle = vehCode.toString();
                            String nameVehicle = vehName.toString();

                           // vehcle.setVehCodes(codeVehicle);
                           // vehcle.setVehNames(nameVehicle);

                            sp = getSharedPreferences("vehicle", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("codeVehicle", codeVehicle);
                            editor.putString("nameVehicle", nameVehicle);
                            editor.commit();

                            startService(new Intent(LoginAct.this, MyService.class));

                            //Log.e("codeVehicle", " : " + codeVehicle);
                            //Log.e("namesVehicle", " : " + nameVehicle);
                        } catch (Exception e) {
                            Log.e("Exception while", " Splitting : " + e.getMessage());
                        }
                    }

                    System.out.println("The response =>" + response);

                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return false;
                } catch (Exception e) {
                    System.out.println("Exception occured!!" + e);
                    return false;
                }

                if (!(response.equals("No_Data"))) {

                    try {
                        int count = 0;
                        String[] str = response.split(" ## ");
                        while (count < str.length) {
                            String line = str[count];
                            String[] rows = line.split("\\$");

                            publishProgress(rows);

                            count++;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }


        }

        @Override
        protected void onProgressUpdate(String... arg) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(arg);
            Log.e("Array-", String.valueOf(arg.length) + "array-" + arg.toString());

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
            String date = sdf.format(cal.getTime());
            String time = stf.format(cal.getTime());

            name = arg[0];
            name2 = arg[1];
            status = arg[2];
            Log.i("CPA", "Fetch the data form the DB" + db1);
            Log.i("CPA", "Fetch the data form the DB" + db5);

            //This data is fetched from using the cuyrrentpostion.java at server side store into local database
            ContentValues values = new ContentValues();
            values.put("vehicleCode", name);
            values.put("vehicleRegNum", name2);
            values.put("active", "YES");

            //This data is fethced using the onlinedata.java at the server side
            String logVCode=","+name+",";

            Log.e("Log VCode"," : "+logVCode);

            ContentValues values1 = new ContentValues();
            values1.put("vehicleCode", name);
            values1.put("vNum", name2);
            values1.put("date", "1990-12-12");
            values1.put("time", "00:00:01");
            values1.put("lat", "54.5441");
            values1.put("lng", "78.5484");
            values1.put("location", "location");
            values1.put("speed", "0");
            values1.put("status", status);


            db5.insert("vehicleInfo", null, values);
            db1.insert("onlineInfo", null, values1);

            //startService(new Intent(LoginAct.this, MyService.class));
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            loginThread = null;
            db.close();
            db1.close();
            // showPrdfogress(false);
            progressDialog.dismiss();
            if (success) {
                // Go to the Dash board
                mt("Login successful");
                // mt("username is: " + name);
                k = 1;
                pref = getSharedPreferences("myPref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putLong("date", System.currentTimeMillis());
                editor.commit();
            } else {
                k = 0;
                // mt("username is: " + name);

                mt("Wrong username or password");
            }
            progressDialog.dismiss();
            if (k == 1) {
                db1.close();
                db5.close();
                //db4.close();

                Log.i("CPA", "Go to the serachactivity");


                finish();
                //startService(new Intent(LoginAct.this, NewService.class));
                startActivity(new Intent(LoginAct.this, SearchActivity.class));
                finish();
                // mt(code.get(2) + " " + names.get(2));
            } else {

                mt("Please enter correct username and password!!");
            }
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginAct.this);
        builder1.setMessage("Are you sure want to exit");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        finish();
                        System.exit(0);
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

	/*validating email id and password*/

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 1) {
            return true;
        }
        return false;
    }

	/*code ends here*/


    public void loginValidation() {
        //validating username

        if (edtUsername.getText().toString().trim().equals("")) {
            edtUsername.setError("Enter Username");
            validation++;
        } else if (!isValidEmail(edtUsername.getText().toString())) {
            edtUsername.setError("Please enter correct Email");
            validation++;
        }
        //validating password
        else if (edtPassword.getText().toString().trim().equals("")) {
            edtPassword.setError("Enter Password");
            validation++;

        } else if (!isValidPassword(edtPassword.getText().toString())) {
            edtPassword.setError("Please enter correct password");
            validation++;
        }
    }
    private void mt(String text) {
        Toast.makeText(LoginAct.this, text, Toast.LENGTH_SHORT).show();
    }
}
