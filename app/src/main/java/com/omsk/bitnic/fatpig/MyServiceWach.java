package com.omsk.bitnic.fatpig;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyServiceWach extends Service {
    public MyServiceWach() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onTaskRemoved(Intent rootIntent) {

        //unregister listeners
        //do any other cleanup if required

        //stop service
        stopSelf();
    }
}
