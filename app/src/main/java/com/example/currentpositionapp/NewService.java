package com.example.currentpositionapp;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NewService extends Service
{

	private MyDBHelper dbHelper;
	private MyDBHelper dHelper;
	private ArrayList<String> vCode;
	private String[] arr;
	private OnlineThread9 thread9;
	private String date = "";
	private String time = "";
	private String lat = "";
	private String log = "";
	private String location = "";
	private String speed = "";
	private int k = 0, i = 0;
	private Cursor cursor1;
	private SQLiteDatabase base;

	@Override
	public void onStart(Intent intent, int startId)
	{

		Log.e("NewService ","Called");
		// super.onStart(intent, startId);
		// mt("inside service");
		vCode = new ArrayList<String>();

		dbHelper = new MyDBHelper(NewService.this);
		base = dbHelper.getReadableDatabase();

		cursor1 = base.query("onlineInfo", null, null, null, null, null, null,
				null);
		cursor1.move(0);
		while (cursor1.moveToNext())
		{
			vCode.add(cursor1.getString(0));
		}

		base.close();
		//dHelper = new MyDBHelper(NewService.this);
		//database = dHelper.getWritableDatabase();

		boolean bool = isOnline();
		if (bool == true)
		{
			// mDataLodingStatusMessageView.setText(R.string.data_fetching_progress_in);

			for (int i = 0; i < vCode.size(); i++)
			{
				// mt("bool is true");
				thread9 = new OnlineThread9();
				thread9.execute((Void) null);
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "No Internet connection.",
					Toast.LENGTH_LONG).show();
		}
	}
	public boolean isOnline()
	{

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		boolean result = false;
		if (ni != null)
		{
			if (ni.getState() == NetworkInfo.State.CONNECTED)
			{
				result = true;
			}
		}
		return result;
	}

	public class OnlineThread9 extends AsyncTask<Void, String, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			// try { // Simulate network access.

			String response = "";
			try
			{

				String url = "http://103.241.181.36:8080/AndrFleetApp/OnlineData?vehiclecode=" + vCode.get(i);
				Log.e("url",url);
				i++;
				url = url.replaceAll(" ", "%20");
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);

				HttpResponse execute = client.execute(httpGet);
				InputStream content = execute.getEntity().getContent();

				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(content));

				String s = "";
				while ((s = buffer.readLine()) != null) {
					response += s;
				}

				// System.out.println("The response =>" + response);

				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			} catch (Exception e) {
				System.out.println("Exception occured!!" + e);
				return false;
			}

			if (!(response.equals("No_Data"))) {

				try
				{
					String[] rows = response.split("\\$");

					publishProgress(rows);

				}
					catch (Exception e)
				{
					e.printStackTrace();
				}

				return true;
			}
			else
			{
				return false;
			}

		}

		// TODO: register the new account here. //return true;



		@Override
		protected void onProgressUpdate(String... arg)
		{
			Log.e("onProgressUpdate"," Called");
			// TODO Auto-generated method stub
			super.onProgressUpdate(arg);
			// name = values[0];
			SQLiteDatabase database;
			dHelper = new MyDBHelper(NewService.this);
			database = dHelper.getWritableDatabase();
			
			date = arg[0];
			Log.e("date",date);
			time = arg[1];
            Log.e("time",time);
			lat = arg[2];
            Log.e("lat",lat);
			log = arg[3];
            Log.e("long",log);
			location = arg[4];
            Log.e("location  4444",location);
			speed = arg[5];
            Log.e("speed",speed);
			ContentValues values = new ContentValues();

			// values.put("vehicleCode", vCode.get(k));
			// values.put("vNum", arr[k]);

			values.put("date", date);
			values.put("time", time);
			values.put("lat", lat);
			values.put("lng", log);
			values.put("location", location);
			values.put("speed", speed);
			// db.update("onlineInfo", values, null, null);

			String[] whereArgs = { vCode.get(k) };
			//database.update("onlineInfo", values, "vehicleCode=?", whereArgs);
			database.update("onlineInfo", values, "vehicleCode="+vCode.get(i), whereArgs);
			// long n = db.insert("onlineInfo", null, values);
            Log.e("whereArgs", String.valueOf(database.update("onlineInfo", values, "vehicleCode=?", whereArgs)));
            if (k < vCode.size()) {
				k++;
			}
			// db.close();
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			thread9 = null;
			// showProgress(false);
		//	mt("Updating...");
			// progressDialog.dismiss();
			// mt(date);
			// MyDBHelper helper = new MyDBHelper(NewService.this);
			// SQLiteDatabase data = helper.getReadableDatabase();
			//
			// Cursor curs = data.query("", columns, selection, selectionArgs,
			// groupBy, having, orderBy)
		}
	}
	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private void mt(String text)
	{
		Toast.makeText(NewService.this, text, Toast.LENGTH_LONG).show();
	}
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		//database.close();
		//base.close();
		//cursor1.close();
	}
}
