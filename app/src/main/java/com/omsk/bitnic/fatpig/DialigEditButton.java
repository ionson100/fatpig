package com.omsk.bitnic.fatpig;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import Model.ButtonBase;
import orm.Configure;

public class DialigEditButton extends DialogFragment {


    private EditText name;
    private EditText calorie;
    private ButtonBase button;
    private IAction iAction;
    private IAction iActionDismiss;

    public DialigEditButton setButton(ButtonBase work) {
        this.button = work;
        return this;
    }

    public DialigEditButton addIAction(IAction iAction) {
        this.iAction = iAction;
        return this;
    }

    public DialigEditButton addIActionDismiss(IAction iAction) {
        this.iActionDismiss = iAction;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(iActionDismiss!=null){
            iActionDismiss.Action(null);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_edit_button, null);
        builder.setView(v);
        name= (EditText) v.findViewById(R.id.button_name2);
        calorie= (EditText) v.findViewById(R.id.button_calories);
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.button_check_isshow);


        name.setText(button.name);
        calorie.setText(String.valueOf(button.calories));
        checkBox.setChecked(button.ishow);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                button.name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        calorie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    button.calories = Double.parseDouble(s.toString());
                } catch (Exception e) {
                    button.calories = 0d;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                button.ishow = isChecked;
            }
        });

        v.findViewById(R.id.bt_cancel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        v.findViewById(R.id.bt_save1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    if(iAction!=null){
                        iAction.Action(button);

                    }
                    dismiss();
                }

            }
        });





        return builder.create();
    }

    private boolean validate() {
        if(button.name==null||button.name.trim().length()==0){

            name.setError("Поле не заполнено");

            return false;

        }
        if(button.calories==0d){

            calorie.setError("Поле не заполнено");

            return false;

        }
        return true;
    }


}