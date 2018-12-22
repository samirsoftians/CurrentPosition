package com.example.currentpositionapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sushant on 20/1/17.
 */
public class OSMDemo extends ItemizedOverlay<OverlayItem>
{

    static ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private Context mContext;

    public OSMDemo(Drawable pDefaultMarker, Context context)
    {

        super(pDefaultMarker,new DefaultResourceProxyImpl(context));
        mContext=context;

    }

    public void addOverlay(OverlayItem overlay)
    {
        mOverlays.add(overlay);
        populate();

    }

    @Override
    protected OverlayItem createItem(int i)
    {
        return mOverlays.get(0);

    }
    @Override
    public int size()
    {
        return mOverlays.size();
    }
    @Override
    public boolean onSnapToItem(int i, int i1, Point point, IMapView iMapView)
    {


        return false;
    }

    public boolean ontap(int index)
    {

        Log.i("CPA","In the OSMDEMO ONTAP");
        OverlayItem item = mOverlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(item.getTitle());

        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;

    }
}
