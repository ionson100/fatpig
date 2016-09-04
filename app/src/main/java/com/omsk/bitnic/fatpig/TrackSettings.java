package com.omsk.bitnic.fatpig;

import com.settings.ion.mylibrary.Reanimator;

public class TrackSettings {
    private transient static TrackSettings tracksettings;

    public int trackName;

    public String startTab="t1";

    public long timeWhenStopped;

    public long timeTimeDelta;

    private String statusTrack="0";

    public static TrackSettings getCore(){
        if(tracksettings==null){
            tracksettings= (TrackSettings) Reanimator.get(TrackSettings.class);
        }
        return tracksettings;
    }

    public static void save(){
        Reanimator.save(TrackSettings.class);
    }


    public String getStatusTrack() {
        return statusTrack;
    }

    public void setStatusTrack(String statusTrack) {
        this.statusTrack = statusTrack;
    }
}
