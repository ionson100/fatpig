package com.omsk.bitnic.fatpig;


import com.settings.ion.mylibrary.Reanimator;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.osmdroid.http.HttpClientFactory;
import org.osmdroid.http.IHttpClientFactory;

import orm.Configure;

public class Application extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Reanimator.intContext(getApplicationContext());
        new Configure(getApplicationInfo().dataDir + "/ion100.sqlite", getApplicationContext(), false);
        //  firstStart.execute(getBaseContext());
        HttpClientFactory.setFactoryInstance(new IHttpClientFactory() {
            @Override
            public org.apache.http.client.HttpClient createHttpClient() {
                final DefaultHttpClient client = new DefaultHttpClient();
                client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
                return client;
            }
        });

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

