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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import Model.Product;
import Model.Work;

/**
 * Created by USER on 11.08.2016.
 */
public class DialogAddOdEditWork extends DialogFragment {

    private IAction iAction;
    private Work work;
    private IAction iActionDismiss;
    private EditText editTextName;
    private EditText editTextCal;

    public DialogAddOdEditWork addIActionDiasmiss(IAction iAction){
        this.iActionDismiss=iAction;
        return  this;
    }

    public DialogAddOdEditWork addIAction(IAction iAction){
        this.iAction=iAction;
        return  this;
    }
    public DialogAddOdEditWork addWork(Work work){
        this.work=work;
        return  this;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vi;
        vi = LayoutInflater.from(getActivity());
        View v = vi.inflate(R.layout.dialog_add_product, null);
        builder.setView(v);
        editTextName= (EditText) v.findViewById(R.id.dialog_add_name);
        editTextCal= (EditText) v.findViewById(R.id.dialog_add_calor);
        editTextCal.setHint("Калории на еденицу веса (кг) в час:");
        Button buttonCancel= (Button) v.findViewById(R.id.bt_dialog_add_canell);
        Button buttonOk= (Button) v.findViewById(R.id.bt_dialog_add_ok);
        final CheckBox checkBox= (CheckBox) v.findViewById(R.id.checkBox_pref);
        checkBox.setVisibility(View.GONE);


        if(work.calorieses==0d){
            editTextCal.setText("");
        }else{
            editTextCal.setText(String.valueOf(work.calorieses));
        }
        editTextName.setText(work.name);


        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               work.name=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextCal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 try{
                     work.calorieses=Double.parseDouble(s.toString());
                 }catch (Exception e){
                     work.calorieses=0d;
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){

                    if(iAction!=null){
                        iAction.Action(work);
                    }
                    dismiss();
                }
            }


        });

        return builder.create();
    }

    private boolean validate() {
        if(work.name==null||work.name.trim().length()==0){
            editTextName.setError("Поле не заполнено");
            return false;
        }
        if(work.calorieses==0d){
            editTextCal.setError("Поле не заполнено");
            return false;
        }
            return true;
    }


}
