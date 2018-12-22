package com.example.currentpositionapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by sushant on 24/7/16.
 */
public class Vehical extends Application {

    private String vehicalName;
    private boolean isSelected;
    Context context;
    String vehNames,vehCodes;

    public String getVehicalName()
    {
        return vehicalName;
    }
    public void setVehicalName(String vehicalName)
    {
        this.vehicalName = vehicalName;
    }
    public boolean isSelected()
    {
        return isSelected;
    }


    public void setSelected(boolean selected)
    {

        isSelected = selected;

    }

    public String getVehNames() {
        return vehNames;
    }

    public void setVehNames(String vehNames) {
        this.vehNames = vehNames;
    }

    public String getVehCodes() {
        return vehCodes;
    }

    public void setVehCodes(String vehCodes) {
        this.vehCodes = vehCodes;
    }
}

