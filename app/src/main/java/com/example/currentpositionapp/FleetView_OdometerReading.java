package com.example.currentpositionapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class FleetView_OdometerReading extends Activity
{

    Spinner vehicleno,drivername;
    EditText current_date,current_time,odometer_entry,remark;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_view__odometer_reading);

        vehicleno= (Spinner) findViewById(R.id.spinner_vehicleno);
        ArrayList<String> arrayList=new ArrayList<String>();
        arrayList.add("MH12 1022");
        arrayList.add("MH14 2345");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleno.setAdapter(arrayAdapter);



        drivername= (Spinner) findViewById(R.id.spinner_driver);
        ArrayList<String> arrayList1=new ArrayList<String>();
        arrayList1.add("MAHESH SHARMA");
        arrayList1.add("BUNTY PAKHALE");
        arrayList1.add("JHON ELINSA");
        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drivername.setAdapter(arrayAdapter1);







    }
}
