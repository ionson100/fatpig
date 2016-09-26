package com.omsk.bitnic.fatpig;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import Model.GeoData;
import orm.Column;
import orm.Configure;
import orm.PrimaryKey;
import orm.Table;


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


        new Configure(getApplicationInfo().dataDir + "/ion100.sqlite", getApplicationContext(), false);


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

        if (Utils.isMyServiceRunning(MyServiceGeoRunner.class, getBaseContext())) {
            getBaseContext().stopService(new Intent(getBaseContext(), MyServiceGeoRunner.class));
        }


        return Service.START_STICKY;
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

            List<Ser>  list=Configure.getSession().getList(Ser.class,null);
            if(list.size()!=0){
                Ser ser=list.get(0);
                if(ser.value==false){
                    if (!Utils.isMyServiceRunning(MyServiceGeoRunner.class, getBaseContext())) {
                        getBaseContext().startService(new Intent(getBaseContext(), MyServiceGeoRunner.class));
                    }
                }
            }



        } catch (Exception ex) {
            String msg = "onDestroy geo servis" + ex.getMessage();

            Configure.getSession().insert(msg);
        }
        super.onDestroy();
    }


    class MyLocationListener implements android.location.LocationListener {

        double cur;
        double cur1;
        int anInt = 0;

        @Override
        public synchronized void onLocationChanged(Location location) {


            if (anInt++ < 2) return;
            anInt = 0;

            if(location.getAccuracy()>10) return;
            if(location.getAltitude()==0) return;

            if (BuildConfig.DEBUG) {
                if (cur == location.getLatitude() && cur1 == location.getLongitude())
                    return;
            } else {
                if (cur == location.getLatitude() && cur1 == location.getLongitude())
                    return;
            }


            cur = location.getLatitude();
            cur1 = location.getLongitude();

            GeoData data = new GeoData();
            data.date = location.getTime();
            data.latitude = location.getLatitude();
            data.longitude = location.getLongitude();
            data.trackName = TrackSettings.getCore().trackName;
            data.speed = location.getSpeed();
            data.altitude = location.getAltitude();

            Configure.getSession().insert(data);

            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            List<Parcelable> parcell=new ArrayList<>(1);
            parcell.add(location);
            intent.putParcelableArrayListExtra("ss", (ArrayList<? extends Parcelable>) parcell);
            sendBroadcast(intent);


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
@Table("service")
class Ser{

    @PrimaryKey("id")
    public int id;

    @Column("value")
    public boolean value;
}

