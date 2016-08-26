package com.omsk.bitnic.fatpig;

import android.*;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import orm.Configure;


public class MyServiceGeo extends Service {
    public MyServiceGeo() {
    }

    protected LocationManager locationManager;
    private Settings mSettings;
    MyLocationListener listener;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        new Configure(getApplicationInfo().dataDir + "/ion100.sqlite", getApplicationContext(),true);

        locationManager = (LocationManager) getSystemService(getBaseContext().LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

        }
        listener = new MyLocationListener();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        }


        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        }


        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {


            }
            locationManager.removeUpdates(listener);
        } catch (Exception ex) {
            String msg = "onDestroy geo servis" + ex.getMessage();

            Configure.getSession().insert(msg);
        }
        super.onDestroy();
    }


    class MyLocationListener implements android.location.LocationListener {

        int anInt = 0;
        @Override
        public synchronized void onLocationChanged(Location location) {

            if(anInt++<10) return;
            anInt=0;
            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            intent.putExtra(MainActivity.PARAM_LATITUDE, location.getLatitude());
            intent.putExtra(MainActivity.PARAM_LONGITUDE, MainActivity.PARAM_LONGITUDE);
            sendBroadcast(intent);
            sendBroadcast(intent);
            Log.d("ZZZZZZZZZZZZZZZZ",String.valueOf(1111));
            Log.d("ZZZZZZZZZZZZZZZZ",String.valueOf(11111111));

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
