package com.omsk.bitnic.fatpig;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.settings.ion.mylibrary.Reanimator;
import com.settings.ion.mylibrary.SettingField;
import com.settings.ion.mylibrary.TypeField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Settings implements Serializable {



    @SettingField(

            descriptions = R.string.settings_percent,
            index = -1,
            title = R.string.settings_percent_title,
            typeField = TypeField.Integer)
    private int percent=80;


    @SettingField(

            defaultColor = Color.CYAN,
            index = 2,
            title = R.string.color_panel1,
            typeField = TypeField.Color)
    public int colorPanel1= Color.CYAN;

    @SettingField(
            defaultColor = Color.DKGRAY,
            index = 3,
            title = R.string.color_panel2,
            typeField = TypeField.Color)
    public int colorPanel2= Color.DKGRAY;


    @SettingField(
            defaultColor = Color.GREEN,
            index = 4,
            title = R.string.color_panel3,
            typeField = TypeField.Color)
    public int colorPanel3= Color.GREEN;



    @SettingField(
            defaultColor = Color.MAGENTA,
            index = 5,
            title = R.string.color_panel3,
            typeField = TypeField.Color)
    public int colorPanel4= Color.MAGENTA;





    @SettingField(
            defaultColor = Color.YELLOW,
            index = 6,
            title = R.string.color_panel_top,
            typeField = TypeField.Color)
    public int colorTopPanel =Color.YELLOW;


    public String startTab="t1";

    private static transient Settings settings;
    //,
    private int _stateSystem;
    public double latitude=56.819847;
    public double longitude=60.640622;
    public float zoom=16;
    public transient Track trackshow;


    public static Settings getSettings(){
        if(settings==null){
            settings= (Settings) Reanimator.get(Settings.class);
        }
        return settings;
    }

    public static void  Save(){
        Reanimator.save(Settings.class);
    }
    public int getSateSystem(){
        return _stateSystem;
    }
    public void setStateSystem(int stateSystem,Activity activity){
        _stateSystem=stateSystem;
        Reanimator.save(Settings.class);
        FactoryFragment.Action((AppCompatActivity) activity);
    }


    public int getPercent(){
        return percent;
    }
    public void setPercent(int value){
        percent=value;
        Reanimator.save(Settings.class);
    }

    public long timerStart;
    public long timerStop;

    public String statusTrack="0";

    public List<timer> timerList=new ArrayList<>();

}
