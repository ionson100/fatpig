package com.settings.ion.mylibrary;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DialogSubmenu extends DialogFragment {

    private InnerAttribute innerAttribute;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_sub_menu, null);
        Settingion settingion = (Settingion) v.findViewById(R.id.subsetting);
        settingion.setModelClass(innerAttribute.aClass, getActivity());
        getDialog().setTitle(innerAttribute.settingField.title());
        return v;
    }

    public void showDialog(Object host, InnerAttribute attribute, FragmentManager manager) {
        innerAttribute = attribute;
        show(manager, "");
    }
}
