package com.omsk.bitnic.fatpig;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import java.util.List;

public class FactoryFragment {

    public static void Action( AppCompatActivity activity) {

        FillData.fill(activity);
        Settings mSettings=Settings.getSettings();
                ((LinearLayout) activity.findViewById(R.id.panel_base)).removeAllViews();
         android.support.v4.app.FragmentManager mFragmentManager = activity.getSupportFragmentManager();
         FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        removeFragment(mFragmentManager,mFragmentTransaction);
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
                showMap(mFragmentTransaction, activity);
                break;
            }

        }


    }

    private static void showMap(FragmentTransaction mFragmentTransaction, AppCompatActivity activity) {

        mFragmentTransaction.add(R.id.panel_base, new FMap2(), "map");
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
