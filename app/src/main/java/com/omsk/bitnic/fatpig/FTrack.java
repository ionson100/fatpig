package com.omsk.bitnic.fatpig;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import android.os.SystemClock;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;

import android.widget.Toast;


import java.util.Date;
import java.util.List;

import Model.GeoData;
import Model.User;
import orm.Configure;


public class FTrack extends Fragment implements View.OnClickListener{

   Pupper distance,calories;
   Pupper speed;


    ImageButton mBtRunn;
    ImageButton mBtPause;
    ImageButton mBtStop;

    List<GeoData> mGeoDatas;

    PausableChronometer chronometer;
    View mView;
    User user;


    MyBroadcastReceiver broadcastReceiver;
    private int anInt=0;

    public FTrack() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_ftrack, container, false);

        distance= (Pupper) mView.findViewById(R.id.diastace);
        speed= (Pupper) mView.findViewById(R.id.speed);
        calories= (Pupper) mView.findViewById(R.id.calories);


        mBtRunn = (ImageButton) mView.findViewById(R.id.tarack_run);
        mBtPause = (ImageButton) mView.findViewById(R.id.tarack_pause);
        mBtStop = (ImageButton) mView.findViewById(R.id.tarack_stop);
        mBtRunn.setOnClickListener(this);
        mBtPause.setOnClickListener(this);
        mBtStop.setOnClickListener(this);


        List<User> users=Configure.getSession().getList(User.class,null);
        if(users.size()>0){
            user=users.get(0);
        }else{
            user=null;
        }




        final String tag= TrackSettings.getCore().statusTrack;

        mGeoDatas= Configure.getSession().getList(GeoData.class," track_name = "+TrackSettings.getCore().trackName);
        Toast.makeText(getActivity(),String.valueOf(mGeoDatas.size()), Toast.LENGTH_LONG).show();







        broadcastReceiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter(MainActivity.BROADCAST_ACTION));
        if(TrackSettings.getCore().timeTimeDelta!=0){
            long del=new Date().getTime()- TrackSettings.getCore().timeTimeDelta;
            long dd= TrackSettings.getCore().timeWhenStopped;
            long res= TrackSettings.getCore().timeWhenStopped-del;
           // Log.d("ssssssssssssssss",String.valueOf(res));
            TrackSettings.getCore().timeWhenStopped=res;
            TrackSettings.getCore().timeTimeDelta=0;
            TrackSettings.save();
        }
        activateChrono();
        if(tag.equals("1")){
            start();
        }else if(tag.equals("2")){
            pause();
        }else if(tag.equals("3")){
            stop();
        }else{
            mBtRunn.setEnabled(true);
            mBtPause.setEnabled(false);
            mBtStop.setEnabled(false);
        }


        calculate();
        return mView;
    }
    void activateChrono(){
        chronometer = (PausableChronometer) mView.findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {


                if(anInt++>20){
                    anInt=0;
                    FillData.fill(getActivity());
                }

            }
        });
        chronometer.setFormat("00:%s");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        chronometer.destoryStop();
        getActivity(). unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onClick(View v) {
        String tag= (String) v.getTag();
        if(tag.equals("1")){
            start();
        }else if(tag.equals("2")){
            pause();
        }else if(tag.equals("3")){
            stop();
        }
    }
    void start(){
       boolean first=TrackSettings.getCore().trackName==0;
        chronometer.start();
        mBtRunn.setEnabled(false);
        mBtPause.setEnabled(true);
        mBtStop.setEnabled(true);
        getActivity().startService(new Intent(getContext(), MyServiceGeo.class));
        if(first){
            mGeoDatas= Configure.getSession().getList(GeoData.class," track_name = "+TrackSettings.getCore().trackName);
            Toast.makeText(getActivity(),String.valueOf(mGeoDatas.size()), Toast.LENGTH_LONG).show();
            calculate();
        }
        FillData.fill(getActivity());
    }
    void pause(){
        chronometer.stop();
        mBtRunn.setEnabled(true);
        mBtPause.setEnabled(false);
        mBtStop.setEnabled(true);
        getActivity().stopService(new Intent(getContext(), MyServiceGeo.class));
    }
    void stop(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.reset();
        mBtRunn.setEnabled(true);
        mBtPause.setEnabled(false);
        mBtStop.setEnabled(false);
        getActivity().stopService(new Intent(getContext(), MyServiceGeo.class));
    }

    void calculate(){

        double dis=  Utils.round(Calculation.getDistance(mGeoDatas),3);
        distance.setPairString(getString(R.string.distance), String.valueOf(dis));
        double a= Utils.round(Calculation.getSpeed(mGeoDatas),2);

        if(a>0){
            String ss=Double.toString(a);
            speed.setPairString(getString(R.string.speed),ss);
        }


        double cal=Utils.round(Calculation.getCalories(mGeoDatas,user),2);
        String df=String.valueOf(cal);

        calories.setPairString(getString(R.string.calories),df);
    }



    public class MyBroadcastReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {


            double langitude = intent.getDoubleExtra(MainActivity.PARAM_LATITUDE, 0);
            double longitude = intent.getDoubleExtra(MainActivity.PARAM_LONGITUDE, 0);
            long date = intent.getLongExtra(MainActivity.PARAM_DATE, 0);
            GeoData geoData=new GeoData();
            geoData.latitude=langitude;
            geoData.longitude=longitude;
            geoData.date=date;
            mGeoDatas.add(geoData);
            calculate();



            Log.d("ZZZZZZZZZZZZZZZZ",String.valueOf(longitude));
            Log.d("ZZZZZZZZZZZZZZZZ",String.valueOf(langitude));
        }
    }
}


