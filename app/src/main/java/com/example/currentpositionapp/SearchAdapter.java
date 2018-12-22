package com.example.currentpositionapp;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sushant on 24/7/16.
 */
public class SearchAdapter extends BaseAdapter
{

    private Activity activity;
    private ArrayList<Vehical> vehicals;
    Context context;


    public SearchAdapter(Activity activity, ArrayList<Vehical> vehicals)
    {
        this.activity = activity;
        this.vehicals = vehicals;
    }
    @Override
    public int getCount()
    {
        return vehicals.size();
    }

    @Override
    public Object getItem(int position)
    {
        return vehicals.get(position);
    }

    @Override
    public long getItemId(int position)

    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Vehical vehical = vehicals.get(position);
        ViewHolder viewHolder = null;

        if(convertView==null)
        {
            viewHolder = new ViewHolder();
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item,null);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvVehicalName = (TextView) convertView.findViewById(R.id.vehicle_name);
        viewHolder.tvVehicalName.setText(vehical.getVehicalName());
        if(vehical.isSelected()){
            viewHolder.tvVehicalName.setTextColor(Color.RED);
        }
        else
        {
            viewHolder.tvVehicalName.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void setSelectedItem(int position,boolean isSelected)
    {

        vehicals.get(position).setSelected(isSelected);
        this.notifyDataSetInvalidated();
       // if(getArrayList("list").equals(true));
    }

    public boolean isItemSelected(int position)
    {
        return vehicals.get(position).isSelected();

    }
    public class ViewHolder
    {

        TextView tvVehicalName;
    }
}
