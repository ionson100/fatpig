package com.omsk.bitnic.fatpig;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.PathOverlay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Model.GeoData;
import orm.Configure;

import static com.omsk.bitnic.fatpig.R.id.mapView;


public class FMap extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {


    private MapView mMapView;


    private MyBroadcastReceiver broadcastReceiver;
    private List<GeoPoint> pointList;
    private PathOverlay myPath;
    private boolean[] run = {true};
    private ViewTreeObserver vto;
    private List<GeoData> mGeoDatas=new ArrayList<>();

    public FMap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fmap, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapview);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setClickable(true);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mMapView.setClickable(true);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.getController().setZoom(16);

        if (Settings.getSettings().getSateSystem() == StateSystem.MAP) {
            if (TrackSettings.getCore().statusTrack.equals("1")) {
                 mGeoDatas = Configure.getSession().getList(GeoData.class, " track_name = " + TrackSettings.getCore().trackName);
                pointList = new ArrayList<>(mGeoDatas.size());
                for (GeoData gd : mGeoDatas) {
                    double d1 = gd.latitude, d2 = gd.longitude;
                    GeoPoint geoPoint = new GeoPoint(d1, d2);
                    pointList.add(geoPoint);
                }
                myPath = new PathOverlay(Color.RED, getActivity());
                for (GeoPoint point : pointList) {
                    myPath.addPoint(point);
                }
                mMapView.getOverlays().add(myPath);

            }
        } else {

             mGeoDatas = Settings.getSettings().trackshow.list;
                if (mGeoDatas != null && mGeoDatas.size() > 0) {
                    myPath = new PathOverlay(Color.RED, getActivity());
                    pointList = new ArrayList<>(mGeoDatas.size());
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

                }

        }

        mMapView.invalidate();
///////////////////////////////////////////////////////////центрирование в центре экрана

        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                run[0] = false;
                return false;
            }
        });

        vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);
        /////////////////////////////
        initBrodcast();
        return view;
    }

    @Override
    public void onGlobalLayout() {
        if (run[0]) {
            if (pointList != null && pointList.size() > 0) {
                mMapView.getController().setCenter(pointList.get(0));
            }

        }
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (broadcastReceiver != null) {
                getActivity().unregisterReceiver(broadcastReceiver);
            }
            mMapView.getOverlays().remove(myPath);
            mMapView.getTileProvider().clearTileCache();
//            mMapView = null;
            if(vto!=null&&vto.isAlive()){
                vto.removeOnGlobalLayoutListener(this);

            }
            vto=null;


        } catch (Exception ignored) {

        } finally {
            System.gc();
        }


    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            double langitude = intent.getDoubleExtra(MainActivity.PARAM_LATITUDE, 0);
//            double longitude = intent.getDoubleExtra(MainActivity.PARAM_LONGITUDE, 0);
//            GeoData  geoData=new GeoData();
//            geoData.altitude=langitude;
//            geoData.longitude=longitude;
//            mGeoDatas.add(geoData);
//
////            int dynamicOverlayIndex = mMapView.getOverlays().size();
////            mMapView.getOverlays().remove(dynamicOverlayIndex-1);
//            mMapView.getOverlays().remove(myPath);
//           // mMapView.invalidate();
//            mMapView.getOverlayManager().clear();
//            mMapView.invalidate();
//
//            pointList = new ArrayList<>(mGeoDatas.size());
//            for (GeoData gd : mGeoDatas) {
//                double d1 = gd.latitude, d2 = gd.longitude;
//                GeoPoint geoPoint = new GeoPoint(d1, d2);
//                pointList.add(geoPoint);
//            }
//            myPath = new PathOverlay(Color.RED, getActivity());
//            for (GeoPoint point : pointList) {
//                myPath.addPoint(point);
//            }
//
//            List<Overlay> getOver=mMapView.getOverlays();
//
//            mMapView.getOverlays().add(myPath);
//
//
//
//            mMapView.invalidate();


//            GeoPoint point = new GeoPoint(langitude, longitude);
//            PathOverlay dds=new PathOverlay(Color.RED, getActivity());
//            dds.addPoint(point);
//            mMapView.getOverlays().clear();
//            mMapView.invalidate();
//            myPath.addPoint(point);
//            myPath. clearPath();
//            mMapView.getOverlays().add(myPath);









        }
    }
}
