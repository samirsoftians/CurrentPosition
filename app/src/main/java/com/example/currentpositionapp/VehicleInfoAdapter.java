package com.example.currentpositionapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class VehicleInfoAdapter extends BaseAdapter
{

	// My Lat Lng = Mundhawa
	// End Lat Lng = swargate

	Double mylat=18.532181,mylng=73.933153,endlat=18.501832,endlng=73.863591;

	String distance;

	double ETACAL;

	int pickuptime=7;
	int avgspeed=40;

	Activity context;
	ArrayList<String> vehicleList;
	LayoutInflater inflater;
	ArrayList<String> date;
	ArrayList<String> time;
	ArrayList<String> location;
	ArrayList<String> lat;
	ArrayList<String> lng;
	ArrayList<String> speed;
	ArrayList<Integer> statusArray;
	int size = 0;
	CardView cardView;
	public VehicleInfoAdapter(Activity context, ArrayList<String> vehicleList,
			ArrayList<String> date, ArrayList<String> time,
			ArrayList<String> location, ArrayList<String> lat,
			ArrayList<String> lng, ArrayList<String> speed,ArrayList<Integer> statusArray) {
		this.context = context;
		this.vehicleList = vehicleList;
		inflater = context.getLayoutInflater();
		this.date = date;
		this.time = time;
		this.lat = lat;
		this.lng = lng;
		this.location = location;
		this.speed=speed;
		this.statusArray = statusArray;
	}

    @Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return vehicleList.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		size = date.size();
		ImageView img;
		final TextView txtName;
		TextView txtDate;
		TextView txtTime;
		final TextView txtLocation;
		TextView txtSpeed;
		TextView status,dtmyhome,ETA,dtdest,ETAdest;
        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.new_vehicle_info_custom, null);

		}
		img = (ImageView) convertView.findViewById(R.id.google);
		txtName = (TextView) convertView.findViewById(R.id.vName);
		txtDate = (TextView) convertView.findViewById(R.id.vDate);
		txtTime = (TextView) convertView.findViewById(R.id.vTime);
		txtSpeed = (TextView) convertView.findViewById(R.id.vSpeed);
		txtLocation = (TextView) convertView.findViewById(R.id.vLocation);
		Log.e("VIA",txtLocation.toString());
		status= (TextView) convertView.findViewById(R.id.statusText);
		dtmyhome= (TextView) convertView.findViewById(R.id.distnacehome);
		ETA= (TextView) convertView.findViewById(R.id.ETA);

		dtdest= (TextView) convertView.findViewById(R.id.dtodest);
		ETAdest= (TextView) convertView.findViewById(R.id.desteta);
		cardView=(CardView)convertView.findViewById(R.id.cv_diig);
		txtName.setText(vehicleList.get(position).trim());
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd ");
        Date newDate = null;
        try {
            newDate = spf.parse(date.get(position));
            spf = new SimpleDateFormat("dd MMM yyyy");
            String newDateString = spf.format(newDate);
            txtDate.setText(newDateString);
            //txtDate.setText(date.get(position));
            Log.e("mygoogle",date.get(position));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exception",e.getMessage());
        }
		String loc=getAddress(context,Double.valueOf(lat.get(position)),Double.valueOf(lng.get(position)));

		txtTime.setText(time.get(position));
        Log.e("mygoogle",time.get(position));
		txtSpeed.setText(speed.get(position)+" KMPH");
		txtLocation.setText("Location : "  +loc);
		Log.e("speed vehicle--",time.get(position));
		for (int i = 0; i < speed.size(); i++) {
            if (speed.get(position).contains("0")) {
                cardView.setBackgroundResource(R.color.Lightyellow);
				Calendar c = Calendar.getInstance();
				Date date=new Date();
				Log.e("datetime",date.toString());
                Log.e("myspeed", String.valueOf(speed));
                SharedPreferences sp = context.getSharedPreferences("vehdetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("veh", vehicleList.get(position));
                editor.commit();
            } else
                cardView.setBackgroundResource(R.color.lightGreen);
            Log.e("else","else"+speed.get(position).contains("0"));
        }
		/*SharedPreferences sharedPreferences=context.getSharedPreferences("map", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("name",location.get(position));
		editor.clear();
		editor.commit();*/

		String v_number=vehicleList.get(position).trim();
		//String loc=location.get(position).trim();

		//Calculate the Distnace
	/*	double earthRadius = 3958.75s;
		double dlat=Math.toRadians(mylat-vlat);
		double dlng=Math.toRadians(mylng-vlng);

		double a = Math.sin(dlat/2) * Math.sin(dlat/2) +
				Math.cos(Math.toRadians(mylat)) * Math.cos(Math.toRadians(vlat)) *
						Math.sin(dlng/2) * Math.sin(dlng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		long dist = (long) (earthRadius * c);*/


		/*double earthRadius = 6371000; //meters
		double dLat = Math.toRadians(mylat-vlat);
		double dLng = Math.toRadians(mylng-vlng);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(mylat)) * Math.cos(Math.toRadians(vlat)) *
						Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		float dist = (float) (earthRadius * c);

		int KM= (int) (dist/1000);
		dtmyhome.setText(" "+KM+" Km");
*/
		Log.i("CPA"," VEHICLE LIST "+vehicleList.get(position));
/*
		if (v_number.equals("MH 14 GD 0159"))
		{

			Location startPoint=new Location("locationA");
			startPoint.setLatitude(Double.parseDouble(lat.get(position)));
			startPoint.setLongitude(Double.parseDouble(lng.get(position)));

			Log.i("CPA"," LAT  0159 "+lat.get(position)+" lng :"+lng.get(position));

			Location endPoint=new Location("locationB");
			endPoint.setLatitude(18.731143);
			endPoint.setLongitude(73.680519);
			double distance=startPoint.distanceTo(endPoint);

			int KM= (int) ((distance/1000)*1.1);
			dtmyhome.setText(""+KM+" Km");

			//Calculate the ETA
			ETACAL=KM*60/avgspeed;
			Calendar now=Calendar.getInstance();
			now.add(Calendar.MINUTE, (int) ETACAL);

			SimpleDateFormat simpleDateFormatnew=new SimpleDateFormat("dd-MMM-yyyy HH:mm");

			ETA.setText(""+simpleDateFormatnew.format(now.getTime())+"");

			Location endPoint1=new Location("locationB");
			endPoint1.setLatitude(18.602834);
			endPoint1.setLongitude(73.716162);

			double dest=startPoint.distanceTo(endPoint1);

			int DESTDIST= (int) ((dest/1000)*1.1);

			dtdest.setText(""+DESTDIST+" Km");

			//Calculate the Destination ETA

			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy HH:mm");


			double ETACALDEST = DESTDIST * 60 / avgspeed;
			now.add(Calendar.MINUTE, (int) ETACALDEST);

			ETAdest.setText(""+simpleDateFormat.format(now.getTime()));

			status.setText(" Talegaon  to ETZ ");


		}

		else  if (v_number.equals("MH  14 CW  4659"))
		{

			Location startPoint=new Location("locationA");
			startPoint.setLatitude(Double.parseDouble(lat.get(position)));
			startPoint.setLongitude(Double.parseDouble(lng.get(position)));


			Log.i("CPA"," LAT 4659"+lat.get(position)+" lng :"+lng.get(position));

			Location endPoint=new Location("locationB");
			endPoint.setLatitude(18.4928);
			endPoint.setLongitude(73.8213);
			double distance=startPoint.distanceTo(endPoint);

			int KM= (int) ((distance/1000)*1.1);
			dtmyhome.setText(""+KM+" Km");

			//Calculate the ETA
			ETACAL=KM*60/avgspeed;
			Calendar now=Calendar.getInstance();
			now.add(Calendar.MINUTE, (int) ETACAL);

			SimpleDateFormat simpleDateFormatnew=new SimpleDateFormat("dd-MMM-yyyy HH:mm");
			ETA.setText(""+simpleDateFormatnew.format(now.getTime())+"");

			Location endPoint1=new Location("locationB");
//			endPoint1.setLatitude(18.731143);
			endPoint1.setLatitude(18.6028369);
			endPoint1.setLongitude(73.7161628);
//			endPoint1.setLongitude(73.680519);
			double dest=startPoint.distanceTo(endPoint1);
			int DESTDIST= (int) ((dest/1000)*1.1);
			dtdest.setText(""+DESTDIST+" Km");

			//Calculate the Destination ETA

			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy HH:mm");
			double ETACALDEST = DESTDIST * 60 / avgspeed;
			now.add(Calendar.MINUTE, (int) ETACALDEST);

			ETAdest.setText(""+simpleDateFormat.format(now.getTime()));
			status.setText(" Karvenagar to ETZ ");

		}
		else
		{


			Location startPoint=new Location("locationA");
			startPoint.setLatitude(Double.parseDouble(lat.get(position)));
			startPoint.setLongitude(Double.parseDouble(lng.get(position)));


			Log.i("CPA"," LAT "+lat.get(position)+" lng :"+lng.get(position));

			Location endPoint=new Location("locationB");
			endPoint.setLatitude(20.4928);
			endPoint.setLongitude(74.8213);
			double distance=startPoint.distanceTo(endPoint);

			int KM= (int) ((distance/1000)*1.1);
			dtmyhome.setText(""+KM+" Km");

			//Calculate the ETA
			ETACAL=KM*60/avgspeed;
			Calendar now=Calendar.getInstance();
			now.add(Calendar.MINUTE, (int) ETACAL);

			SimpleDateFormat simpleDateFormatnew=new SimpleDateFormat("dd-MMM-yyyy HH:mm");
			ETA.setText(""+simpleDateFormatnew.format(now.getTime())+"");


			Location endPoint1=new Location("locationB");
	*//*		endPoint1.setLatitude(21.731143);
			endPoint1.setLongitude(24.680519);
*//*
			endPoint1.setLatitude(18.6028369);
			endPoint1.setLongitude(73.7161628);

			double dest=startPoint.distanceTo(endPoint1);

			int DESTDIST= (int) ((dest/1000)*1.1);

			dtdest.setText(""+DESTDIST+" Km");

			//Calculate the Destination ETA

			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy HH:mm");


			double ETACALDEST = DESTDIST * 60 / avgspeed;
			now.add(Calendar.MINUTE, (int) ETACALDEST);

			ETAdest.setText(""+simpleDateFormat.format(now.getTime()));

			status.setText(" Talegaon  to ETZ ");


		}*/

		switch (statusArray.get(position))
		{
		case 0:
			// convertView.setBackgroundColor(Color.WHITE);
			//convertView.setBackgroundColor(Color.rgb(255, 255, 204));
			break;

		default:
			// convertView.setBackgroundColor(context.getResources().getColor(R.color.LawnGreen));
			//convertView.setBackgroundColor(Color.rgb(204, 255, 204));
			break;
		}
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
                if (networkInfo != null && networkInfo.isConnected()) {
					Log.e("vehicle Info",txtLocation.getText().toString());
					//if (date.get(0) == null)
                    if ((txtLocation.getText().toString()).contains("Location location")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                context);
                        builder.setIcon(R.drawable.fleetlogo);
                        builder.setTitle("Fleetview App");
                        builder.setMessage("No data found.Please click REFRESH button to open google map.");
                        builder.setCancelable(false);
                        DialogInterface.OnClickListener detailsListener = new DetailsDialog();
                        builder.setPositiveButton("Ok", detailsListener);
                        builder.setNeutralButton("Cancel", detailsListener);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Intent intent = new Intent(context, NewGoogle.class);
                        intent.putExtra("lat", lat.get(position));
                        intent.putExtra("lng", lng.get(position));
                        intent.putExtra("date", date.get(position));
                        intent.putExtra("time", time.get(position));
                        Log.e("time.get(POsition)",time.get(position));
						intent.putExtra("speed", speed.get(position));
                        intent.putExtra("vname", vehicleList.get(position));
                        intent.putExtra("location",location.get(position));
                        context.startActivity(intent);
                    }
                }
				else
				{
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            context);
                    builder.setIcon(R.drawable.fleetlogo);
                    builder.setTitle("Fleetview App");
                    builder.setMessage("Please check internet Connection.");
                    builder.setCancelable(false);
                    DialogInterface.OnClickListener detailsListener = new DetailsDialog();
                    builder.setPositiveButton("Yes", detailsListener);
                    builder.setNeutralButton("Cancel", detailsListener);
                    AlertDialog dialog = builder.create();
                    dialog.show();
					/*intent.putExtra("lat", lat.get(position));
					intent.putExtra("lng", lng.get(position));
					intent.putExtra("date", date.get(position));
					intent.putExtra("time", time.get(position));
					intent.putExtra("vname", vehicleList.get(position));
					context.startActivity(intent);
					*/
//					Intent intent = new Intent(context,OSMMap.class);
//					Intent intent = new Intent(context,MapActivity.class);



					/*Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
							Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=73.933153,73.933153"));
					context.startActivity(intent);
*/

					// Implicit Intent to open the gooogle map

					/*String title=" Current Position of Vehicle ";
					String geoUri = "http://maps.google.com/maps?q=loc:" + lat.get(position) + "," + lng.get(position) + " ("  + vehicleList.get(position) + ")";
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
					context.startActivity(intent);
*/

				}
			}
		});
		return convertView;
	}

	class DetailsDialog implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if (which == DialogInterface.BUTTON_POSITIVE) {
				dialog.dismiss();
			}
			if (which == DialogInterface.BUTTON_NEUTRAL) {
				dialog.dismiss();
			}

		}
	}

		public String getAddress(Context context, double LATITUDE, double LONGITUDE) {
			String address=" ";
			//Set Address
			try {
				Geocoder geocoder = new Geocoder(context, Locale.getDefault());
				List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
				if (addresses != null && addresses.size() > 0) {



					address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
					String city = addresses.get(0).getLocality();
					String state = addresses.get(0).getAdminArea();
					String postalCode = addresses.get(0).getPostalCode();
					String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

				}
			} catch (IOException e) {
				Log.e("ss",e.getMessage());
				e.printStackTrace();
			}
			return address;
		}
	}

