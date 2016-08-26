package com.omsk.bitnic.fatpig;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.util.TimeUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.osmdroid.contributor.util.Util;

import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class FTrack extends Fragment implements View.OnClickListener{


    ImageButton mBtRunn;
    ImageButton mBtPause;
    ImageButton mBtStop;
    TextView mTimerText;
    Chronometer chronometer;
    View mView;
    boolean isStart=true;
    long currentTime=0;
    BroadcastReceiver broadcastReceiver;

    public FTrack() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_ftrack, container, false);


        mTimerText= (TextView) mView.findViewById(R.id.timer_text);
        mBtRunn = (ImageButton) mView.findViewById(R.id.tarack_run);
        mBtPause = (ImageButton) mView.findViewById(R.id.tarack_pause);
        mBtStop = (ImageButton) mView.findViewById(R.id.tarack_stop);
        mBtRunn.setOnClickListener(this);
        mBtPause.setOnClickListener(this);
        mBtStop.setOnClickListener(this);

        final String tag=Settings.getSettings().statusTrack;
        if(tag.equals("1")){
            mBtRunn.setEnabled(false);
            mBtPause.setEnabled(true);
            mBtStop.setEnabled(true);
        }else if(tag.equals("2")){
            mBtRunn.setEnabled(true);
            mBtPause.setEnabled(false);
            mBtStop.setEnabled(true);
        }else if(tag.equals("3")){
            mBtRunn.setEnabled(true);
            mBtPause.setEnabled(true);
            mBtStop.setEnabled(false);
        }





       activateChrono();

         broadcastReceiver = new BroadcastReceiver() {


            @Override
            public void onReceive(Context context, Intent intent) {
                int lototude = intent.getIntExtra(MainActivity.PARAM_LATITUDE, 0);
                int langitude = intent.getIntExtra(MainActivity.PARAM_LONGITUDE, 0);
                Log.d("ZZZZZZZZZZZZZZZZ",String.valueOf(lototude));
                Log.d("ZZZZZZZZZZZZZZZZ",String.valueOf(langitude));
            }
        };

        return mView;
    }
    void activateChrono(){
        chronometer = (Chronometer) mView.findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(final Chronometer chronometer) {
                mTimerText.post(new Runnable() {
                    @Override
                    public void run() {

                        currentTime=SystemClock.elapsedRealtime()-chronometer.getBase();

                        if(isStart==false){
                            Settings.getSettings().timerStart=currentTime;

                            isStart=true;

                        }
                        if(Settings.getSettings().timerStart==0&&Settings.getSettings().timerList.size()!=0){
                            Settings.getSettings().timerStart=currentTime;
                            Settings.Save();
                        }

                        long res=0;
                        for (timer timer : Settings.getSettings().timerList) {
                            res=res+(timer.stop-timer.start);
                        }
                        res=res+(currentTime-Settings.getSettings().timerStart);


                        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(res);
                        int hour = (int) TimeUnit.MILLISECONDS.toHours(res);
                        int dey = (int) TimeUnit.MILLISECONDS.toDays(res);
                        int secd = (int) TimeUnit.MILLISECONDS.toSeconds(res);
                        int secCore= (int) (secd-(minutes*60)-(hour*60*60)-(dey*24*60*60));

                        mTimerText.setText(Utils.getStringDecimal(dey)+"."+Utils.getStringDecimal(hour)+"."+Utils.getStringDecimal(minutes)+"."+Utils.getStringDecimal(secCore));}


                });
            }
        });

    }

    @Override
    public void onDestroy() {
            super.onDestroy();
       getActivity(). unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        String tag= (String) v.getTag();
        if(tag.equals("1")){


           // chronometer.destroyDrawingCache();
            chronometer.stop();
//            chronometer=null;
//            activateChrono();
            mBtRunn.setEnabled(false);
            mBtPause.setEnabled(true);
            mBtStop.setEnabled(true);
            Settings.getSettings().statusTrack="1";
            Settings.Save();
            getActivity().startService(new Intent(getContext(), MyServiceGeo.class));
        }else if(tag.equals("2")){


            isStart=true;
            chronometer.stop();
            mBtRunn.setEnabled(true);
            mBtPause.setEnabled(false);
            mBtStop.setEnabled(true);
            Settings.getSettings().statusTrack="2";

            timer timer = new timer(Settings.getSettings().timerStart,currentTime);
            Settings.getSettings().timerList.add(timer);
            Settings.getSettings().timerStart=0;
            Settings.Save();
            getActivity().stopService(new Intent(getContext(), MyServiceGeo.class));
        }else if(tag.equals("3")){
            isStart=false;
            isStart=true;
            chronometer.stop();
            mBtRunn.setEnabled(true);
            mBtPause.setEnabled(true);
            mBtStop.setEnabled(false);
            Settings.getSettings().statusTrack="3";
            Settings.getSettings().timerStart=0;
            Settings.getSettings().timerStop=0;
            Settings.getSettings().timerList.clear();
            Settings.Save();
            getActivity().stopService(new Intent(getContext(), MyServiceGeo.class));
        }
    }
}
