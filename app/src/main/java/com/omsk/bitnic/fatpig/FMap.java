package com.omsk.bitnic.fatpig;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.http.HttpClientFactory;
import org.osmdroid.http.IHttpClientFactory;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;

import java.util.ArrayList;
import java.util.List;

import Model.GeoData;
import orm.Configure;


public class FMap extends Fragment {


    private Settings mSettings;
    private MapView mMapView;
    private MyItemizedOverlay myItemizedOverlay = null;
    private List<GeoData> mGeoDatas = new ArrayList<>();
    private MyBroadcastReceiver broadcastReceiver;

    public FMap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSettings = Settings.getSettings();


        View view = inflater.inflate(R.layout.fragment_fmap, container, false);


        mMapView = (MapView) view.findViewById(R.id.mapview);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setClickable(true);
        Drawable marker = getResources().getDrawable(R.drawable.ic_point2);

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


        if (Settings.getSettings().getSateSystem() == StateSystem.MAP) {
            if (TrackSettings.getCore().statusTrack.equals("1")) {
                mGeoDatas = Configure.getSession().getList(GeoData.class, " track_name = " + TrackSettings.getCore().trackName);
                List<GeoPoint> pointList = new ArrayList<>(mGeoDatas.size());
                for (GeoData gd : mGeoDatas) {
                    pointList.add(new GeoPoint(gd.latitude, gd.longitude));
                }
                for (int i = 0; i < pointList.size(); i++) {
                    myItemizedOverlay.addItem(pointList.get(i), "");
                }
                if (pointList.size() > 0) {
                    mMapView.getController().animateTo(pointList.get(0));
                }
                mMapView.invalidate();
            } else {
                GeoPoint myPoint1 = new GeoPoint(56.819715, 60.640623);
                myItemizedOverlay.addItem(myPoint1, "myPoint1");
                mMapView.getController().animateTo(myPoint1);
                mMapView.invalidate();
            }
        } else {
            if (Settings.getSettings().trackshow == null) {
                GeoPoint myPoint1 = new GeoPoint(56.819715, 60.640623);
                myItemizedOverlay.addItem(myPoint1, "myPoint1");
                mMapView.getController().animateTo(myPoint1);
                mMapView.invalidate();
            } else {
                mGeoDatas = Settings.getSettings().trackshow.list;
                if (mGeoDatas != null && mGeoDatas.size() > 0) {
                    PathOverlay myPath = new PathOverlay(Color.RED, getActivity());
                    List<GeoPoint> pointList = new ArrayList<>(mGeoDatas.size());
                    for (GeoData geoData : mGeoDatas) {
                        pointList.add(new GeoPoint(geoData.latitude, geoData.longitude));
                    }
                    for (GeoPoint point : pointList) {
                        myPath.addPoint(point);
                    }
                    mMapView.getOverlays().add(myPath);
                    if (pointList.size() > 0) {
                        mMapView.getController().animateTo(pointList.get(0));
                    }
                    mMapView.invalidate();
                } else {
                    GeoPoint myPoint1 = new GeoPoint(56.819715, 60.640623);
                    myItemizedOverlay.addItem(myPoint1, "myPoint1");
                    mMapView.getController().animateTo(myPoint1);
                    mMapView.invalidate();
                }
            }


        }


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

        initBrodcast();

        return view;
    }

    private void initBrodcast() {

        if (TrackSettings.getCore().statusTrack.equals("1") && Settings.getSettings().getSateSystem() == StateSystem.MAP) {
            if (!Utils.isMyServiceRunning(MyServiceGeo.class, getActivity())) {
               Utils.start();
                getActivity().startService(new Intent(getContext(), MyServiceGeo.class));
            }

            broadcastReceiver = new MyBroadcastReceiver();
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(MainActivity.BROADCAST_ACTION));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mMapView.getOverlays().remove(myItemizedOverlay);
        mMapView.getTileProvider().clearTileCache();
        myItemizedOverlay = null;
        mMapView = null;
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }

        System.gc();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {


            double langitude = intent.getDoubleExtra(MainActivity.PARAM_LATITUDE, 0);
            double longitude = intent.getDoubleExtra(MainActivity.PARAM_LONGITUDE, 0);
            long date = intent.getLongExtra(MainActivity.PARAM_DATE, 0);
            GeoData geoData = new GeoData();
            geoData.latitude = langitude;
            geoData.longitude = longitude;
            geoData.date = date;
            mGeoDatas.add(geoData);
            GeoPoint point = new GeoPoint(langitude, longitude);
            myItemizedOverlay.addItem(point, "");

            mMapView.getController().animateTo(point);
            mMapView.invalidate();
            Log.d("ZZZZZZZZZZZZZZZZ", String.valueOf(longitude));
            Log.d("ZZZZZZZZZZZZZZZZ", String.valueOf(langitude));
        }
    }
}
