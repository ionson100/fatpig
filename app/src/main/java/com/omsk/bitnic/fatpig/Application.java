package com.omsk.bitnic.fatpig;


import orm.Configure;

public class Application extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        new Configure(getApplicationInfo().dataDir + "/ion100.sqlite", getApplicationContext(),false);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
