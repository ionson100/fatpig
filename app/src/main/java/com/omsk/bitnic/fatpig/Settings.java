package com.omsk.bitnic.fatpig;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.settings.ion.mylibrary.Reanimator;
import com.settings.ion.mylibrary.SettingField;
import com.settings.ion.mylibrary.TypeField;

import java.io.Serializable;


public class Settings implements Serializable {



    @SettingField(

            descriptions = R.string.settings_percent,
            index = -1,
            title = R.string.settings_percent_title,
            typeField = TypeField.Integer)
    private int percent=80;


    @SettingField(

            defaultColor = 0x90C3D4,
            index = 2,
            title = R.string.color_panel1,
            typeField = TypeField.Color)
    public int colorPanel1= 0xdec0a8;

    @SettingField(
            defaultColor = 0x7BED90,
            index = 3,
            title = R.string.color_panel2,
            typeField = TypeField.Color)
    public int colorPanel2= 0xaef599;


    @SettingField(
            defaultColor = 0xD1ADF7,
            index = 4,
            title = R.string.color_panel3,
            typeField = TypeField.Color)
    public int colorPanel3= 0x46c9bc;



    @SettingField(
            defaultColor = 0xF1F7AD,
            index = 5,
            title = R.string.color_panel3,
            typeField = TypeField.Color)
    public int colorPanel4= 0xf29951;





    @SettingField(
            defaultColor = 0xC5C9C6,
            index = 6,
            title = R.string.color_panel_top,
            typeField = TypeField.Color)
    public int colorTopPanel =0xFFFFFFFF;



    private static transient Settings settings;
//,
    private int _stateSystem;
    public double latitude=56.819847;
    public double longitude=60.640622;
    public float zoom=16;


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


    public int getPercent(){
        return percent;
    }
    public void setPercent(int value){
        percent=value;
        Reanimator.save(Settings.class);
    }

}
