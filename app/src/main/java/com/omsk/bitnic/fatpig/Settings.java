package com.omsk.bitnic.fatpig;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.settings.ion.mylibrary.Reanimator;

import java.io.Serializable;


public class Settings implements Serializable {

    private static transient Settings settings;

    private int _stateSystem;

    public static Settings getSettings(){
        if(settings==null){
            settings= (Settings) Reanimator.get(Settings.class);
        }
        return settings;
    }
    public int getSateSystem(){
        return _stateSystem;
    }
    public void setStateSystem(int stateSystem,Activity activity){
        _stateSystem=stateSystem;
        Reanimator.save(Settings.class);
        FactoryFragment.Action((AppCompatActivity) activity);
    }
}
