package com.example.currentpositionapp;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class FleetVIew_MainForm extends Activity
{

    AlertDialog alertDialog;
    TextView vehicleintimation,odometer_reading,alerts,violations;

   @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_view__main_form);


        vehicleintimation= (TextView) findViewById(R.id.img1_vehicle_intimation);
        vehicleintimation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(getApplicationContext(),FleetView_VehicleIntimation.class);
                startActivity(intent);
            }
        });

        odometer_reading= (TextView) findViewById(R.id.img2_odometer_reading);
        odometer_reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                           Intent intent=new Intent(getApplicationContext(),FleetView_OdometerReading.class);
                            startActivity(intent);


            }
        });

        alerts= (TextView) findViewById(R.id.img3_alerts);
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                    Intent intent=new Intent(getApplicationContext(),FleetView_Alerts.class);
                    startActivity(intent);


            }
        });


        violations= (TextView) findViewById(R.id.img4_violation);
        violations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                Intent intent=new Intent(getApplicationContext(),FleetView_Alerts.class);
                startActivity(intent);


            }
        });


    }

    @Override
        public void onBackPressed()
        {
            Log.d("Success","BackPress");


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("FleetView Application")
                .setMessage("Are you sure want to exit?")
                .setIcon(R.drawable.fleetview)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {

                            goto_seacrhactivity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    private void goto_seacrhactivity()
    {

                Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
                finish();


    }

    @Override
    protected void onDestroy()
    {


        super.onDestroy();
    }
}
