package com.omsk.bitnic.fatpig;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import java.util.List;

import Model.FButtonEat;

public class FactoryFragment {

    public static void Action( AppCompatActivity activity) {

        FillData.fill(activity);
        Settings mSettings=Settings.getSettings();
                ((LinearLayout) activity.findViewById(R.id.panel_base)).removeAllViews();
         android.support.v4.app.FragmentManager mFragmentManager = activity.getSupportFragmentManager();
         FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        removeFragment(mFragmentManager,mFragmentTransaction);

        if(mSettings.getSateSystem()==StateSystem.HOME){
            MainActivity.LISTTRAFIC.clear();
        }else{
            if(MainActivity.LISTTRAFIC.size()>0&&MainActivity.LISTTRAFIC.get(MainActivity.LISTTRAFIC.size()-1)!=mSettings.getSateSystem()){
                MainActivity.LISTTRAFIC.add(mSettings.getSateSystem());
            }
            if(MainActivity.LISTTRAFIC.size()==0){
                MainActivity.LISTTRAFIC.add(mSettings.getSateSystem());
            }

        }


        switch (mSettings.getSateSystem()) {
            case StateSystem.HOME: {
                showHome(mFragmentTransaction);

                break;
            }
            case StateSystem.USER_SETTINGS: {
                showUserSettings(mFragmentTransaction);
                break;
            }
            case StateSystem.PRODUCT: {
                showProduct(mFragmentTransaction);
                break;
            }
            case StateSystem.WORK: {
                showWork(mFragmentTransaction);
                break;
            }
            case StateSystem.SETTINGS: {
                showSettings(mFragmentTransaction);
                break;
            }
            case StateSystem.MAP: {
                showMap(mFragmentTransaction);
                break;
            }
            case StateSystem.TRACK: {
                showTrack(mFragmentTransaction);
                break;
            }

            case StateSystem.TRACK_SHOW: {
                showTrackShow(mFragmentTransaction);
                break;
            }
            case StateSystem.MAP_TRACK: {
                showMap(mFragmentTransaction);
                break;
            }
            case StateSystem.LIFE: {
                showLife(mFragmentTransaction);
                break;
            }

            case StateSystem.BUTTON_EAT: {
                showButton(mFragmentTransaction);
                break;
            }
            case StateSystem.BUTTON_WORK: {
                showButton(mFragmentTransaction);
                break;
            }
            case StateSystem.TIMER_WORK: {
                showTimerWork(mFragmentTransaction);
                break;
            }

        }


    }

    private static void showTimerWork(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FTimerWork(), "timerw");
        mFragmentTransaction.commit();
    }

    private static void showButton(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FButtonEat(), "dd");
        mFragmentTransaction.commit();
    }

    private static void showLife(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FLife(), "life");
        mFragmentTransaction.commit();
    }

    private static void showTrackShow(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FTrackShow(), "trackshow");
        mFragmentTransaction.commit();
    }

    private static void showTrack(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FTrack(), "track");
        mFragmentTransaction.commit();
    }

    private static void showMap(FragmentTransaction mFragmentTransaction) {

        mFragmentTransaction.add(R.id.panel_base, new FMap(), "map");
        mFragmentTransaction.commit();
    }

    private static void showSettings(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FSettings(), "settings");
        mFragmentTransaction.commit();
    }

    private static void showWork(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FWork(), "work");
        mFragmentTransaction.commit();
    }

    private static void showProduct(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FProduct(), "product");
        mFragmentTransaction.commit();
    }

    private static void showUserSettings(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FUserSettings(), "user_settings");
        mFragmentTransaction.commit();
    }

    private static void showHome(FragmentTransaction mFragmentTransaction) {
        mFragmentTransaction.add(R.id.panel_base, new FHome(), "home");
        mFragmentTransaction.commit();
    }

    private static void removeFragment(android.support.v4.app.FragmentManager mFragmentManager,FragmentTransaction mFragmentTransaction) {

        List<Fragment> df = mFragmentManager.getFragments();
        if (df != null && df.size() > 0) {
            for (android.support.v4.app.Fragment fragment : df) {
                if (fragment != null) {
                    mFragmentTransaction.remove(fragment);
                }
            }
        }
    }
}
