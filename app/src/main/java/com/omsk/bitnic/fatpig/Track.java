package com.omsk.bitnic.fatpig;

import java.util.List;

import Model.GeoData;

/**
 * Created by USER on 29.08.2016.
 */
public class Track {
    public long trackName;
    public List<GeoData> list;

    public String name() {
        return Utils.simpleDateFormat(trackName);
    }

    public static void getTrackLive() {

    }
}
