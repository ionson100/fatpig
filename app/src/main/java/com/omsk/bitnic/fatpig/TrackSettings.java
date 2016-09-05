package com.omsk.bitnic.fatpig;

import com.settings.ion.mylibrary.Reanimator;

public class TrackSettings {
    private transient static TrackSettings tracksettings;

    public long trackName;
    public String statusTrack="";
    public long stoper;
    public long runner;


    public static TrackSettings getCore() {
        if (tracksettings == null) {
            tracksettings = (TrackSettings) Reanimator.get(TrackSettings.class);
        }
        return tracksettings;
    }

    public static void save() {
        Reanimator.save(TrackSettings.class);
    }



}
