package com.settings.ion.mylibrary;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;


public class SimpleDialog extends DialogFragment {
    private Object curObject;
    private InnerAttribute attribute;
    private EditText text;
    private String title;
    private String subTitle;

    public SimpleDialog() {

    }
    // private Settingion.OnSettingChangeListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View d = inflater.inflate(R.layout.dialog, container, false);
        text = (EditText) d.findViewById(R.id.edittext);
        Button btCancel = (Button) d.findViewById(R.id.btCancel);
        Button btOk = (Button) d.findViewById(R.id.btOk);
        TextView subtitle = (TextView) d.findViewById(R.id.subTitle);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Field field = curObject.getClass().getDeclaredField(attribute.fieldName);
                    field.setAccessible(true);
                    Object value = text.getText().toString();
                    if (attribute.settingField.typeField() == TypeField.Float) {

                        field.set(curObject, Float.parseFloat(String.valueOf(value)));
                    } else if (attribute.settingField.typeField() == TypeField.Integer) {
                        field.set(curObject, Integer.parseInt(String.valueOf(value)));
                    } else {
                        field.set(curObject, value);
                    }
                    if (Reanimator.mIListener != null) {
                        Reanimator.notify(curObject, field.getName(), null, value);
                    } else {
                        Reanimator.save(curObject.getClass());
                    }


                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                dismiss();


            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        text.requestFocus();
        getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);
        //   getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
        Dialog dialog = getDialog();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (attribute.settingField.typeField() == TypeField.Float) {
            text.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        } else if (attribute.settingField.typeField() == TypeField.Integer) {
            text.setInputType(InputType.TYPE_CLASS_NUMBER);

        } else if (attribute.settingField.typeField() == TypeField.Mail) {
            text.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }

        try {
            Field field = curObject.getClass().getDeclaredField(attribute.fieldName);
            field.setAccessible(true);
            Object value = field.get(curObject);
            text.setText(value.toString());

        } catch (Exception e) {
            new RuntimeException("Reanimator,SimpleDialog:" + e.getMessage());
        }

        getDialog().setTitle(title);
        if (attribute.settingField.descriptions() != 0) {
            subtitle.setText(subTitle);
        }


        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_style);
        return d;

    }

    public void showDialog(Class aClass, InnerAttribute innerAttribute, FragmentManager manager, String title, String subtitle) {

        curObject = Reanimator.get(aClass);
        attribute = innerAttribute;
        this.title = title;
        this.subTitle = subtitle;

        this.show(manager, "");
    }
}
