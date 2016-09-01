package com.omsk.bitnic.fatpig;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import Model.Life;
import orm.Configure;


public class DialogEditLife extends DialogFragment {

    public interface  IActivate{
        void activate(Life life);
    }

    private Life life;
    private IActivate iActivate;

    public DialogEditLife setLife(Life life){
        this.life=life;
        return this;
    }

    public DialogEditLife setActivate(IActivate activate){
        this.iActivate=activate;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_edit_life, null);
        builder.setView(v);
        EditText mass= (EditText) v.findViewById(R.id.edit_mas);
        EditText pressure= (EditText) v.findViewById(R.id.edit_pressure);
        EditText calories= (EditText) v.findViewById(R.id.edit_calories);
        EditText comment= (EditText) v.findViewById(R.id.edit_comment);

        mass.setText(String.valueOf(life.mass));
        pressure.setText(life.pressure);
        calories.setText(String.valueOf(life.calories));
        comment.setText(life.commentary);

        mass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    life.mass=Double.parseDouble(s.toString());
                }catch (Exception e){
                    life.mass=0d;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        pressure.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              life.pressure=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        calories.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             try{
                 life.calories=Double.parseDouble(s.toString());
             }catch (Exception e){
                 life.calories=0d;
             }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             life.commentary=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        v.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        v.findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iActivate!=null){
                    iActivate.activate(life);
                    Configure.getSession().update(life);
                }
                dismiss();
            }
        });
        return builder.create();
    }



}
