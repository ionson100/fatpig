package com.omsk.bitnic.fatpig;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.http.HttpClientFactory;
import org.osmdroid.http.IHttpClientFactory;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import Transceiver.Transceiver;


public class FMap extends Fragment {


    private Settings mSettings;
    private MapView mMapView;
    private MyItemizedOverlay myItemizedOverlay = null;

    public FMap() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSettings = Settings.getSettings();





        View view = inflater.inflate(R.layout.fragment_fmap, container, false);


            mMapView = (MapView) view.findViewById(R.id.mapview);
            mMapView.setBuiltInZoomControls(true);
            mMapView.setClickable(true);
            Drawable marker = getResources().getDrawable(android.R.drawable.star_big_on);

            int markerWidth = marker.getIntrinsicWidth();
            int markerHeight = marker.getIntrinsicHeight();
            marker.setBounds(0, markerHeight, markerWidth, 0);
            ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getActivity().getApplicationContext());
            myItemizedOverlay = new MyItemizedOverlay(marker, resourceProxy);



               // mMapView.setBuiltInZoomControls(false);

                mMapView.setBuiltInZoomControls(true);


            mMapView.setMultiTouchControls(true);
            mMapView.setClickable(true);
        HttpClientFactory.setFactoryInstance(new IHttpClientFactory() {
            @Override
            public org.apache.http.client.HttpClient createHttpClient() {
                final DefaultHttpClient client = new DefaultHttpClient();
                client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
                return client;
            }
        });

            mMapView.setTileSource(TileSourceFactory.MAPNIK);
            mMapView.getController().setZoom(16);
            mMapView.getOverlays().add(myItemizedOverlay);

            GeoPoint myPoint1 = new GeoPoint(56.819715, 60.640623);


            myItemizedOverlay.addItem(myPoint1, "myPoint1");

            mMapView.getController().animateTo(myPoint1);
            mMapView.invalidate();


/////////////////////////////////////////////////////////////центрирование в центре экрана
            final boolean[] run = {true};

            mMapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    run[0] = false;
                    return false;
                }
            });

            ViewTreeObserver vto = view.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (run[0]) {
                        GeoPoint myPoint1 = new GeoPoint(56.819715, 60.640623);

                    }
                }
            });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mMapView.getOverlays().remove(myItemizedOverlay);
        mMapView.getTileProvider().clearTileCache();
        myItemizedOverlay = null;
        mMapView = null;
        System.gc();
    }
}
