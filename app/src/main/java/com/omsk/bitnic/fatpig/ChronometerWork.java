package com.omsk.bitnic.fatpig;

import com.settings.ion.mylibrary.Reanimator;


public class ChronometerWork {

    private static ChronometerWork chronometerWork;
    public long timeWhenStopped;
    public int state;

    public static ChronometerWork getCore() {
        if (chronometerWork == null) {
            chronometerWork = (ChronometerWork) Reanimator.get(ChronometerWork.class);
        }
        return chronometerWork;
    }

    public static void Save() {
        Reanimator.save(ChronometerWork.class);
    }
}
