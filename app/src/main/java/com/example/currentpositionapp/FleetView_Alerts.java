package com.example.currentpositionapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FleetView_Alerts extends Activity
{

        Button today,yestrday,lastmonth,lastweek;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_view__alerts);


        today= (Button) findViewById(R.id.button_today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


            }
        });

        yestrday= (Button) findViewById(R.id.button_yestrday);
        yestrday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        lastmonth= (Button) findViewById(R.id.button_lastmonth);
        lastmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });

        lastweek= (Button) findViewById(R.id.button_Lastweek);
        lastweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {




            }
        });
    }
}
