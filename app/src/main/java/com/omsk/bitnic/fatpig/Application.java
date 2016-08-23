package com.omsk.bitnic.fatpig;


import android.content.Context;

import com.settings.ion.mylibrary.Reanimator;

import java.io.IOException;

import orm.Configure;

public class Application extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Reanimator.intContext(getApplicationContext());
        new Configure(getApplicationInfo().dataDir + "/ion100.sqlite", getApplicationContext(),true);
      //  firstStart.execute(getBaseContext());
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
//class firstStart{
//
//   public static void execute(Context context){
//        String sre= null;
//        try {
//            sre = Utils.readFromAssets(context, "sql.text");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Configure.getSession().execSQL(sre,null);
//    }
//}
