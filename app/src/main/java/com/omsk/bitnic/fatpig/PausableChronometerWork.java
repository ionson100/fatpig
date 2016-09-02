package com.omsk.bitnic.fatpig;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;



public class PausableChronometerWork extends Chronometer {

    private long timeWhenStopped = 0;

    public PausableChronometerWork(Context context) {
        super(context);
    }

    public PausableChronometerWork(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PausableChronometerWork(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void start() {
        setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        timeWhenStopped = getBase() - SystemClock.elapsedRealtime();
        ChronometerWork.getCore().timeWhenStopped=timeWhenStopped;
        ChronometerWork.Save();

    }

    public void reset() {
        stop();
        setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        ChronometerWork.getCore().timeWhenStopped=timeWhenStopped;
        ChronometerWork.Save();
    }

    public long getCurrentTime() {
       // return timeWhenStopped;
        return   getBase() - SystemClock.elapsedRealtime();
    }

    public void setCurrentTime(long time) {
        timeWhenStopped = time;
        setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
    }
}