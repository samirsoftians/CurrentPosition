package com.example.currentpositionapp;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

public class MapActivity extends Activity
{
    MapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        MapView mapView = new MapView(this,256);
        mapView= (MapView) findViewById(R.id.osmmap);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        /*mapView.getController().setZoom(10);
        mapView.getController().setCenter(new GeoPoint());
*/
        GeoPoint geoPoint=new GeoPoint(28.704059, 77.102490);


        mapController= (MapController) mapView.getController();
        mapController.setZoom(10);
        mapController.animateTo(geoPoint);


        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.blue_marker);


        OSMDemo itemizedoverlay = new OSMDemo(drawable,this);
        itemizedoverlay.setEnabled(true);

        /*double lat = 39.461078;
        double lng = 2.856445;

        GeoPoint homePoint = new GeoPoint(lat,lng);*/
//        OverlayItem overlayitem = new OverlayItem(quot;Home Sweet Home&quot;, &quot;name&quot;, homePoint);

        OverlayItem overlayItem=new OverlayItem("Vehicle","VEHICLE NO",geoPoint);

        mapView.getController().setZoom(18);
        mapView.getController().setCenter(geoPoint);

        itemizedoverlay.addOverlay(overlayItem);
        mapOverlays.add(itemizedoverlay);


    }
}
