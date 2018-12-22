package com.example.currentpositionapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sushant on 17/11/16.
 */
public class CustomAdapter extends BaseAdapter
{
    Context context;
    String[] arr;
    int[] imageid;

    public CustomAdapter(Context applicationContext, String[] arr, int[] imageid)
    {

        context=applicationContext;
        this.arr=arr;
        this.imageid=imageid;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View grid;
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null)
        {

            Log.d("GETVIEW ","GV");
            Log.i("in the getview","GETVIEW");
            grid=new View(context);
            grid= inflater.inflate(R.layout.gridxml,null);
            TextView textView= (TextView) grid.findViewById(R.id.fleetview_textView3);
            ImageView imageView= (ImageView) grid.findViewById(R.id.flletview_imageView);

            textView.setText(arr[position]);
            imageView.setImageResource(imageid[position]);
        }
        else
        {

            grid=convertView;
        }

        return grid;
    }
}
