package com.settings.ion.mylibrary;


import android.content.Context;
import android.content.DialogInterface;

import com.settings.ion.mylibrary.colorpicker.ColorPickerView;
import com.settings.ion.mylibrary.colorpicker.OnColorSelectedListener;
import com.settings.ion.mylibrary.colorpicker.builder.ColorPickerClickListener;
import com.settings.ion.mylibrary.colorpicker.builder.ColorPickerDialogBuilder;

public class ColorPickerDialogE {

    private Context context;
    private IAction iAction;

    public ColorPickerDialogE(Context context,IAction iAction){
        this.context = context;

        this.iAction = iAction;
    }

    public interface IAction{
        void Action(int color);
    }
    public void show(int color,String name) {

        ColorPickerDialogBuilder
                .with(context)
                .setTitle(name)
                .initialColor(color)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {}
                })
                .setPositiveButton("Сохранить", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        if(iAction!=null){
                            iAction.Action(selectedColor);
                        }
                    }
                })
                .setNegativeButton("Отказаться", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();

    }
}
