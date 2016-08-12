package com.omsk.bitnic.fatpig;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

    private final ArrayList<OverlayItem> mOverlayItemList = new ArrayList<>();

    public MyItemizedOverlay(Drawable pDefaultMarker,
                             ResourceProxy pResourceProxy) {
        super(pDefaultMarker, pResourceProxy);

    }

    public GeoPoint getGeoPoint() {
        if (mOverlayItemList.size() != 0) {
            return mOverlayItemList.get(0).getPoint();
        }
        return null;
    }

    public void addItem(GeoPoint p, String snippet) {
        // GeoPoint mGeoPoint = p;
        //String mTitle = "myPoint1";
        // String mSnippet = "myPoint1";
        OverlayItem newItem = new OverlayItem("myPoint1", "myPoint1", p);
        mOverlayItemList.add(newItem);
        populate();
    }

    public void removeAll() {
        mOverlayItemList.removeAll(mOverlayItemList);
        populate();
    }

    @Override
    public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected OverlayItem createItem(int arg0) {
        // TODO Auto-generated method stub
        return mOverlayItemList.get(arg0);
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return mOverlayItemList.size();
    }

}
