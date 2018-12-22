package com.example.currentpositionapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class FleetView_VehicleIntimation extends Activity
{

    Spinner sp1,sp2;
    DatePicker from,to;
    EditText fromdate,todate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_view__vehicle_intimation);

        Button submit_vehicleintimation = (Button) findViewById(R.id.button_Submit_VehicleIntimation);

        sp1 = (Spinner) findViewById(R.id.fleetview_vehicleintimation_spinner);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("select");
        arrayList.add("MH12 7254");
        arrayList.add("MH12 1234");
        arrayList.add("MH12 5254");
        arrayList.add("MH12 1634");
        arrayList.add("MH12 8294");
        arrayList.add("MH12 7204");
        arrayList.add("MH12 8234");
        arrayList.add("MH12 7234");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(arrayAdapter);

        /*sp1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
*/
        sp2 = (Spinner) findViewById(R.id.vehiclspinner_comment);
        ArrayList<String> arrayList2 = new ArrayList<String>();
        arrayList2.add("Select");
        arrayList2.add("Not Driving");
        arrayList2.add("Maintence");
        arrayList2.add("Reapiring");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(arrayAdapter2);

       /* fromdate= (EditText) findViewById(R.id.edttxt_formdate);
        from= (DatePicker) findViewById(R.id.datePicker_from);
*/

        submit_vehicleintimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                    new Mytask();

            }
        });



    }

    class Mytask extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog=new ProgressDialog(getApplicationContext());

        @Override
        protected void onPreExecute()
        {

            progressDialog.setMessage("SENDING REQUEST.");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {

            Thread thread=new Thread();

            try {

                for(int i=0;i<100;i++)
                {
                    thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {


            progressDialog.dismiss();

            super.onPostExecute(s);
        }
    }

}
