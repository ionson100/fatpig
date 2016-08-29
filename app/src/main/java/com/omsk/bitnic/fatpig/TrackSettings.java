package com.omsk.bitnic.fatpig;

import com.settings.ion.mylibrary.Reanimator;

public class TrackSettings {
    private transient static TrackSettings tracksettings;

    public int trackName;

    public String startTab="t1";

    public long timeWhenStopped;

    public long timeTimeDelta;

    public String statusTrack="0";

    public static TrackSettings getCore(){
        if(tracksettings==null){
            tracksettings= (TrackSettings) Reanimator.get(TrackSettings.class);
        }
        return tracksettings;
    }

    public static void save(){
        Reanimator.save(TrackSettings.class);
    }




}
