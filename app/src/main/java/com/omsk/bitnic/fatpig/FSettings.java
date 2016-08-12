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


        mView.findViewById(R.id.bt_cancell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.getSettings().setStateSystem(StateSystem.HOME,getActivity());
            }
        });

        mView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reanimator.save(Settings.class);
                Settings.getSettings().setStateSystem(StateSystem.HOME, getActivity());
            }
        });


        Settingion mSettingsEngine = (Settingion) mView.findViewById(R.id.setting_panel);
        mSettingsEngine.setModelClass(Settings.class, getActivity());


        return mView;
    }

}
