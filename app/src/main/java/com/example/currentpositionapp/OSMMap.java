package com.example.currentpositionapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import java.util.List;
public class OSMMap extends Activity
{
    MapView mapView;
    MapController mapController;
    private double latitude;
    private double longitude;
    String date,time,vname,name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osmmap2);

        //Coming Parameter is from vehicleinfo activity
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        latitude = Double.parseDouble(bundle.getString("lat"));
        Log.i("CPA","In the OSM map method"+latitude);
        longitude = Double.parseDouble(bundle.getString("lng"));
        Log.i("CPA","In the OSM map method"+longitude);
        date = bundle.getString("date");
        Log.i("CPA","In the OSM map method"+date);
        time = bundle.getString("time");
        Log.i("CPA","In the OSM map method"+time);
        vname = bundle.getString("vname");
        Log.i("CPA","In the OSM map method"+vname);
        mapView= (MapView) findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        name=bundle.getString("name");

//        mapView.setTileSource(TileSourceFactory.CYCLEMAP);
//        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
//      mapView.setTileSource(TileSourceFactory.BASE);
//        mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
//      mapView.setTileSource(TileSourceFactory.MAPQUESTAERIAL);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController= (MapController) mapView.getController();
        Log.i("CPA","In the map view"+mapView);

//      GeoPoint geoPoint=new GeoPoint(40.712784,-74.005941);
        GeoPoint geoPoint=new GeoPoint(latitude,longitude);
        Log.i("CPA","In the OSM map method"+latitude);
        Log.i("CPA","In the OSM map method"+longitude);
        mapController.setZoom(8);
        mapController.animateTo(geoPoint);
        Log.i("CPA","In the OSM map method"+geoPoint);
        Log.i("CPA","In the OSsM map method");
       /* SharedPreferences sharedPreferences=getSharedPreferences("map",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        final String name = sharedPreferences.getString("name", "");
        Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();
        editor.clear();
        editor.commit();*/
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.blue_marker);

        OSMDemo itemizedoverlay = new OSMDemo(drawable,this);
        itemizedoverlay.setEnabled(true);

        OverlayItem overlayItem=new OverlayItem("Vehicle"+time,"VEHICLE NO"+date,name,geoPoint);
        mapView.getController().setZoom(18);
        mapView.getController().setCenter(geoPoint);

        itemizedoverlay.addOverlay(overlayItem);
        mapOverlays.add(itemizedoverlay);
        mapView.getOverlays().add(itemizedoverlay);
        mapView.setMultiTouchControls(true);

    }

}
