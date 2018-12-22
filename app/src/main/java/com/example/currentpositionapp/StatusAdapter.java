package com.example.currentpositionapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StatusAdapter extends BaseAdapter
{
	Activity context;
	ArrayList<String> vehiclesName;
	ArrayList<String> status;
	ArrayList<Integer> speed;
	LayoutInflater inflater;

	public StatusAdapter(Activity context, ArrayList<String> vehiclesName,
			ArrayList<String> status, ArrayList<Integer> speed) {
		this.context = context;
		this.vehiclesName = vehiclesName;
		this.status = status;
		this.speed = speed;
		inflater = context.getLayoutInflater();
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return vehiclesName.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int x = 0;
		//TextView txt;
		//TextView text;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.status_custom, null);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txt = (TextView) convertView.findViewById(R.id.text);
		viewHolder.text = (TextView) convertView.findViewById(R.id.statusText);
		viewHolder.txt.setText(vehiclesName.get(position));
		//viewHolder.text.setTextColor(Color.BLUE);
		if (status.get(position).trim().equals("-")) {
			viewHolder.text.setText("Active");
			convertView.setBackgroundResource(R.color.white);
		} else if (status.get(position).trim().equals("DeActivated")) {
			viewHolder.text.setText("DeActivated");
			convertView.setBackgroundResource(R.color.MistyRose);
			x = 1;
		} else if (status.get(position).trim().equals("Removed")) {
			viewHolder.text.setText(status.get(position));
			convertView.setBackgroundResource(R.color.UILightBlue);
			x = 1;
		} else {
			//convertView.setBackgroundColor(R.color.white);
			convertView.setBackgroundResource(R.color.white);
			x = 1;
		}
		switch (speed.get(position))
		{
		case 0:
			if (x == 0)
			{
				// convertView.setBackgroundColor(Color.LTGRAY);
				//convertView.setBackgroundColor(Color.rgb(255, 255, 204));

			}
			break;
			default:
			if (x == 0)
			{
				// convertView.setBackgroundColor(context.getResources().getColor(R.color.LawnGreen));
				//convertView.setBackgroundColor(Color.rgb(204, 255, 204));
			}
			break;
		}
		return convertView;
	}

	//added by sushant
	public class ViewHolder
	{
		TextView txt;
		TextView text;
	}
}
