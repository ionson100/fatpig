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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import Model.Product;
import Model.ProductBase;

/**
 * Created by USER on 10.08.2016.
 */
public class DialogAddOrEditProduct extends DialogFragment {


    private IAction iAction;
    private ProductBase product;
    private IAction iActionDismiss;
    private EditText editTextName;
    private EditText editTextCal;

    public DialogAddOrEditProduct addIActionDiasmiss(IAction iAction){
        this.iActionDismiss=iAction;
        return  this;
    }

    public DialogAddOrEditProduct addIAction(IAction iAction){
        this.iAction=iAction;
        return  this;
    }
    public DialogAddOrEditProduct addProduct(ProductBase product){
        this.product=product;
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
        Button buttonCancel= (Button) v.findViewById(R.id.bt_dialog_add_canell);
        Button buttonOk= (Button) v.findViewById(R.id.bt_dialog_add_ok);
        final CheckBox checkBox= (CheckBox) v.findViewById(R.id.checkBox_pref);

        if(product.calorieses!=0d){
            editTextCal.setText(String.valueOf(product.calorieses));
        }

        editTextName.setText(product.name);
        checkBox.setChecked(product.preferences);

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.name=s.toString();
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
                    product.calorieses=Double.parseDouble(s.toString());
                }catch (Exception  e){
                    product.calorieses=0;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                product.preferences=isChecked;
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
                        iAction.Action(product);
                    }
                    dismiss();
                }
            }
        });

        return builder.create();
    }

    private boolean validate() {
        if(product.name==null||product.name.trim().length()==0){
            editTextName.setError("Поле не заполено");
            return false;
        }
        if(product.calorieses==0){
            editTextCal.setError("Поле не заполнено");
            return false;
        }
        return true;
    }
}
