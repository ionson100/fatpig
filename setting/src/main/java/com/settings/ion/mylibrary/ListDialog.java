package com.settings.ion.mylibrary;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

/**
 *
 */
public class ListDialog extends DialogFragment {

    private Object curObject;
    private InnerAttribute attribute;
    private String title;
    private String subTitle;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View d = inflater.inflate(R.layout.dalog_list, container, false);
        TextView subtitle = (TextView) d.findViewById(R.id.subTitle);
        RadioGroup radioGroup = (RadioGroup) d.findViewById(R.id.radogrup);

        Class<?>[] c = attribute.settingField.IListItem().getInterfaces();

        boolean isContains = false;
        for (Class<?> aClass : c) {
            if (aClass == IListItem.class) {
                isContains = true;
                break;
            }
        }
        if (!isContains) {
            throw new RuntimeException("no contains IListItem");
        }
        IListItem iListItem;
        try {
            // Object sd=attribute.settingField.IListItem().newInstance();
            iListItem = (IListItem) attribute.settingField.IListItem().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        Object value;
        try {
            Field field = curObject.getClass().getDeclaredField(attribute.fieldName);
            field.setAccessible(true);
            value = field.get(curObject);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        List<Item> items = iListItem.getList();
        for (Item item : items) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setPadding(20, 20, 20, 20);
            radioButton.setTextColor(Color.BLACK);
            radioButton.setText(item.key.toString());
            radioButton.setTag(item.value);
            if (value != null && value.equals(item.value)) {
                radioButton.setChecked(true);
            }
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton button = (RadioButton) v;
                    Field field = null;
                    Object val = null;
                    try {
                        val = button.getTag();
                        field = curObject.getClass().getDeclaredField(attribute.fieldName);
                        field.setAccessible(true);
                        field.set(curObject, val);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    if (Reanimator.mIListener != null) {
                        Reanimator.notify(curObject, attribute.fieldName, null, val);
                    } else {
                        Reanimator.save(curObject.getClass());
                    }

//
                    dismiss();
                }
            });
            radioGroup.addView(radioButton);

        }
        getDialog().setTitle(title);
        if (attribute.settingField.descriptions() != 0) {
            subtitle.setText(subTitle);
        }

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_style);
        return d;
    }


    public void showDialog(Class aClass, InnerAttribute innerAttribute, FragmentManager manager,
                           String title, String subtitle, Context context) {
        curObject = Reanimator.get(aClass);
        attribute = innerAttribute;
        this.title = title;
        this.subTitle = subtitle;
        this.context = context;

        this.show(manager, "");
    }

}
