package com.omsk.bitnic.fatpig;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.settings.ion.mylibrary.Reanimator;
import com.settings.ion.mylibrary.Settingion;


public class FSettings extends Fragment {

    View mView;

    public FSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_fsettings, container, false);

        Settingion mSettingsEngine = (Settingion) mView.findViewById(R.id.setting_panel);
        mSettingsEngine.setModelClass(Settings.class, getActivity());


        return mView;
    }

}
