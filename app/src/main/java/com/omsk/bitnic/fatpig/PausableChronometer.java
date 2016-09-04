package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

import java.util.Date;

public class PausableChronometer extends Chronometer {



    public PausableChronometer(Context context) {
        super(context);
    }

    public PausableChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PausableChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void start() {

        long dd= TrackSettings.getCore().timeWhenStopped;
        long res= SystemClock.elapsedRealtime()+dd;
        //Log.d("ssssssssssssssss_START",String.valueOf(dd));
        setBase(res);
        super.start();
        if(TrackSettings.getCore().trackName==0){
            TrackSettings.getCore().trackName=Utils.dateToInt(new Date());
        }

        TrackSettings.getCore().timeTimeDelta=0;
        TrackSettings.getCore().setStatusTrack("1");
        TrackSettings.save();
    }

    @Override
    public void stop() {
        super.stop();
        TrackSettings.getCore().timeWhenStopped = getBase() - SystemClock.elapsedRealtime();
        //Log.d("ssssssssssssssss_STOP",String.valueOf(TrackSettings.getCore().timeWhenStopped ));
        TrackSettings.getCore().setStatusTrack("2");
        TrackSettings.save();
        TrackSettings ss=TrackSettings.getCore();
    }

    public void destoryStop(){
        TrackSettings dd=TrackSettings.getCore();
        if(TrackSettings.getCore().getStatusTrack().equals("1")){

            TrackSettings.getCore().timeWhenStopped = getBase() - SystemClock.elapsedRealtime();
            TrackSettings.getCore().timeTimeDelta = new Date().getTime();
            TrackSettings.save();
        }

    }

    public void reset() {
        stop();
        setBase(SystemClock.elapsedRealtime());
        TrackSettings.getCore().trackName=0;
        TrackSettings.getCore().timeWhenStopped = 0;
        TrackSettings.getCore().setStatusTrack("3");
        TrackSettings.save();
    }

    public long getCurrentTime() {
        return TrackSettings.getCore().timeWhenStopped;
    }

    public void setCurrentTime(long time) {
        TrackSettings.getCore().timeWhenStopped = time;
        setBase(SystemClock.elapsedRealtime()+ TrackSettings.getCore().timeWhenStopped);
        TrackSettings.save();
    }
}

