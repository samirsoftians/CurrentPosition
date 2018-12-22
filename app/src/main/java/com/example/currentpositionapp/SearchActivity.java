

package com.example.currentpositionapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twtech.library.TokenCompleteTextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements TokenCompleteTextView.TokenListener<VehicleParsing>
{

    int count=0;
    public static String veh;
    ListView searchList;
    private ArrayList<Vehical> vehicals;
    EditText edtSearch;
    SearchAdapter searchAdapter;
    ArrayList<String> selectedList = null;
    int i = 1;
    Button btnGo;
    SharedPreferences pref;
    DisplayMetrics metrics;
    private LinearLayout layoutMapView;
    private LinearLayout.LayoutParams llp;
    private LinearLayout.LayoutParams blp;
    private LinearLayout layoutMapViewButtons;
    private LinearLayout.LayoutParams llpButtons;
    int screenHeight;
    int screenWidth;
    TextView tvVehicalName;
    ProgressDialog myPd_ring;
    private View currentSelectedView;
    Typeface font;
    private ImageButton imgBtnMapView;
    String[] arr = null;
    ArrayList<Integer> vCode = null;
    int new_vehiclelimit=0;
    MultiAutoCompleteTextView actv;
    SharedPreferences sharedPreferences;
    VehicleParsing[] people;
    ArrayAdapter<VehicleParsing> adapter;
    String name;

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
           // Toast.makeText(getApplicationContext(), "Inside Broadcast", Toast.LENGTH_SHORT).show();
            PendingIntent service = null;
            Intent intentForService = new Intent(context.getApplicationContext(), MyService.class);
            final AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            final Calendar time = Calendar.getInstance();
            time.set(Calendar.MINUTE, 0);
            time.set(Calendar.SECOND, 0);
            time.set(Calendar.MILLISECOND, 0);
            if (service == null)
            {
                service = PendingIntent.getService(context, 0,
                        intentForService, PendingIntent.FLAG_CANCEL_CURRENT);
            }
            alarmManager.setRepeating(AlarmManager.RTC, time.getTime()
                    .getTime(), 600000, service);
        }
    };
    IntentFilter filter = new IntentFilter("action.broadcast");
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //Receving the value from the vechile limit activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boolean serviceRunningStatus = isServiceRunning(MyService.class);
        // boolean serviceUpdateRunningStatus = isServiceRunning(UpdateRemoteCanParameter.class);

        if (serviceRunningStatus == false) {
            Log.e("Service ", "is not Running");
            startService(new Intent(SearchActivity.this, MyService.class));
        }

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlay);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();
        selectedList = new ArrayList<String>();
        vCode = new ArrayList<Integer>();
        tvVehicalName = (TextView) findViewById(R.id.vehicle_name);

        getList();
        // getCode();
        init();
        sharedPreferences = getSharedPreferences("vehicle", MODE_PRIVATE);
        String dt2 = sharedPreferences.getString("nameVehicle", null);
       // arr=dt2.split("$");
        String[] arr=dt2.split(",");
       // people = new VehicleParsing[]{new VehicleParsing("shivi"),new VehicleParsing("shivankchi")};
        ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.select_dialog_item,arr);
        actv = (MultiAutoCompleteTextView) findViewById(R.id.inputSearch);
        actv.setThreshold(0);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);
        actv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        actv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                count=0;

                veh= String.valueOf(parent.getItemAtPosition(position));
                //Toast.makeText(SearchActivity.this, veh, Toast.LENGTH_SHORT).show();
                Log.e("veh",veh);
                if(searchAdapter.isItemSelected(position))
                {

                    searchAdapter.setSelectedItem(position,false);
                    selectedList.remove(((Vehical)searchAdapter.getItem(position)).getVehicalName());
                }
                else
                {

                    //if(selectedList.size()<new_vehiclelimit){
                        SQLiteDatabase db4;
                        MyDBHelper helper = new MyDBHelper(SearchActivity.this);
                        db4 = helper.getReadableDatabase();
                        Cursor c = db4.query("vehicleInfo", null, null, null, null, null, null);
                        c.move(0);
                        while (c.moveToNext())
                        {
                            String m1=c.getString(1);
                            String m2=veh;
                            int a=m1.compareTo(m2);

                            if(a==0)
                            {
                                searchAdapter.setSelectedItem(count,true);
                                selectedList.add(((Vehical)searchAdapter.getItem(count)).getVehicalName());

                            }
                            count++;

                        }
                        c.close();
                        db4.close();
                    }
                    /*else{
                        Toast.makeText(getApplicationContext(), "Vehicle Limit is Over", Toast.LENGTH_SHORT).show();
                    }*/
                }

        });


        /*code*/


        /*code*/


        searchList = (ListView) findViewById(R.id.list_view);
       // edtSearch = (EditText) findViewById(R.id.inputSearch);
        btnGo = (Button) findViewById(R.id.sendList);
        searchAdapter = new SearchAdapter(this,vehicals);
        searchList.setAdapter(searchAdapter);
        searchList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        pref = getSharedPreferences("vList", Activity.MODE_PRIVATE);
        //SharedPreferences.Editor editor = pref.edit();
        //editor.putInt("vehiclelimit", VLimit.value_vlimit);
        //editor.commit();


        new_vehiclelimit=pref.getInt("vehiclelimit", 5);

      /*  edtSearch.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
                // TODO Auto-generated method stub
//          SearchActivity.this.searchAdapter.getFilter().filter(s);



            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {
                // TODO Auto-generated  method stub

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub

            }
        });*/
        searchList.setOnItemClickListener(

                new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long arg3) {

                        if(searchAdapter.isItemSelected(position))
                        {
                            searchAdapter.setSelectedItem(position,false);
                            selectedList.remove(((Vehical)searchAdapter.getItem(position)).getVehicalName());
                        }
                        else
                        {
                            if(selectedList.size()<new_vehiclelimit)
                            {


                                searchAdapter.setSelectedItem(position,true);
                                selectedList.add(((Vehical)searchAdapter.getItem(position)).getVehicalName());
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Vehicle Limit is Over", Toast.LENGTH_SHORT).show();
                            }
                        }

                  /*if ((view.isEnabled() == false))
                  {   // for higher version// APsK*//**//*
                     //


                     ((TextView) view).setTextColor(Color.BLACK);

                     Toast.makeText(SearchActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();

                                 String item = ((TextView) view).getText().toString();

                     selectedList.remove(item);

                     adapter.notifyDataSetChanged();

                     view.setEnabled(true);

                     i--;


                  }
                  else
                  {


                     if (i <= VLimit.value_vlimit)
                     {





                           ((TextView) view).setTextColor(Color.RED);

                           view.setEnabled(true);

                           adapter.notifyDataSetChanged();



                        //adapter.notifyDataSetChanged();

                        *//*if(position==position+14)

                           ((TextView) view).setTextColor(Color.BLACK);
                        }*//*


                        //searchList.setItemsCanFocus(true);

                        //((TextView) view).setTextColor(Color.RED);
                        //view.setEnabled(false);

                        //view= (View) searchList.getSelectedItem();


                        *//*if(view!=searchList)
                        {
                           ((TextView) view).setTextColor(Color.RED);
                           view.setEnabled(false);
                        }*//*

                        *//*for(int j=0;j<=selectedList.size();j++) {

                           ((TextView) view).setTextColor(Color.RED);
                           view.setEnabled(false);
                        }*//*

                        //view.setBackgroundColor(Color.RED);


                        //Toast.makeText(SearchActivity.this, "Selected" + position, Toast.LENGTH_SHORT).show();

                     *//*View v;
                     v.setBackgroundResource(0);
                     view.setBackgroundResource(R.color.green);
                     v = view;*//*

                        String item = ((TextView) view).getText().toString();

                        selectedList.add(item);

                        adapter.notifyDataSetChanged();

                        i++;





                     } else {


                        Toast.makeText(getApplicationContext(), "Vechicle Limit is Over", Toast.LENGTH_SHORT).show();

                     }


                  }
*/

                        // view.setVisibility(70);

            /*s
             * Drawable background = view.getBackground(); if (background
             * instanceof ColorDrawable) color = ((ColorDrawable)
             * background).getColor();
             *
             * if(color == Color.RED){ view.setBackgroundColor(Color.WHITE);
             * }
             */

                        // Toast.makeText(SearchActivity.this,
                        // `es.get(position), Toast.LENGTH_SHORT).show();
                        // view.setBackgroundColor(Color.RED);
                    }

                });

        btnGo.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View v) {



                if (i == 0)
                {

                    Log.i("CPA","In the btngo i==0");


                    // SharedPreferences sp = getSharedPreferences("vList",
                    // Activity.MODE_PRIVATE);
                    // Set<String> set2 = sp.getStringSet("list", null);
                    // ArrayList<String> vlist = new ArrayList<String>(set2);

                    Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                    // intent.putExtra("list", vlist);
                    startActivity(intent);
                    finish();
                    //registerReceiver(receiver, filter);
                  /*
                * MyDBHelper helper = new MyDBHelper(SearchActivity.this);
                * SQLiteDatabase data = helper.getReadableDatabase();
                *
                * Cursor curs = data.query("onlineInfo", null, null, null,
                * null, null, null); curs.move(0); while(curs.moveToNext())
                * { mt(curs.getString(1) + " " + curs.getString(2) + " "
                * +curs.getString(7)); } curs.close(); data.close();
                */
                }
                else
                {

//code from here
                    myPd_ring= ProgressDialog.show(SearchActivity.this, "", "Please Wait.....", true);
                    myPd_ring.setCancelable(true);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            myPd_ring.dismiss();

                            finish();

                            //startActivity(new Intent(SearchActivity.this,VehicleInfoActivity.class));

                            Log.i("CPA","In the else loop");

                     /*code begins from here*/

                           /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
                            SharedPreferences.Editor ed = prefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(selectedList);
                            ed.putString("list", json);
                            ed.apply();*/

                     /*ends here*/


                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("count", selectedList.size());

                            for (int i = 0; i < selectedList.size(); i++)
                            {

                                editor.putString("vehicle" + i, selectedList.get(i));
                            }
                            //registerReceiver(receiver, filter);
                            // editor.putStringSet("list", set);
                            editor.commit();
                            //stopService(new Intent(SearchActivity.this,NewService.class));

                            finish();
                            registerReceiver(receiver, filter);
                            Intent intent = new Intent("action.broadcast");
                            sendBroadcast(intent);
                            Intent intent1 = new Intent(SearchActivity.this,VehicleInfoActivity.class);

                            startActivity(intent1);

                            String s=actv.getText().toString();
                            Log.e("string s",s);
                        }
                    }, 5*1000);
                    //till here


                  /*Log.i("CPA","In the else loop");

               SharedPreferences.Editor editor = pref.edit();
               editor.putInt("count", selectedList.size());

               for (int i = 0; i < selectedList.size(); i++)
               {

                              editor.putString("vehicle" + i, selectedList.get(i));
               }
               //registerReceiver(receiver, filter);
               // editor.putStringSet("list", set);
               editor.commit();
               //stopService(new Intent(SearchActivity.this,NewService.class));

               finish();
               registerReceiver(receiver, filter);
               Intent intent = new Intent("action.broadcast");
               sendBroadcast(intent);
               Intent intent1 = new Intent(SearchActivity.this,VehicleInfoActivity.class);

               startActivity(intent1);*/
                }

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

   /*code for highlighting selected item*/


   /*code ends here*/


    @Override
    public void onBackPressed()
    {

        Intent i = new Intent(SearchActivity.this, VehicleInfoActivity.class);
        startActivity(i);
        finish();
    }


    private void init() {

        font = Typeface.createFromAsset(getAssets(), "GOTHIC.TTF");
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
    }

    private void mt(String text)
    {
        Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void getList()
    {
        vehicals = new ArrayList<>();
        SQLiteDatabase db3;
        MyDBHelper helper = new MyDBHelper(SearchActivity.this);
        db3 = helper.getReadableDatabase();
        Cursor c = db3.query("vehicleInfo", null, null, null, null, null, null);
        c.move(0);
        while (c.moveToNext())
        {
            // mt(c.getString(1));
            Vehical vehical = new Vehical();
            vehical.setVehicalName(c.getString(1));
            String nm = c.getString(1);
            Log.e("Values ",": "+nm);
            vehicals.add(vehical);
        }

        c.close();
        db3.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // TODO Auto-generated method stub


        MenuItem item;

        item = menu.add(0, 1, 1, "LogOut");
        //item.setIcon(R.drawable.ic_launcher);
        item = menu.add(0, 2, 2, "Current Vehicle Limit:"+new_vehiclelimit);
        item=menu.add(0,3,3,"Set New Limit");
//    item=menu.add(0,4,4,"FleetView Application ");
      /*SubMenu menu1=menu.addSubMenu("Login");
      SubMenu subMenu=menu.addSubMenu("Vechile Limit");
      subMenu.add("5");
      subMenu.add("10");
      subMenu.add("15");
      subMenu.add("20");
      SubMenu menu2=menu.addSubMenu("EXIT");*/

        //item.setIcon(R.drawable.ic_launcher);
        //item = menu.add(0, 3, 3, "Exit");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub

        if (item.getItemId() == 1)
        {
            SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor e=sp.edit();
            e.clear();
            e.commit();
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Sure to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(SearchActivity.this, LoginAct.class));
                            stopService(new Intent(SearchActivity.this,MyService.class));
                            dialog.dismiss();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.dismiss();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
            //finish();
            //startActivity(new Intent(SearchActivity.this, LoginAct.class));
        } else if (item.getItemId() == 2)
        {

            Toast.makeText(getApplicationContext(),"Current Vehicle Limti is:"+new_vehiclelimit,Toast.LENGTH_LONG).show();

        } else if (item.getItemId() == 3)
        {
            finish();


            Intent intent=new Intent(SearchActivity.this,VLimit.class);
            startActivity(intent);

        }
        else if(item.getItemId()==4)
        {
            finish();
            Intent intent=new Intent(getApplicationContext(),FleetVIew_MainForm.class);
            startActivity(intent);

        }
        return true;
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


   /*//Get the result from VechicleActivity
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      *//*if (requestCode == 1 && data != null)
      {
         Log.v("TAG", data.getStringExtra("value"));
         if(resultCode == RESULT_OK)
         {


            String vlimit=data.getStringExtra("value");

            Toast.makeText(getApplicationContext(),"Vechical Limit :"+vlimit,Toast.LENGTH_LONG).show();


         }
         if (resultCode == RESULT_CANCELED)
         {

         }
      }*//*
      if (requestCode == 2) {
         String vlimit = data.getStringExtra("vechiclelimit");


         Toast.makeText(getApplicationContext(), "Vechicle Limit " + vlimit, Toast.LENGTH_LONG);

         //String data = getActivity().getIntent().getExtras().getString("vechiclelimit");


      }

   }

*/

    //@Override
    protected void onStop()
    {
        // TODO Auto-generated method stub
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Search Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.currentpositionapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        registerReceiver(receiver, filter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);


    }

    public void exit()
    {


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Search Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.currentpositionapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }


    @Override
    public void onTokenAdded(VehicleParsing token) {

    }

    @Override
    public void onTokenRemoved(VehicleParsing token) {

    }

    @Override
    public void onDuplicateRemoved(VehicleParsing token) {

    }
}
/*
 * private void getCode() { int x = listVehicles.size(); arr = new String[x];
 * for (int i = 0; i < x; i++) { arr[i] = listVehicles.get(i); }
 *
 * dbHelper = new MyDBHelper(SearchActivity.this); db =
 * dbHelper.getReadableDatabase(); for (int i = 0; i < arr.length; i++) {
 * String[] selectionArgs = { arr[i] }; Cursor c = db.query("vehicleInfo", null,
 * "vehicleRegNum = ?", selectionArgs, null, null, null); c.move(0); while
 * (c.moveToNext()) { // mt(c.getString(1)); vCode.add(c.getInt(0)); } }
 * db.close(); }
 */
/*
 * private void addButtonToTheMapViewOptions(Button b, LayoutParams blp, String
 * text) { b.setLayoutParams(blp); b.setTextSize(13f); b.setText(text);
 * b.setTextColor(getResources().getColor(R.color.White));
 * b.setBackgroundResource(R.drawable.border); b.setTypeface(font);
 * b.setOnClickListener(new MapViewButtonListener());
 * layoutMapViewButtons.addView(b); }
 *
 * class MapViewButtonListener implements View.OnClickListener {
 *
 * @Override public void onClick(View b) {
 *
 * Button btnMapView = (Button) b; if
 * (btnMapView.getText().toString().equals("Update")) { mt("update");
 * startActivity(new Intent(SearchActivity.this, VehicleInfoActivity.class));
 *
 * } else if (btnMapView.getText().toString().equals("Detail")) {
 * mt("deatails");
 *
 * } else if (btnMapView.getText().toString().equals("Reg.")) {
 * mt("registration");
 *
 * startActivity(new Intent(SearchActivity.this, LoginAct.class)); } else if
 * (btnMapView.getText().toString().equals("Search")) { mt("Search"); }
 *
 * ViewGroup parent = (ViewGroup) b.getParent().getParent() .getParent();
 * parent.removeView(layoutMapView); imgBtnMapView.setEnabled(true);
 *
 * } }
 */

