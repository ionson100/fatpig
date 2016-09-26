package com.omsk.bitnic.fatpig;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class MyServiceGeoRunner extends Service {

    private Timer mTimer;
    public MyServiceGeoRunner() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimer = new Timer();
        MyTimerTask mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000, 3000);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        mTimer=null;
        super.onDestroy();
    }
    private synchronized void ruun() {

        if (!Utils.isMyServiceRunning(MyServiceGeo.class, getBaseContext())) {
            Utils.start();
            getBaseContext().startService(new Intent(getBaseContext(), MyServiceGeo.class));
        }
    }
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            ruun();
        }
    }

}
